# 프론트엔드 CI/CD 설정

## 환경변수 및 설정
```text
VITE_SERVER_URL=https://{SERVER_URL}
VITE_WEBSOCKET_URL={SERVER_URL}
VITE_APPLICATION_TYPE=server
VITE_SECRET_KEY={SECRET_KEY}
VITE_CHAT_WEBSOCKET_URL=wss://{SERVER_URL}/chat/v1/
```

## CI

```groovy
pipeline {
    agent any

    stages{
        stage('Copy env') {
            steps {
                withCredentials([file(credentialsId: 'frontend_env', variable: 'env')]) {
                    script {
                    sh 'chmod 755 $env'
                    sh 'cp -f -R $env front/.env'
                    }
                }
            }
        }
        stage('Build stage') {
            steps {
                dir('front') {
                    echo 'Docker build...'
                    sh 'docker build -f ../docker/front-docker -t frontend .'
                }
            }
            post {
                always {
                    sh 'docker image prune -f'
                }
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
```groovy
pipeline {
    agent any

    stages {
        stage('Remove docker') {
            steps {
                echo 'Remove Docker Process and Image'
                sh '''if [ "$(docker ps -a -q -f name=frontend)" ]; then
                    docker stop frontend
                    if [ "$(docker ps -aq -f status=exited -f name=frontend)" ]; then
                    docker rm frontend
                    fi
                docker rmi frontend
                fi'''
            }
        }
        stage('Copy env') {
            steps {
                withCredentials([file(credentialsId: 'frontend_env', variable: 'env')]) {
                    script {
                        sh 'chmod 755 $env'
                        sh 'cp -f -R $env front/.env'
                    }
                }
            }
        }
        stage('Build image') {
            steps{
                dir('front') {
                    echo 'Docker build...'
                    sh 'docker build -f ../docker/front-docker -t frontend .'
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
        stage('Docker Run') {
            steps{
                echo 'Docker run...'
                sh 'docker run -d --name frontend frontend'
            }
            post {
                success {
                    echo "Docker run success"
                    updateGitlabCommitStatus name: 'run', state: 'success'
                }
                failure {
                    echo "Docker run failed"
                    updateGitlabCommitStatus name: 'run', state: 'failed'
                }
            }
        }
        stage('Docker copy') {
            steps {
                echo 'Docker copy...'
                sh 'docker cp frontend:/front/dist/. /build-result'
            }
            post {
                success {
                    echo "Docker copy success"
                    updateGitlabCommitStatus name: 'copy', state: 'success'
                }
                failure {
                    echo "Docker copy failed"
                    updateGitlabCommitStatus name: 'copy', state: 'failed'
                }
            }
        }
        stage('Nginx reload') {
            steps {
                sh 'docker exec nginx nginx -s reload'
            }
        }
    }
}
```