#FROM azul/zulu-openjdk:17
FROM clojure:lein


#ARG COMMIT_HASH=willbereplaced
#ENV COMMIT_HASH=${COMMIT_HASH}

#COPY . /usr/src/app
WORKDIR /ile
COPY . .
CMD ["lein", "uberjar"]

COPY target/uberjar/ile.jar /ile/app.jar

CMD ["java", "-Xmx400m", "-jar", "/ile/app.jar"]

EXPOSE 3000