FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/evalforge-ai-1.0.0.jar app.jar
EXPOSE 8089
USER 10001
ENTRYPOINT ["java","-jar","app.jar"]

