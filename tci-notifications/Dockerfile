FROM openjdk:8-jre

COPY build/libs/tci-notifications-*.jar /usr/app/tci-notifications.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "tci-notifications.jar"]