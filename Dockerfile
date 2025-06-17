FROM bellsoft/liberica-openjdk-alpine:17 as build
WORKDIR /deploy

COPY qc-api/qc-api-container/build/libs/*.jar .

ENTRYPOINT ["sh", "-c", "java -jar $(ls /deploy/*.jar)"]