FROM maven:3.9-eclipse-temurin-17-alpine

WORKDIR /app

#COPY settings.xml /root/.m2/settings.xml

#RUN mvn clean package

RUN apk add osv-scanner

COPY target/ipm.jar .

CMD ["java", "-jar", "ipm.jar"]
#CMD ["java", "-jar", "target/ipm.jar"]
