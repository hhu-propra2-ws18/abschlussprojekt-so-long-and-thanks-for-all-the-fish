FROM openjdk:8-jdk-alpine AS build
WORKDIR /log
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootJar

FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
WORKDIR /log



COPY --from=build /log/build/libs/*.jar app.jar

CMD java -jar app.jar
