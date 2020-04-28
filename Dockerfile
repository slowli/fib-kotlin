FROM maven:3.6.3-openjdk-14 AS builder
WORKDIR /service
COPY * /service/
COPY src/ /service/src/
RUN mvn clean package -Dmaven.test.skip

FROM openjdk:14-jdk-alpine
COPY --from=builder /service/target/fib-*.jar /usr/local/service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/service.jar"]
