FROM openjdk:11-jdk
COPY /target/message-board-service-0.1.jar ./message-board-service.jar
CMD ["java", "-Dspring.profiles.active=test", "-jar", "message-board-service.jar"]