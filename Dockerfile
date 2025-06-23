FROM bellsoft/liberica-openjdk-alpine:17 as build
WORKDIR /deploy

COPY /qc-api-container/build/libs/questioncloud-*-SNAPSHOT.jar app.jar

ENTRYPOINT ["sh", "-c", "java -jar $(ls /deploy/*.jar)"]