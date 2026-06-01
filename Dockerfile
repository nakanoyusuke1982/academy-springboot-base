FROM eclipse-temurin:17

WORKDIR /app
COPY . .

RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew
RUN ./gradlew build -x test

CMD ["./gradlew", "bootRun"]