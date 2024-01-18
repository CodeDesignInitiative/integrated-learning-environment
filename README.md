# Integrated Learning Environment

An online learning platform for HTML, CSS and JavaScript in a playful way.

Visit http://play.code.design for the live application.

## Prerequisites

1. JDK 11+
2. Clojure
3. Leiningen
4. Docker

## Starting the app

1. Run `lein repl`
2. Run `(start)` inside of REPL

## Docker

1. Run `docker build -t lms .` to create the latest image
2. Run `docker-compose up -d` to launch the app with postgres