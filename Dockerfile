FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml ./
COPY src ./src

#Optional - If you have a custom Maven settings.xml file, copy it to the container
#COPY settings.xml /root/.m2/settings.xml

# Build the Spring Boot application using Maven
#RUN mvn clean package

COPY target/ipm.jar .

# Run the application
# Extract the JAR file location from the target directory (update with your actual JAR file name)
CMD ["java", "-jar", "ipm.jar"]
