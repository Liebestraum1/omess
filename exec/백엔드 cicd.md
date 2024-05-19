# 백엔드 CI/CD 설정

## CI

### 환경변수 및 설정

- .env
```text
DB_URL=jdbc:{MYSQL_URL}
DB_USERNAME={MYSQL_ROOT}
DB_PASSWORD={MYSQL_ROOT_PWD}

TEST_DB_URL={MYSQL_TEST_URL}
TEST_DB_USERNAME={MYSQL_TEST_ROOT}
TEST_DB_PASSWORD={MYSQL_TEST{PWD}}

MONGODB_HOST={MONGO_URL}
MONGODB_PORT={MONGO_PORT}
MONGODB_DATABASE={MONGO_DB}

MINIO_URL={MINIO_URL}
MINIO_BUCKET={MINIO_BUCKET}
MINIO_TEST_BUCKET={MINIO_TEST_BUCKET}
MINIO_ACCESS_KEY={MINIO_ACCESS_KEY}
MINIO_SECRET_KEY={MINIO_SECRET_KEY}
DOMAIN_NAME=https://{SERVER_URL}
```

- application.yml
```yaml
server:
  servlet:
    session:
      timeout: 7d
      cookie:
        max-age: 7d

spring:
  config:
    import:
      - optional:file:env/.env[.properties]

  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    properties:
      hibernate:
        format_sql: true

minio:
  url: ${MINIO_URL}
  path: ${MINIO_PATH}
  bucket:
    name: ${MINIO_BUCKET}
  key:
    access: ${MINIO_ACCESS_KEY}
    secret: ${MINIO_SECRET_KEY}

domain:
  name: ${DOMAIN_NAME}
```

### Jenkins Pipeline

```groovy
// backend ci

pipeline {
    agent any

    tools {
        jdk "jdk 21"
    }

    environment {
        JAVA_HOME = "tool jdk 21"
    }

    stages {
        stage('Copy env') {
            steps {
            withCredentials([file(credentialsId: 'backend_env', variable: 'env')]) {
                script {
                        sh 'mkdir -p back/env'
                        sh 'chmod 755 $env'
                        sh 'cp -f -R $env back/env/.env'
                    }
                }
            }
        }
        stage('Test stage') {
            steps {
                dir('back') {
                    script {
                        echo 'Running tests...'
                        sh 'chmod +x ./gradlew'
                        sh './gradlew clean test'
                    }

                }
            }
            post {
                success {
                    echo 'Test success'
                    updateGitlabCommitStatus name: 'test', state: 'success'
                }
                failure {
                    echo 'Test failed'
                    updateGitlabCommitStatus name: 'test', state: 'failed'
                }
            }
        }
        stage('Build stage') {
            steps {
                dir('back') {
                    script {
                        echo 'Running build...'
                        sh 'chmod +x ./gradlew'
                        sh './gradlew clean build -x test --parallel'
                    }
                }
            }
            post {
                success {
                    echo 'Build success'
                    updateGitlabCommitStatus name: 'build', state: 'success'
                }
                failure {
                    echo 'Build failed'
                    updateGitlabCommitStatus name: 'build', state: 'failed'
                }
            }
        }
    }
}
```

## CD

### 환경변수 및 설정

- application.yml

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: { MYSQL_URL }
    username: { MYSQL_ROOT }
    password: { MYSQL_ROOT_PWD }

  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    properties:
      hibernate:
        format_sql: true

  data:
    mongodb:
      host: { MONGO_URL }
      port: { MONGO_PORT }
      database: { MONGO_DB_NAME }
      username: { MONGO_ROOT }
      password: { MONGO_ROOT_PWD }

  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB

minio:
  url: { MINIO_URL }
  bucket:
    name: { MINIO_BUCKET_NAME }
  key:
    access: { MINIO_ACCESS_KEY }
    secret: { MINIO_SECRET_KEY }

management:
  endpoint:
    health:
      show-details: always
      status:
        http-mapping:
          down: 503
          fatal: 503
          out-of-service: 503

domain:
  name: https://{ SERVICE_ROOT_URL }
```

### Jenkins Pipeline

```groovy
def oldBackend
def newBackend

