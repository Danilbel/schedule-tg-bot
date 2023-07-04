FROM openjdk:17-ea-17-slim
COPY build/libs/schedule-tg-bot-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]