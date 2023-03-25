FROM clojure:temurin-17-lein-bullseye-slim AS builder

WORKDIR /user/src
COPY . .
#COPY . /user/src

RUN lein uberjar

FROM azul/zulu-openjdk:17

WORKDIR /root/

COPY --from=builder /user/src/target/uberjar/ile.jar /ile/app.jar

CMD ["java", "-Xmx400m", "-jar", "/ile/app.jar"]

# Rename `release.jar` to your uberjar name
#COPY --from=build-jar /usr/src/target/uberjar/ile.jar .
#COPY /usr/src/target/uberjar/ile.jar .
#CMD ["java", "-jar", "ile.jar"]
EXPOSE 3000