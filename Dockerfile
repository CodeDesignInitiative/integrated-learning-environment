FROM clojure:temurin-17-lein-bullseye-slim AS build-jar

WORKDIR /user/src
COPY . .

RUN lein uberjar

COPY /user/src/target/uberjar/ile.jar /ile/app.jar

CMD ["java", "-Xmx400m", "-jar", "/ile/app.jar"]

# Rename `release.jar` to your uberjar name
#COPY --from=build-jar /usr/src/target/uberjar/ile.jar .
#COPY /usr/src/target/uberjar/ile.jar .
#CMD ["java", "-jar", "ile.jar"]
EXPOSE 3000