# Code Review 3 Evidence

This page collects the main evidence for Code Review 3 so the reviewer can quickly find our testing, CI and Sprint 3 work.

## Unit Tests

Current unit test classes include:

- [CityReportTest.java](../src/test/java/com/napier/sem/CityReportTest.java)
- [CapitalCityReportTest.java](../src/test/java/com/napier/sem/CapitalCityReportTest.java)
- [CountryReportTest.java](../src/test/java/com/napier/sem/CountryReportTest.java)
- [PopulationBreakdownTest.java](../src/test/java/com/napier/sem/PopulationBreakdownTest.java)

These tests check report behaviour including normal input, empty input and error cases.

## Integration Tests

Current integration test classes include:

- [CountryReportIT.java](../src/test/java/com/napier/sem/CountryReportIT.java)
- [PopulationBreakdownIT.java](../src/test/java/com/napier/sem/PopulationBreakdownIT.java)
- [IntegrationTestBase.java](../src/test/java/com/napier/sem/IntegrationTestBase.java)

The integration tests use the MySQL World database.

## Tests on GitHub Actions

GitHub Actions runs the project tests automatically.

The workflow includes:

- Unit tests
- Integration tests
- Build and run the application

[View GitHub Actions](https://github.com/Lancelcode/set08103-group4-coursework/actions)

## Test Coverage

Test coverage is measured as part of Sprint 3.

Coverage evidence can be checked through the repository coverage work and Codecov once the latest coverage changes are merged.

## Kanban / Project Board

Sprint 3 work is managed using the Group 4 GitHub Project board.

[View Group 4 Project Board](https://github.com/users/Lancelcode/projects/1)

The team uses the following workflow:

Backlog → Ready → In progress → In review → Done

## Branch and Pull Request Workflow

The team uses:

`feature/* → develop → release → main`

Work is completed on feature branches and then added to `develop` through Pull Requests and teammate reviews.

[View Pull Requests](https://github.com/Lancelcode/set08103-group4-coursework/pulls)

## Releases

The repository uses releases to show stable versions of the project.

[View Releases](https://github.com/Lancelcode/set08103-group4-coursework/releases)

## Sprint 3 Evidence

Sprint 3 mainly focuses on:

- Unit testing
- Integration testing
- GitHub Actions
- Test coverage
- Code quality
- Team reviews

Sprint notes can be found here:

[sprint-3.md](sprints/sprint-3.md)

## Suggested CR3 Review Order

During the Code Review, we can show the evidence in this order:

1. Sprint 3 GitHub Project board
2. Unit tests
3. Integration tests
4. Successful GitHub Actions checks
5. Test coverage
6. Feature branches and Pull Requests
7. Sprint 3 notes
8. Completed project requirements

## My CR3 Contribution

For CR3, I worked on unit testing for the city and capital city display methods.

Issue #120 added tests for:

- null lists
- empty lists
- null entries inside lists
- normal city output
- normal capital city output

The tests identified a `NullPointerException` problem in the display methods.

The methods were updated so null input and null entries are handled safely.

The work was completed using:

- Issue #120
- A feature branch
- Unit tests
- GitHub Actions checks
- Pull Request #172
- Teammate review
- The GitHub Project board
