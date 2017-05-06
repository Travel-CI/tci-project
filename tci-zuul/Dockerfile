FROM openjdk:8-jre

COPY build/libs/tci-zuul-*.jar /usr/app/tci-zuul.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ['java', '-jar', 'tci-zuul.jar']