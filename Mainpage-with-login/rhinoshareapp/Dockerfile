FROM openjdk:8-jdk-alpine AS build
WORKDIR /rhinoshare
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootJar

FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
WORKDIR /rhinoshare

COPY ./wait-for-it.sh .

COPY --from=build /rhinoshare/build/libs/*.jar app.jar

CMD java -jar app.jar
