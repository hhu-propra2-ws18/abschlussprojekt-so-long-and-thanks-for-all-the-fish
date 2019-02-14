FROM openjdk:8-jdk-alpine AS build
WORKDIR /DatabaseWorkDir
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootJar

FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
WORKDIR /DatabaseWorkDir

COPY ./wait-for-it.sh .

COPY --from=build /DatabaseWorkDir/build/libs/*.jar app.jar

CMD java -jar app.jar
