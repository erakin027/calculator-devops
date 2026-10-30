pipeline {
    agent any

    tools {
        // Names must match Manage Jenkins -> Tools
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        // Docker Hub repository (username/repo)
        IMAGE_REPO   = 'erakin027/calculator-devops'
        IMAGE_TAG    = "${env.BUILD_NUMBER}"
        // Jenkins credentials ID (Username with password) holding Docker Hub login
        DOCKER_CREDS = 'dockerhub-creds'
        HOST_PORT    = '8081'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build -t ${IMAGE_REPO}:${IMAGE_TAG} -t ${IMAGE_REPO}:latest .
                '''
            }
        }

        stage('Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDS}",
                                                  usernameVariable: 'DH_USER',
                                                  passwordVariable: 'DH_PASS')]) {
                    sh '''
                        echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
                        docker push ${IMAGE_REPO}:${IMAGE_TAG}
                        docker push ${IMAGE_REPO}:latest
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker compose up -d --remove-orphans

                    echo "Waiting for the stack to become healthy..."
                    for i in $(seq 1 30); do
                        if curl -fsS http://localhost:${HOST_PORT}/actuator/health | grep -q '"status":"UP"'; then
                            echo "Health check passed"
                            docker compose ps
                            exit 0
                        fi
                        sleep 5
                    done
                    echo "Health check FAILED"
                    docker compose ps
                    docker compose logs --tail=50
                    exit 1
                '''
            }
        }
    }
}
