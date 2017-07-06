FROM openjdk:8-jre

COPY build/libs/tci-eureka-*.jar /usr/app/tci-eureka.jar

WORKDIR /usr/app/

EXPOSE 8000

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "tci-eureka.jar"]