stage('Build') {
    node {
        git url: 'https://github.com/Roshiajin/jenkins.git'
        env.PATH = "${tool 'maven350'}/bin:${env.PATH}"
        sh 'mvn clean package'
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }
}

stage('deployToTomcat') {
    node {
        sh 'curl -v -u admin:tomcat -T "./target/webservice-1.0.war" "http://localhost:8181/manager/text/deploy?path=/webservice-1.0&update=true"'
        //sh 'sleep 20'
    }
}