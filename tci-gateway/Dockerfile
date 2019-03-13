FROM openjdk:8-jre

COPY build/libs/tci-gateway-*.jar /usr/app/tci-gateway.jar

WORKDIR /usr/app/

EXPOSE 8001

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "tci-gateway.jar"]