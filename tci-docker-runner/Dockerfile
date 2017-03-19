FROM openjdk:8-jre

ENV DOCKER_REMOTE_API_PORT 4243

# Install Docker
RUN apt-get update && \
	apt-get install -y --no-install-recommends \
		apt-transport-https ca-certificates software-properties-common

RUN add-apt-repository "deb https://apt.dockerproject.org/repo/ debian-jessie main" && \
	curl -fsSL https://apt.dockerproject.org/gpg | apt-key add -

RUN apt-get update && \
	apt-get -y install docker-engine && \
	rm -rf /var/lib/apt/lists/*

RUN echo 'DOCKER_OPTS="-H tcp://0.0.0.0:$DOCKER_REMOTE_API_PORT -H unix:///var/run/docker.sock"' >> /etc/default/docker

COPY build/libs/tci-docker-runner-*.jar /usr/app/tci-docker-runner.jar

WORKDIR /usr/app/

EXPOSE 8080

CMD ['java', '-jar', 'tci-docker-runner.jar']