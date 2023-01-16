pipeline { 
    agent any 
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') { 
            steps { 
                echo '---------Build 1' 
            }
        }
        stage('Test'){
            steps {
                echo 'Testing'
                sh 'ls -lha'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker version'
            }
        }
    }
}
