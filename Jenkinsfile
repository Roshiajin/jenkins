stage('Build') {
    node {
        git url: 'https://github.com/Roshiajin/jenkins.git'
        env.PATH = "${tool 'MAVEN3.3.9'}/bin:${env.PATH}"
        sh 'mvn clean package'
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }
}

stage('deployToWildfly') {
    node {
        sh 'curl -v -u admin:tomcat -T "./target/webservice-1.0.war" "http://localhost:8080/manager/text/deploy?path=/webservice-1.0&update=true"'
        //sh 'sleep 20'
    }
}

}