def removeDockerContents(String name) {
    sh script: """
    if [ \$(docker ps -a -q -f name=${name}) ]; then
        docker stop ${name}
    fi
    if [ \$(docker ps -aq -f status=exited -f name=${name}) ]; then
        docker rm ${name}
    fi
    docker image prune -f
    """, label: 'Removing Docker contents'
}

pipeline {
    agent any

     tools {
    jdk "jdk 21"
    }

    environment {
    JAVA_HOME = "tool jdk 21"
    }

    stages {
        stage('Get current backend') {
            steps {
                script {
                    echo 'Get current backend...'
                    oldBackend = sh(script: """docker exec nginx grep "set \\\$current_backend" /etc/nginx/conf.d/custom.conf | awk -F '[ \\t]+' '{print \$4}' | tr -d ';'""", returnStdout: true).trim()

                    if(oldBackend == 'backend-blue') {
                        newBackend = 'backend-green'
                    } else if(oldBackend == 'backend-green') {
                        newBackend = 'backend-blue'
                    } else {
                        currentBuild.result = 'FAILURE'
                        error('Get current backend fail')
                    }
                }
            }

            post {
                always {
                    echo "Current Backend: ${oldBackend}"
                }
                success {
                    echo "Get backend success"
                    updateGitlabCommitStatus name: 'check', state: 'success'
                }
                failure {
                    echo "Get backend failed"
                    updateGitlabCommitStatus name: 'check', state: 'failed'
                }
            }
        }
        stage('Copy env') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'application_yaml', variable: 'application')]) {
                        script {
                            sh 'chmod 755 $application'
                            sh 'cp -f -R $application back/src/main/resources/application.yml'
                        }
                    }
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    echo 'Build gradle...'
                    dir('back') {
                        sh "chmod +x ./gradlew"
                        sh "./gradlew clean build -x check --parallel"
                    }

                        echo 'Docker build...'
                        sh "docker build --build-arg JAR_FILE=back/build/libs/omess-0.0.1-SNAPSHOT.jar -f ./docker/back-docker -t ${newBackend} ."
                }
            }
            post {
                success {
                    echo "Build success"
                    updateGitlabCommitStatus name: 'build', state: 'success'
                }
                failure {
                    echo "Build failed"
                    updateGitlabCommitStatus name: 'build', state: 'failed'
                }
            }
        }
        stage('Run') {
            steps {
                script {
                    echo 'Docker run...'
                    sh "docker run -d --net omess --name ${newBackend} ${newBackend}"
                }
            }
            post {
                success {
                    echo "Run success"
                    updateGitlabCommitStatus name: 'run', state: 'success'
                }
                failure {
                    echo "Run failed"
                    updateGitlabCommitStatus name: 'run', state: 'failed'
                }
            }
        }
        stage('Health check') {
            steps{
                script {
                    // sleep 시간은 유동적으로 설정 가능
                    sleep 20
                    def status = sh(script: "curl -s -o /dev/null -I -w \"%{http_code}\" http://${newBackend}:8080/actuator/health", returnStdout: true).trim()

                    if(status != '200') {
                        currentBuild.result = 'FAILURE'
                        error('Health check fail')
                    }
                }
            }
            post {
                success {
                    echo "Health check success"
                    updateGitlabCommitStatus name: 'healthcheck', state: 'success'
                }
                failure {
                    echo "Health check failed"
                    removeDockerContents(newBackend)
                    updateGitlabCommitStatus name: 'healthcheck', state: 'failed'
                }
            }
        }
        stage('Update backend') {
            steps{
                script {
                    echo 'Update nginx conf...'

                    sh "docker exec nginx sed -i 's/set \$current_backend ${oldBackend};/set \$current_backend ${newBackend};/g' /etc/nginx/conf.d/custom.conf"
                    sh 'docker exec nginx nginx -s reload'

                    removeDockerContents(oldBackend)
                }
            }
            post {
                success {
                    echo "Update success"
                    updateGitlabCommitStatus name: 'update', state: 'success'
                }
                failure {
                    echo "Update failed"
                    removeDockerContents(newBackend)
                    updateGitlabCommitStatus name: '', state: 'failed'
                }
            }
        }
    }
}
```