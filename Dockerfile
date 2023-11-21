FROM clojure:temurin-17-lein-bullseye-slim AS builder

WORKDIR /user/src
COPY . .

RUN lein uberjar

FROM azul/zulu-openjdk:17

WORKDIR /root/

COPY --from=builder /user/src/target/uberjar/ile.jar /ile/app.jar

#CMD ["mkdir", "/asset-store"]

CMD ["java", "-Xmx400m", "-jar", "/ile/app.jar"]

EXPOSE 3000