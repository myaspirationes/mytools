pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                script{
                    print("hello script")
                }
            }
        }
        stage('checkout'){
            steps {
            echo 'checkout from github'
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'a4cc10b2-7df1-41a0-801b-6523bac5defb', url: 'https://github.com/myaspirationes/mytools.git']]])

            }
        }
        stage('build'){
            steps {
            echo 'Begin build~~~'
           sh 'mvn clean package '
            }
        }

    }
}
