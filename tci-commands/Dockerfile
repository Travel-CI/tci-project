FROM openjdk:8-jre

COPY build/libs/tci-commands-*.jar /usr/app/tci-commands.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ['java', '-jar', 'tci-commands.jar']