# Testing Guide

## Requirements

To run the project's tests, Java JDK 17 and Maven must be installed. Docker Desktop is also required for integration tests, as these connect to the MySQL World database running on port 3306.

## Running Tests

### Unit Tests

Unit tests can be run from the project root using:

```bash
mvn test
```

This runs the unit tests without requiring a database connection.

### Integration Tests

Integration tests require the MySQL World database to be running. Start the database using:

```bash
docker compose up -d --wait mysql
```

Once the database is ready, run:

```bash
mvn verify
```

This executes both the unit and integration tests.

After testing, the database containers can be stopped using:

```bash
docker compose down
```

## Test Organisation

The project uses JUnit 5 for testing. Tests are located in `src/test/java/com/napier/sem/` and follow two naming conventions:

- **Unit tests (`*Test.java`):** Test individual methods without requiring a database connection. These are run using Maven Surefire.
- **Integration tests (`*IT.java`):** Test application functionality against the MySQL World database. These are run using Maven Failsafe.

## Adding New Tests

### Unit Tests

To add a unit test, create a Java class ending in `Test`, such as `MenuTest.java`, in the test directory.

Use the `@Test` annotation to identify test methods and JUnit assertions such as `assertEquals()` to check expected results. Unit tests should not require a database connection.

Run the tests using `mvn test`.

### Integration Tests

To add an integration test, create a Java class ending in `IT`, such as `CountryReportIT.java`.

The class should extend `IntegrationTestBase`, which provides a shared database connection through the `app` variable.

`IntegrationTestBase` connects to MySQL before the tests run using `@BeforeAll` and disconnects afterward using `@AfterAll`. This avoids duplicating database connection code.

Start MySQL and run `mvn verify` to execute the integration tests.

## Coverage and Reports

The project uses JaCoCo to generate code coverage reports when running `mvn verify`.

After running the tests, reports can be found at:

- **Unit test results:** `target/surefire-reports/`
- **Integration test results:** `target/failsafe-reports/`
- **HTML coverage report:** `target/site/jacoco/index.html`
- **XML coverage report:** `target/site/jacoco/jacoco.xml`

The HTML report can be opened in a browser to inspect which parts of the application are covered by tests. Coverage results are also uploaded to Codecov through GitHub Actions.

## GitHub Actions

The project's GitHub Actions workflow runs automatically when code is pushed. It contains three jobs:

1. **Unit tests:** Uses Java 17 and runs `mvn -B test` without a database. Test results are uploaded as artifacts.
2. **Integration tests:** Starts the MySQL World database, waits for the data to load, and runs `mvn -B verify`. It also uploads test results and JaCoCo coverage reports before stopping the database.
3. **Build:** Runs only when both testing jobs succeed. It packages the application into a JAR and runs it using Docker Compose.

These automated checks help identify problems before changes are merged into `develop`.

## Troubleshooting

If integration tests fail because they cannot connect to MySQL, check that Docker Desktop is running and start the database using `docker compose up -d --wait mysql`.

The tests connect to `localhost` by default. If MySQL is running on another host, configure the `DB_HOST` environment variable.

If a test fails, check the Maven output and the reports in `target/surefire-reports/` or `target/failsafe-reports/` for details.