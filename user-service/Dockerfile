FROM maven:3.8.5-openjdk-17 as builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:17
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080