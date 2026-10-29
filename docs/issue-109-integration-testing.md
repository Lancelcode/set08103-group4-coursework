# Population Breakdown Integration Tests — Issue #109



## Purpose

Check the application's continent, region and country reports against the MySQL World database.



Expected totals are calculated in Java from separate reads of the country and city tables. This checks the report queries independently without changing database records.



## Requirements

- JDK 17 and Maven

- Docker Desktop running

- MySQL World database available on port 3306

- Local coursework database credentials: root / root



Start the database from the project folder:



```powershell

docker compose up -d mysql

```



Wait until the database container is healthy:



```powershell

docker compose ps

```



The supplied World dataset used for this run contains 239 countries and 4079 cities.



## Run the tests

From the project folder:



```powershell

mvn verify

```



In IntelliJ, use Execute Maven Goal and enter `mvn verify`.



The tests use localhost by default. Set DB_HOST if the database runs on another host. The application currently uses port 3306 and the database name world.



## Test coverage

1. Country totals match independently calculated values.

2. Continent aggregation counts each country once.

3. Region aggregation counts each country once.

4. Countries without recorded cities remain in the report.

5. Zero-population countries return zero percentages.

6. All three reports use descending total-population order.



## Unit tests and integration tests

- Surefire runs unit tests named *Test during mvn test.

- Failsafe runs integration tests named *IT during mvn verify.

- Unit tests do not require MySQL.

- Integration tests require MySQL and fail when the database is unavailable.

- This branch includes the JUnit setup from #107. Its six unit tests are in PR #108.



## Verified local result

On 16 October 2026:



- Command: mvn verify

- Test class: PopulationBreakdownIT

- Tests run: 6

- Failures: 0

- Errors: 0

- Skipped: 0

- Build: SUCCESS



Reports are written to target/failsafe-reports. Generated target files should not be committed.



## Limits

These tests require a populated World dataset containing countries with no cities and zero population. They do not yet verify every application report or simulate a database failure during a query.



GitHub Actions execution of these integration tests has not yet been verified and is tracked separately.

