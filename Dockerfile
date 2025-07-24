# Use a lightweight OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the packaged jar file
COPY target/S05T01N01-0.0.1-SNAPSHOT.jar app.jar

# Expose the app's port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
