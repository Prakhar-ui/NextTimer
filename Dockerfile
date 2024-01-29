# Use an OpenJDK base image
FROM openjdk:22-ea-21-jdk-slim-bullseye

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the local machine to the container
COPY target/*.jar app.jar

# Expose the port on which your Spring Boot application will run (if applicable)
EXPOSE 8080

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]