# Message Board Service
A Spring Boot WebMVC application that exposes a RESTFul API for creating, updating and reading messages.
<br>
Spring Security is utilized for authentication and authorization.

## Building and running the service
#### Prerequisites
- Maven (https://maven.apache.org/download.cgi)
- JDK + JRE 11 (https://jdk.java.net/11/)
- optional: Docker (if running with Docker) (https://www.docker.com)
- optional: Docker-compose (if running with Docker-compose) (https://docs.docker.com/compose/install/)

#### Building
1. from project root: `mvn clean install`

#### Running
Running the service with this guide will run the service in "test" mode which means the secret for JWT generation will be fetched from a configuration file and two test users, `Bob` and `Alice`, with passwords `Bob` and `Alice` will be created for testing convenience.<br>
This is of course not recommended for production use.

##### With JRE
1. Run the jar (from project root): `java -Dspring.profiles.active=test -jar target/message-board-service-0.1.jar`

##### With Docker
1. Build the image (from project root): `docker build -t message-board-service .`
2. Run the container: `docker run -d --name message-board-service -p 8080:8080 message-board-service`

##### With docker-compose
1. Build and run the container (from project root): `docker-compose up --build --detach`


#### Cleanup

##### JRE
1. Ctrl-C in the terminal window you started the application in

##### Docker
1. `docker stop message-board-service`
2. `docker rm message-board-service`

##### Docker-compose
1. (from project root): `docker-compose down`

<br>

## Example usage
Example curl commands to use the service once it's up and running

#### 1. Authenticate and generate JWT
To create/post a new message to the service the client needs to authenticate itself.<br>
This is done by making a POST request to the `/auth/token` endpoint with the users username and password.<br>
If the authentication is successful the endpoint will respond with a JWT token that should be used in the `Authorization` header as `Bearer` token in requests towards the following endpoints: <br>
`POST /messages` - creates a new message<br>
`PUT /messages/{message-id}` - updates the message with the given id<br>
`DELETE /messages/{message-id}` - deletes the message with the given id<br><br>
Run this curl command to make an authentication request and generate a JWT for user `Bob`:
```
curl -d '{"username":"Bob", "password":"Bob"}' \
   -H "Content-Type: application/json" \
   -X POST \
   http://localhost:8080/auth/token
```

This will respond with the following body
```
200 OK

{"jwt":"somevalue"}
```

In the following requests we will put the JWT from the response in the `Authorization` header.

#### 2. Create / post a new message
Run this curl command to create a new message. Note that you need to replace the Bearer token with the JWT from the response of the Authentication request.
```
curl -d '{"text":"This is a new message"}' \
   -H "Content-Type: application/json" \
   -H "Authorization: Bearer jwtFromAuthenticationResponse" \
   -X POST \
   http://localhost:8080/messages
```
Response: 
```
201 CREATED

{"text":"This is a new message","id":"1273847522","author":"Bob"}
```

#### 3. Update a message
Take the id from the create message request and use it to update the message.
Note that updating a message can only be done by the author of the message.
```
curl -d '{"text":"This is an updated message"}' \
   -H "Content-Type: application/json" \
   -H "Authorization: Bearer jwtFromAuthenticationResponse" \
   -X PUT \
   http://localhost:8080/messages/1273847522
```
Response:
```
200 OK
```

#### 4. Get all messages
Note that no authentication is required for this request.
```
curl http://localhost:8080/messages
```
Response:
```
200 OK

{
  "messages":[
    {
      "text": "This is an updated message",
      "id": "1273847522",
      "author": "Bob"
    }
  ]
}
```

#### 5. Delete a message
Note that deleting a message can only be done by the author of the message.
```
curl -H "Authorization: Bearer jwtFromAuthenticationResponse" \
   -X DELETE \
   http://localhost:8080/messages/1273847522
```
Response:
```
200 OK
```

#### Verifying authorization
To verify the authorization of UPDATE and DELETE'ing a message, first authenticate as user `Bob` and create a message, 
then authenticate as user `Alice` and try to delete or update Bobs message. The service will return a `401 UNAUTHORIZED` response. <br>
This is of course also tested and verified in unit tests for the application.
