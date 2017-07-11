FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk curl
RUN curl -sL https://deb.nodesource.com/setup_6.x | bash -
RUN apt-get install -y nodejs

RUN npm install -g @angular/cli

WORKDIR /app

CMD ["bash"]