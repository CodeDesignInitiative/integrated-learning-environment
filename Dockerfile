FROM clojure:temurin-17-lein-bullseye-slim AS build-jar


#ARG COMMIT_HASH=willbereplaced
#ENV COMMIT_HASH=${COMMIT_HASH}

#COPY . /usr/src/app
WORKDIR /user/src
COPY . .
#CMD ["lein", "uberjar"]
RUN lein uberjar
RUN echo "$PWD"
RUN pwd && ls
RUN pwd && ls /user/src/target

COPY target/uberjar/ile.jar /ile/app.jar
#
#FROM azul/zulu-openjdk:17
#
CMD ["java", "-Xmx400m", "-jar", "/ile/app.jar"]
#
#EXPOSE 3000

#FROM azul/zulu-openjdk:17
#WORKDIR /usr/app

# Rename `release.jar` to your uberjar name
#COPY --from=build-jar /usr/src/target/uberjar/ile.jar .
#COPY /usr/src/target/uberjar/ile.jar .
#CMD ["java", "-jar", "ile.jar"]
EXPOSE 3000