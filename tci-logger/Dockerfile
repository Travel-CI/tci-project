FROM openjdk:8-jre

COPY build/libs/tci-logger-*.jar /usr/app/tci-logger.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "tci-logger.jar"]