FROM openjdk:8-jdk-alpine AS build
WORKDIR /DatabaseTestWorkDir
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootJar

FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
WORKDIR /DatabaseTestWorkDir

COPY --from=build /DatabaseTestWorkDir/build/libs/*.jar app.jar

CMD ./wait-for-it.sh initializer:8080 --timeout=15 -- java -jar app.jar
