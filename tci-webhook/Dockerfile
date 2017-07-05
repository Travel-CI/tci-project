FROM openjdk:8-jre

COPY build/libs/tci-webhook-*.jar /usr/app/tci-webhook.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ["java", "-jar", "tci-webhook.jar"]