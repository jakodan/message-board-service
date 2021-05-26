# Message Board Service
A Spring Boot WebMVC application that exposes a RESTFul API for creating, updating and reading messages.
<br>
Spring Security is utilized for authentication and authorization.

## Building and running the service
#### Prerequisites
- Maven (https://maven.apache.org/download.cgi)
- JDK 11 (https://jdk.java.net/11/)
- optional: Docker (if running with Docker) (https://www.docker.com)
- optional: Docker-compose (if running with Docker-compose) (https://docs.docker.com/compose/install/)

#### Building
1. from project root: `mvn clean install`

#### Running
##### With Docker
1. Build the image (from project root): `docker build -t message-board-service .`
2. Run the container: `docker run -d --name message-board-service -p 8080:8080 message-board-service`

##### With docker-compose
1. Build and run the container (from project root): `docker-compose up --build --detach`


#### Cleanup
##### Docker
1. `docker stop message-board-service`
2. `docker rm message-board-service`

##### Docker-compose
1. (from project root): `docker-compose down`

<br>

## Example usage

## Create a new message
