FROM openjdk:8-jre

COPY build/libs/tci-scheduler-*.jar /usr/app/tci-scheduler.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "tci-scheduler.jar"]