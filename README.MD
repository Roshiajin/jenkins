dockerfile:
    docker build --tag=wildfly-app
    docker run -it -p 8080:8080 wildfly-app
    docker build --tag=jboss/wildfly-admin
    docker run -it -p 9990:9990 jboss/wildfly-admin

docker-compose:
    docker-compose -f docker-compose.yml up -d
    docker ps
