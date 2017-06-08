stage('Build') {
    node {
        git url: 'https://github.com/PavelShchetska/boxfuse-sample-java-war-hello.git'
        env.PATH = "${tool 'MAVEN3.3.9'}/bin:${env.PATH}"
        sh 'mvn clean package'
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }
}

stage('deployToWildfly') {
    node {
        sh 'curl -v -u admin:tomcat -T "./target/hello-1.0.war" "http://192.168.56.2:81/manager/text/deploy?path=/hello-1.0&update=true"'
        //sh 'sleep 20'
    }
}

}