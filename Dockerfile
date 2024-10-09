FROM maven:3.8.5-openjdk-18

WORKDIR /app

COPY . .

RUN mvn clean install

CMD mvn spring-boot:run
#CMD ["java", "-jar", "target/movie-api-0.0.1-SNAPSHOT.jar"]
