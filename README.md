# SET08103 Group 4 - Population Reporting System
![workflow](https://github.com/Lancelcode/set08103-group4-coursework/actions/workflows/main.yml/badge.svg?branch=develop)

A Java console app that produces population reports from the MySQL `world` database: countries, cities, capital cities, population totals and language statistics (31 reports, chosen from a menu).

## Run it with Docker (easiest)
~~~
mvn -B package
docker compose up --build
~~~
This starts MySQL with the World database and runs every report once.

## Run it locally with the menu
~~~
mvn -B package
docker compose up -d mysql
java -jar target/set08103-group4-jar-with-dependencies.jar
~~~
Pick a report number, enter a continent/region/country/district or N when asked, and `0` to exit.

## Project docs
- [How we work](docs/CONTRIBUTING.md) and [Definition of Done](docs/DEFINITION_OF_DONE.md)
- [Use cases](docs/use-cases/README.md) and [diagrams](docs/diagrams/)
- [Sprint notes](docs/sprints/)
- [Code of Conduct](CODE_OF_CONDUCT.md)

## Releases
See [Releases](https://github.com/Lancelcode/set08103-group4-coursework/releases) for the latest JAR.
