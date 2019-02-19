FROM openjdk:8-jdk-alpine AS build
WORKDIR /test
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootJar

FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
WORKDIR /test



COPY --from=build /test/build/libs/*.jar app.jar

CMD java -jar app.jar
