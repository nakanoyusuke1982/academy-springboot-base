FROM eclipse-temurin:17

WORKDIR /app
COPY . .

RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test

CMD ["java", "-jar", "build/libs/spring-0.0.1-SNAPSHOT.jar"]