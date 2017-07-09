FROM openjdk:8-jre

COPY build/libs/tci-projects-*.jar /usr/app/tci-projects.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "tci-projects.jar"]