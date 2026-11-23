# Code Review 3 Evidence

Everything the CR3 marker needs, with a link to the proof for each item. All figures are from `develop` on 3 Nov 2026, released as [v0.3](https://github.com/Lancelcode/set08103-group4-coursework/releases/tag/v0.3).

## CR3 checklist

| CR3 item | Evidence |
|---|---|
| Suitable unit tests | **57 unit tests** in 9 `*Test` classes, no database needed (see [Unit tests](#unit-tests)) |
| Suitable integration tests | **93 integration tests** in 18 `*IT` classes against the MySQL World database. Every user story, US01.1 - US06.1, has integration tests (see [Integration tests](#integration-tests)) |
| Tests running on GitHub Actions | Three jobs on every push: Unit tests, Integration tests, then Build and run the app, which only runs if both pass. [Latest green run on `develop`](https://github.com/Lancelcode/set08103-group4-coursework/actions/runs/41602689645). Workflow: [`.github/workflows/main.yml`](../.github/workflows/main.yml) |
| Code coverage | **74.4% of lines** (JaCoCo, unit and integration tests). [Codecov report](https://app.codecov.io/gh/Lancelcode/set08103-group4-coursework) |
| Kanban board in use | [Group_4 board, Sprint 3 milestone](https://github.com/users/Lancelcode/projects/1/views/1?filterQuery=milestone%3A%22Sprint%203%20-%20CR3%20(testing)%22). Every issue has an owner, priority, size, sprint and status |
| Branch usage | `feature/*` → `develop` → `release` → `main`, every change through a reviewed pull request with passing checks. [Merged PRs in Sprint 3](https://github.com/Lancelcode/set08103-group4-coursework/pulls?q=is%3Apr+is%3Amerged+merged%3A%3E%3D2026-10-13) and the [v0.3 release](https://github.com/Lancelcode/set08103-group4-coursework/releases/tag/v0.3) |
| Sprint evidence | [Sprint 3 notes](sprints/sprint-3.md): goal, roles, planning, status checks, review, retrospective and contribution |
| Project requirements met | All 25 reports, 6 lookups and the language report, each proved by the tests in the [user story table](#user-stories-and-their-tests) |

## Unit tests

57 tests in 9 classes. Run with `mvn test` (no database).

| Class | Tests | What it covers |
|---|---|---|
| [`MenuTest`](../src/test/java/com/napier/sem/MenuTest.java) | 15 | Menu choices, exit, Top N prompt and validation, name prompts, duplicate city names |
| [`AppWithoutDatabaseTest`](../src/test/java/com/napier/sem/AppWithoutDatabaseTest.java) | 10 | Every report returns an empty list and prints a message when there is no connection |
| [`PopulationBreakdownTest`](../src/test/java/com/napier/sem/PopulationBreakdownTest.java) | 7 | City and non-city calculations, zero population, cities outnumbering the country |
| [`LanguageReportTest`](../src/test/java/com/napier/sem/LanguageReportTest.java) | 6 | Language report display: null, empty, missing rows, format |
| [`CountryReportTest`](../src/test/java/com/napier/sem/CountryReportTest.java) | 5 | Country report display: null, empty, null rows, `N/A` capital |
| [`PopulationBreakdownDisplayTest`](../src/test/java/com/napier/sem/PopulationBreakdownDisplayTest.java) | 5 | Breakdown display: null, empty, null rows, percentages, zero population |
| [`CityReportTest`](../src/test/java/com/napier/sem/CityReportTest.java) | 4 | City report display: null, empty, null rows, format |
| [`CapitalCityReportTest`](../src/test/java/com/napier/sem/CapitalCityReportTest.java) | 4 | Capital city report display: null, empty, null rows, format |
| [`PopulationTotalTest`](../src/test/java/com/napier/sem/PopulationTotalTest.java) | 1 | World population is held in a `long` |

## Integration tests

93 tests in 18 classes. Run with `docker compose up -d --wait mysql`, then `mvn verify`. Every class extends the shared [`IntegrationTestBase`](../src/test/java/com/napier/sem/IntegrationTestBase.java), which connects once per class.

| Class | Tests | User stories |
|---|---|---|
| [`PopulationTotalIT`](../src/test/java/com/napier/sem/PopulationTotalIT.java) | 15 | US05.1 - US05.6 |
| [`CountryReportIT`](../src/test/java/com/napier/sem/CountryReportIT.java) | 13 | US01.1 - US01.6 |
| [`PopulationBreakdownIT`](../src/test/java/com/napier/sem/PopulationBreakdownIT.java) | 12 | US04.1 - US04.3 |
| [`CapitalCityReportIT`](../src/test/java/com/napier/sem/CapitalCityReportIT.java) | 8 | US03.1, US03.4 - US03.6 |
| [`LanguageReportIT`](../src/test/java/com/napier/sem/LanguageReportIT.java) | 6 | US06.1 |
| [`CityReportIT`](../src/test/java/com/napier/sem/CityReportIT.java) | 3 | US02.1 |
| [`CitiesInContinentIT`](../src/test/java/com/napier/sem/CitiesInContinentIT.java) | 3 | US02.2 |
| [`CitiesInRegionIT`](../src/test/java/com/napier/sem/CitiesInRegionIT.java) | 3 | US02.3 |
| [`CitiesInCountryIT`](../src/test/java/com/napier/sem/CitiesInCountryIT.java) | 3 | US02.4 |
| [`CitiesInDistrictIT`](../src/test/java/com/napier/sem/CitiesInDistrictIT.java) | 3 | US02.5 |
| [`TopNCitiesWorldIT`](../src/test/java/com/napier/sem/TopNCitiesWorldIT.java) | 3 | US02.6 |
| [`TopNCitiesContinentIT`](../src/test/java/com/napier/sem/TopNCitiesContinentIT.java) | 3 | US02.7 |
| [`TopNCitiesRegionIT`](../src/test/java/com/napier/sem/TopNCitiesRegionIT.java) | 3 | US02.8 |
| [`TopNCitiesCountryIT`](../src/test/java/com/napier/sem/TopNCitiesCountryIT.java) | 3 | US02.9 |
| [`TopNCitiesDistrictIT`](../src/test/java/com/napier/sem/TopNCitiesDistrictIT.java) | 3 | US02.10 |
| [`CapitalCitiesInContinentIT`](../src/test/java/com/napier/sem/CapitalCitiesInContinentIT.java) | 3 | US03.2 |
| [`CapitalCitiesInRegionIT`](../src/test/java/com/napier/sem/CapitalCitiesInRegionIT.java) | 3 | US03.3 |
| [`TopNCapitalCitiesWorldIT`](../src/test/java/com/napier/sem/TopNCapitalCitiesWorldIT.java) | 3 | US03.4 |

## User stories and their tests

Each story's acceptance criteria give the exact expected values from the World database, and the tests check those values. Full stories: [`user-stories.md`](user-stories.md).

| Area | Stories | Tested by |
|---|---|---|
| Country reports | US01.1 - US01.6 | `CountryReportIT` |
| City reports | US02.1 - US02.10 | `CityReportIT`, `CitiesIn*IT`, `TopNCities*IT` |
| Capital city reports | US03.1 - US03.6 | `CapitalCityReportIT`, `CapitalCitiesIn*IT`, `TopNCapitalCitiesWorldIT` |
| City and non-city breakdown | US04.1 - US04.3 | `PopulationBreakdownIT`, `PopulationBreakdownTest` |
| Population lookups | US05.1 - US05.6 | `PopulationTotalIT`, `PopulationTotalTest` |
| Language statistics | US06.1 | `LanguageReportIT`, `LanguageReportTest` |

## Other Sprint 3 evidence

- [Testing guide](testing.md): how to run the tests and add new ones
- [UML class diagram](diagrams/class-diagram.drawio.png)
- [Use case diagram](diagrams/use-case-diagram.png) with the World database as a secondary actor (CR2 feedback)
- [Known data limitations](../README.md#known-data-limitations) in the README

## Running order (15 minutes)

| Time | Who | Shows |
|---|---|---|
| 0 - 3 min | Djiby | The Sprint 3 board, the sprint notes, a green GitHub Actions run with its three jobs, and coverage on Codecov |
| 3 - 6 min | Julia | Unit vs integration tests using `CountryReportIT` and `CountryReportTest`, and how a story's acceptance criteria become test values |
| 6 - 9 min | Kyle | `PopulationTotalIT` and `LanguageReportIT`, the Singapore rule (#118), the class diagram and the v0.3 release |
| 9 - 12 min | Hassan | The display tests that found and fixed a `NullPointerException` (#120), and this evidence page |
| 12 - 15 min | Everyone | Questions |

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
