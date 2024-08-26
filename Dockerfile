FROM openjdk:17-jdk

WORKDIR /app

COPY target/financial-transactions-service-0.0.1-SNAPSHOT.jar /app/financial-transactions-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "financial-transactions-service-0.0.1-SNAPSHOT.jar"]
