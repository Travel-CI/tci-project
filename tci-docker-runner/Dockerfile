FROM openjdk:8-jre

# Install Docker
RUN apt-get update && \
	apt-get install -y --no-install-recommends \
		apt-transport-https ca-certificates software-properties-common

RUN add-apt-repository "deb https://apt.dockerproject.org/repo/ debian-jessie main" && \
	curl -fsSL https://apt.dockerproject.org/gpg | apt-key add -

RUN apt-get update && \
	apt-get -y install docker-engine && \
	rm -rf /var/lib/apt/lists/*

RUN service docker stop

RUN sed -i "s/ExecStart=\/usr\/bin\/dockerd -H fd:\/\//ExecStart=\/usr\/bin\/dockerd -H fd:\/\/ -H tcp:\/\/0.0.0.0:2375 -H unix:\/\/\/var\/run\/docker.sock/g"  /lib/systemd/system/docker.service

RUN service docker start

COPY build/libs/tci-docker-runner-*.jar /usr/app/tci-docker-runner.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "tci-docker-runner.jar"]