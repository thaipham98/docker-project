FROM maven:3.8.1-openjdk-17
WORKDIR /app
COPY CentralCoordinator/pom.xml .
RUN mvn dependency:go-offline
COPY CentralCoordinator/src src
CMD ["mvn", "spring-boot:run"]