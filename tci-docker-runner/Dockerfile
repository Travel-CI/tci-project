FROM openjdk:8-jre

COPY build/libs/tci-docker-runner-*.jar /usr/app/tci-docker-runner.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ['java', '-jar', 'tci-docker-runner.jar']