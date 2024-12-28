FROM maven:3.9-amazoncorretto-17-debian-bookworm

WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml ./
COPY src ./src

# Build the Spring Boot application using Maven
RUN mvn clean package

# Run the application
# Extract the JAR file location from the target directory (update with your actual JAR file name)
CMD ["java", "-jar", "target/ipm.jar"]
