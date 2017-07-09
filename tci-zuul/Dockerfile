FROM openjdk:8-jre

COPY build/libs/tci-zuul-*.jar /usr/app/tci-zuul.jar

WORKDIR /usr/app/

EXPOSE 8001

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "tci-zuul.jar"]