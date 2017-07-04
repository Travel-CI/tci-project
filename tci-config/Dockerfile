FROM openjdk:8-jre

COPY build/libs/tci-config-*.jar /usr/app/tci-config.jar

WORKDIR /usr/app/

EXPOSE 8888

CMD ['java', '-jar', 'tci-config.jar']