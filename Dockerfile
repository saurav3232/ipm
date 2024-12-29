FROM maven:3.9-eclipse-temurin-17

WORKDIR /app


COPY pom.xml ./
COPY src ./src

#COPY settings.xml /root/.m2/settings.xml

RUN mvn clean package

#COPY target/ipm.jar .

#CMD ["java", "-jar", "ipm.jar"]
CMD ["java", "-jar", "target/ipm.jar"]
