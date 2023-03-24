FROM azul/zulu-openjdk:17
FROM clojure:lein

EXPOSE 3000

#ARG COMMIT_HASH=willbereplaced
#ENV COMMIT_HASH=${COMMIT_HASH}

COPY . /usr/src/app
WORKDIR /usr/src/app
CMD ["lein", "run"]

COPY target/uberjar/lms.jar /lms/app.jar

CMD ["java", "-Xmx400m", "-jar", "/lms/app.jar"]
