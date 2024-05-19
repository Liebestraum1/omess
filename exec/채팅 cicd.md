# 채팅 CI/CD 설정

## CI

### 환경변수 및 설정

- mysql이 r2dbc를 사용합니다.

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

TEST_MONGODB_HOST={MONGO_TEST_URL}
TEST_MONGODB_PORT={MONGO_TEST_PORT}
TEST_MONGODB_DATABASE={MONGO_TEST_DB}
TEST_MONGODB_USERNAME={MONGO_TEST_ROOT}
TEST_MONGODB_PASSWORD={MONGO_TEST_ROOT_PWD}

ALLOW_ORIGIN=https://{SERVER_URL}
```

- applicaiton.yml

```yaml
server:
  port: 8081
spring:
  config:
    import:
      - optional:file:env/.env[.properties]

  r2dbc:
    url: ${MYSQL_URL}
    username: ${MYSQL_USERNAME}
    password: ${MYSQL_PASSWORD}

  data:
    mongodb:
      host: ${MONGODB_HOST}
      port: ${MONGODB_PORT}
      database: ${MONGODB_DATABASE}
      username: ${MONGODB_USERNAME}
      password: ${MONGODB_PASSWORD}
allow:
  origin: ${ALLOW_ORIGIN}

logging:
  level:
    web: debug
```

### Jenkins Pipeline

```groovy
// chat ci

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
                withCredentials([file(credentialsId: 'chat_env', variable: 'env')]) {
                    script {
                        sh 'mkdir -p omess-chat/env'
                        sh 'chmod 755 $env'
                        sh 'cp -f -R $env omess-chat/env/.env'
                    }
                }
            }
        }
        stage('Test stage') {
            steps {
                dir('omess-chat') {
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
                dir('omess-chat') {
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

- mysql이 r2dbc를 사용합니다.

- application.yml

```yaml
spring:

  r2dbc:
    url: {MYSQL_URL}
    username: {MYSQL_ROOT}
    password: {MYSQL_PWD}

  data:
    mongodb:
      host: {MONGO_URL}
      port: {MONGO_PORT}
      database: {MONGO_DB}
      username: {MONGO_ROOT}
      password: {MONGO_ROOT_PWD}

allow:
  origin: https://{SERVER_URL}

management:
  endpoint:
    health:
      show-details: always
      status:
        http-mapping:
          down: 503
          fatal: 503
          out-of-service: 503

logging:
  level:
    web: debug
```

### Jenkins Pipeline

```groovy
def oldChat
def newChat

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
        stage('Get current chat') {
            steps {
                script {
                    echo 'Get current chat...'
                    oldChat = sh(script: """docker exec nginx grep "set \\\$current_chat" /etc/nginx/conf.d/custom.conf | awk -F '[ \\t]+' '{print \$4}' | tr -d ';'""", returnStdout: true).trim()
                    
                    if(oldChat == 'chat-blue') {
                        newChat = 'chat-green'
                    } else if(oldChat == 'chat-green') {
                        newChat = 'chat-blue'
                    } else {
                        currentBuild.result = 'FAILURE'
                        error('Get current chat fail')
                    }
                }
            }
            post {
                always {
                    echo "Current Chat: ${oldChat}"
                }
                success {
                    echo "Get chat success"
                    updateGitlabCommitStatus name: 'check', state: 'success'
                }
                failure {
                    echo "Get chat failed"
                    updateGitlabCommitStatus name: 'check', state: 'failed'
                }
            }
        }
        stage('Copy env') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'chat_yml', variable: 'application')]) {
                        script {
                            sh 'chmod 755 $application'
                            sh 'cp -f -R $application omess-chat/src/main/resources/application.yml'
                        }
                    }
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    echo 'Build gradle...'
                    dir('omess-chat') {
                        sh "chmod +x ./gradlew"
                        sh "./gradlew clean build -x check --parallel"
                    }
                    echo 'Docker build...'
                    sh "docker build --build-arg JAR_FILE=omess-chat/build/libs/omess-chat-0.0.1-SNAPSHOT.jar -f ./docker/chat-docker -t ${newChat} ."
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
                    sh "docker run -d --net omess --name ${newChat} ${newChat}"
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
                    sleep 10
                    def status = sh(script: "curl -s -o /dev/null -I -w \"%{http_code}\" http://${newChat}:8080/actuator/health", returnStdout: true).trim()

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
                    removeDockerContents(newChat)
                    updateGitlabCommitStatus name: 'healthcheck', state: 'failed'
                }
            }
        }
        stage('Update chat') {
            steps{
                script {
                    echo 'Update nginx conf...'
                    sh "docker exec nginx sed -i 's/set \$current_chat ${oldChat};/set \$current_chat ${newChat};/g' /etc/nginx/conf.d/custom.conf"
                    sh 'docker exec nginx nginx -s reload'
                    removeDockerContents(oldChat)
                }
            }
            post {
                success {
                    echo "Update success"
                    updateGitlabCommitStatus name: 'update', state: 'success'
                }
                failure {
                    echo "Update failed"
                    removeDockerContents(newChat)
                    updateGitlabCommitStatus name: '', state: 'failed'
                }
            }
        }
    }
}
```