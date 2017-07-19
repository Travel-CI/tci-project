<p align="center">
  <img src="https://github.com/Travel-CI/tci-project/blob/dev/travelci/logo.png"/>
</p>

[![Travis build](https://img.shields.io/travis/Travel-CI/tci-project.svg)](https://travis-ci.org/Travel-CI/tci-project)


Travel CI is a microservices solution for continuous integration.
With Travel CI, you can easily build any project whatever the language used.
You can find the full documentation [here](https://github.com/Travel-CI/tci-project/blob/dev/travelci/Documentation.pdf)

## Installation

### Requirements

Install Docker CE, PostgreSQL, and update config

```bash
$ sudo nano /lib/systemd/system/docker.service
$ Update line : ExecStart=/usr/bin/dockerd -H fd:// -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
```

### From sources

```bash
$ ./gradlew clean build
$ java -jar tci-**/build/libs/tci-**.jar (start each microservice)
$ cd tci-webapp
$ npm install
$ npm start
```

### From Docker

Or alternatively using Docker Compose:

```bash
$ docker-compose up -d
```

## Microservices Architecture

Context :
<p align="center">
  <img src="https://github.com/Travel-CI/tci-project/blob/dev/travelci/C1-diagram.jpg"/>
</p>
Travel CI Front-End & Back-End :
<p align="center">
  <img src="https://github.com/Travel-CI/tci-project/blob/dev/travelci/C2-diagram.jpg"/>
</p>
Spring Cloud Architecture :
<p align="center">
  <img src="https://github.com/Travel-CI/tci-project/blob/dev/travelci/C2-engine-diagram.jpg"/>
</p>

## Contributors

 - [Julien Bertault](https://github.com/juliiien)
 - [Jérémy Boehm](https://github.com/jeremyboehm)
 - [Mathieu Boisnard](https://github.com/mboisnard)
