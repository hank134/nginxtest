pipeline {

    agent any
    options {
        timestamps()
        timeout(time: 1, unit: 'HOURS')
        buildDiscarder(logRotator(artifactDaysToKeepStr: '7', artifactNumToKeepStr: '10', daysToKeepStr: '7', numToKeepStr: '50'))
    }


    parameters {
        choice(name: 'BITBUCKET_HELM_PATH',
                choices: ['web1test', 'web2test'],
                description: 'Папки с хелмами'
        )
        string(name: 'BITBUCKET_REPOSITORY', defaultValue: 'helmtest', description: 'Репозиторий для сборки и публикации')
        string(name: 'BRANCH', defaultValue: 'main', description: 'Ветка в Bitbucket-репозитории')
        string(name: 'BITBUCKET_PROJECT', defaultValue: 'hank134', description: 'Проект в Bitbucket')
        string(name: 'BITBUCKET_URL', defaultValue: 'https://github.com', description: 'URL Bitbucket')
        //     string(name: 'DOCKER_IMAGE_PATH', defaultValue: 'cicd_test_freestyle', description: 'Папка, куда будем выкладывать образ')
        // string(name: 'DOCKER_REGISTRY_HOST', defaultValue: '127.0.0.1:8083', description: 'host:port docker-репозитория, для ДЗО порт указывать не нужно')
        string(name: 'TUZ_USER_PASS', defaultValue: 'bc9247ef-4077-4a67-8e96-eaeb4136b0e9', description: 'ТУЗ user/pass для доступа в Bitbucket и Nexus')
    }

    environment {
        BUILD_DISPLAY_NAME = "${env.BITBUCKET_PROJECT}/${env.BITBUCKET_REPOSITORY}-${env.BUILD_NUMBER}"
        // DOCKER_IMAGE_TAG = "${env.BITBUCKET_REPOSITORY}:${env.BUILD_NUMBER}"
        // DOCKER_IMAGE_NAME = "${env.DOCKER_REGISTRY_HOST}/${env.DOCKER_IMAGE_PATH}/${env.DOCKER_IMAGE_TAG}"
        MY_KUBECONFIG = credentials('kubeconfigcloud')
    }

    stages {

        stage('Prepare') {
            steps {

                script {
                    script {
                        currentBuild.displayName = env.BUILD_DISPLAY_NAME

                        echo ''
                        echo '****************************************'
                        echo '* ENVIRONMENT VARIABLES BEGIN'
                        echo '****************************************'
                        echo ''
                        echo sh(script: 'env|sort', returnStdout: true)
                        echo ''
                        echo '****************************************'
                        echo '* ENVIRONMENT VARIABLES END'
                        echo '****************************************'
                        echo ''

                    }
                    cleanWs()
                }
            }
        }
        
        stage('Checkout project') {
            steps {
                checkout([$class                           : 'GitSCM',
                          branches                         : [[name: "main"]],
                          doGenerateSubmoduleConfigurations: false,
                          extensions                       : [],
                          submoduleCfg                     : [],
                          userRemoteConfigs                : [[credentialsId: "${env.TUZ_USER_PASS}", url: "${env.BITBUCKET_URL}/${env.BITBUCKET_PROJECT}/${env.BITBUCKET_REPOSITORY}.git"]]]
                )
            }

        }

        stage('Build project') {
            steps {
                script {
                    echo ''
                    echo '------HELLO-------'
                    echo ''
                    sh "cat $MY_KUBECONFIG"
                
                }
            }
        }
    }

    post {
        always {
            sh "ls -lha"
            echo '!!!!!!!!!!!!!!!BY!!!!!!!!!!!!!'
        }
    }
}
