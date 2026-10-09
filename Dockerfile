FROM amazoncorretto:17
COPY ./target/set08103-group4-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "set08103-group4-jar-with-dependencies.jar"]
