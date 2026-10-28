# Sprint 3 - CR3 (testing)

**Dates:** 13 Oct - 1 Nov 2026
**Milestone:** Sprint 3 - CR3 (testing)
**Code Review 3:** lab of Week 9

## Sprint goal
Prove every report with unit and integration tests that run in GitHub Actions, measure test coverage, and act on both pieces of CR2 feedback: replace the vague user stories with specific, testable ones, and show the database as a secondary actor in the use case diagram.

## CR2 feedback and what we changed
| Feedback (Andreas, CR2) | What we did |
|---|---|
| User stories were too vague | The six grouped "As a user" stories (#13-#18) became epics and were split into 32 specific stories (#130-#161), one per report or lookup in the brief. Each names a real role, has Given / When / Then criteria with the correct answer from the World database, and lists the tests that prove it (#125). |
| The database was missing from the use case diagram | The MySQL World database is added as a secondary actor on the right of the system boundary, and named in UC01-UC06 (#124). |

## Roles
We each took a role on top of our own stories. We will rotate them in Sprint 4.

| Member | Role | What it means |
|---|---|---|
| Djiby (Lancelcode) | Scrum Master; CI and test setup lead | Runs stand-ups and these notes, keeps the board up to date, owns the work that would block everyone (shared test setup, CI test jobs, coverage), reviews PRs quickly |
| Julia (jkasiuk) | Product Owner | Checks each story's acceptance criteria are met before its issue is closed |
| Zain (zainali-uni) | Design and diagrams lead | Use case diagram fix, class diagram, testing guide |
| Kyle (Ky1eM05) | Release manager | Moves `develop` to `release` to `main` and publishes v0.3 |
| Hassan (Hasni243) | Review evidence lead | Builds the CR3 evidence page with links for the marker |

## Planning
Planned on 20 Oct 2026 and shared with the team in the Teams chat, with roles, owners, points and dates. Everyone was invited to change their role or challenge the split.

**How the work was split.** Sprint 2's GitHub numbers showed uneven contribution (Djiby 39 commits, Julia 19, Zain 11, Hassan 4, Kyle 3). Since each code review is marked separately, Sprint 3 aims for an even share:
- Djiby kept the critical, time-short work that would block everyone if late (#114, #115, #116).
- The work with the most commits in it (many small, independent tests) went to the members with fewer commits in Sprint 2.
- Everyone works atomically: one branch and one PR per issue, and one commit per test or fix.

| Member | Points |
|---|---|
| Djiby (Lancelcode) | 10 |
| Julia (jkasiuk) | 13 |
| Hassan (Hasni243) | 13 |
| Kyle (Ky1eM05) | 15 |
| Zain (zainali-uni) | 11 |

### Tasks
| Issue | Task | Owner | Points | Status |
|---|---|---|---|---|
| #114 | Shared integration test setup for all report tests | Djiby | 2 | Done (PR #163) |
| #115 | Run unit and integration tests in GitHub Actions | Djiby | 3 | Done (PR #164) |
| #116 | Measure test coverage with JaCoCo and Codecov | Djiby | 2 | In review |
| #117 | Unit tests for menu input: Top N and name prompts | Kyle | 3 | To do |
| #118 | Decide and test how breakdowns show countries whose cities outnumber the country | Zain | 2 | To do |
| #119 | Unit tests for displayCountries | Julia | 1 | To do |
| #120 | Unit tests for displayCities and displayCapitalCities | Hassan | 2 | To do |
| #121 | Unit tests for displayPopulationBreakdowns | Zain | 1 | To do |
| #122 | Unit tests for displayLanguageStatistics | Kyle | 1 | To do |
| #123 | UML class diagram of the application | Zain | 2 | To do |
| #124 | Database as a secondary actor in the use case diagram (CR2 feedback) | Zain | 1 | Done (PR #165) |
| #125 | Rewrite user stories with specific roles and testable criteria (CR2 feedback) | Djiby | 2 | In review |
| #126 | Testing guide: how to run and add tests | Zain | 2 | To do |
| #127 | Sprint 3 notes, roles, stand-ups, review, retrospective and contribution sheet | Djiby | 1 | In progress |
| #128 | Release v0.3 for Code Review 3 | Kyle | 1 | To do |
| #129 | CR3 evidence page | Hassan | 1 | To do |

### User stories
| Issue | Story | Owner | Points | Status |
|---|---|---|---|---|
| #130 | US01.1 All countries in the world | Julia | 1 | To do |
| #131 | US01.2 All countries in a continent | Julia | 1 | To do |
| #132 | US01.3 All countries in a region | Julia | 1 | To do |
| #133 | US01.4 Top N countries in the world | Julia | 1 | To do |
| #134 | US01.5 Top N countries in a continent | Julia | 1 | To do |
| #135 | US01.6 Top N countries in a region | Julia | 1 | To do |
| #136 | US02.1 All cities in the world | Hassan | 1 | To do |
| #137 | US02.2 All cities in a continent | Hassan | 1 | To do |
| #138 | US02.3 All cities in a region | Hassan | 1 | To do |
| #139 | US02.4 All cities in a country | Hassan | 1 | To do |
| #140 | US02.5 All cities in a district | Hassan | 1 | To do |
| #141 | US02.6 Top N cities in the world | Hassan | 1 | To do |
| #142 | US02.7 Top N cities in a continent | Hassan | 1 | To do |
| #143 | US02.8 Top N cities in a region | Hassan | 1 | To do |
| #144 | US02.9 Top N cities in a country | Hassan | 1 | To do |
| #145 | US02.10 Top N cities in a district | Hassan | 1 | To do |
| #146 | US03.1 All capital cities in the world | Julia | 1 | To do |
| #147 | US03.2 All capital cities in a continent | Julia | 1 | To do |
| #148 | US03.3 All capital cities in a region | Julia | 1 | To do |
| #149 | US03.4 Top N capital cities in the world | Julia | 1 | To do |
| #150 | US03.5 Top N capital cities in a continent | Julia | 1 | To do |
| #151 | US03.6 Top N capital cities in a region | Julia | 1 | To do |
| #152 | US04.1 City and non-city population for each continent | Zain | 1 | To do |
| #153 | US04.2 City and non-city population for each region | Zain | 1 | To do |
| #154 | US04.3 City and non-city population for each country | Zain | 1 | To do |
| #155 | US05.1 Population of the world | Kyle | 1 | To do |
| #156 | US05.2 Population of a continent | Kyle | 1 | To do |
| #157 | US05.3 Population of a region | Kyle | 1 | To do |
| #158 | US05.4 Population of a country | Kyle | 1 | To do |
| #159 | US05.5 Population of a district in a country | Kyle | 2 | To do |
| #160 | US05.6 Population of a city in a country | Kyle | 2 | To do |
| #161 | US06.1 Speakers of Chinese, English, Hindi, Spanish and Arabic | Kyle | 2 | To do |

### Key dates
| Date | What |
|---|---|
| Wed 21 Oct | Shared integration test setup merged (#114) |
| Week of 26 Oct | First stand-up and first-PR checkpoint (moved from Sun 25 Oct, see Stand-ups). Anyone who hasn't started gets support, and work is rebalanced |
| Thu 29 Oct | All PRs merged into `develop` |
| Fri 30 Oct | Release v0.3 (#128) |
| Sun 1 Nov | These notes, the contribution sheet and the CR3 evidence page finished |

## Acting on the Sprint 2 retrospective
| Sprint 2 action | How Sprint 3 does it |
|---|---|
| Hold a short planning meeting and record it | Planning above, shared in Teams with owners, points and dates |
| Weekly 10-minute stand-up with a dated note | Stand-ups recorded below |
| Pick up work from the board | Every issue is on the Group_4 board with an owner and steps to follow |
| Everyone reviews at least two PRs | Every PR now needs one approval before merging (ruleset on `main`, `develop` and `release`) |
| Keep PRs focused | One PR per issue, one commit per test or fix |
| Update the diagrams to match UC01-UC06 | Done in #106; the database actor is added in #124 |

## Stand-ups / check-ins
**20 Oct 2026 (Teams chat):** The team agreed to hold the first Sprint 3 stand-up in the week of 26 Oct, because another module's assessment is due this week. The first-PR checkpoint moves from Sun 25 Oct to that stand-up. Meanwhile, members work on the issues that need no database (unit tests and diagrams).

**Status check, 28 Oct 2026 (from GitHub).** The stand-up planned for the week of 26 Oct was not held. Status on 28 Oct, the day before the PR deadline:
- **Done:** shared integration test setup (#114, merged 21 Oct), unit and integration tests in GitHub Actions (#115, merged 21 Oct, approved by Julia), Zain's population breakdown integration tests (#110), database shown as a secondary actor in the use case diagram (#124, Zain, merged 27 Oct).
- **In review:** user stories rewrite (#125) and code coverage (#116), both Djiby.
- **Not started:** no commits, branches or comments since 20 Oct on any of Julia's (13), Hassan's (12) or Kyle's (10) issues, or on Zain's remaining 7 issues.
- **Blockers raised:** none. Nobody reported being stuck.

## Sprint review
_Written on 28 Oct 2026. Update on Sun 1 Nov if more work is merged before the release._

| CR3 checklist item | Evidence |
|---|---|
| Suitable unit tests | `PopulationBreakdownTest` (6 tests, Zain): normal split, zero population, no cities, all cities, fractional percentages, totals above the `int` range |
| Suitable integration tests | `PopulationBreakdownIT` (6 tests, Zain) checks the continent, region and country breakdowns against values calculated independently from the database. All integration tests share one connection setup, `IntegrationTestBase` (#114, Djiby) |
| Tests running on GitHub Actions | Three jobs: Unit tests, Integration tests (MySQL started with Docker Compose), then Build and run the app, which only runs if both pass (#115). A deliberately failing test turned the run red: [run 39463479536](https://github.com/Lancelcode/set08103-group4-coursework/actions/runs/39463479536). The three jobs are required status checks on `main`, `develop` and `release` |
| Code coverage | JaCoCo measures coverage from both test types, uploaded to Codecov (#116) |

Test count: unit 6, integration 6. Coverage: 11.1% of lines (JaCoCo, unit and integration tests).

**CR2 feedback acted on**
- Specific user stories: 32 stories with real roles and testable criteria (#125, `docs/user-stories.md`).
- Database as a secondary actor in the use case diagram (#124).

**Not done**
- The 32 story-level test sets (#130 - #161) and the display unit tests (#119 - #122) were not started by their owners before the PR deadline.
- Menu input tests (#117), the class diagram (#123), the testing guide (#126), the data decision (#118) and the CR3 evidence page (#129) were not started.

## Retrospective
_Written on 28 Oct 2026._

**What went well**
- The test infrastructure is in place and automated: a shared integration test setup, separate unit and integration test jobs in GitHub Actions, and a build that only runs when the tests pass.
- Both pieces of CR2 feedback were acted on (#124, #125).
- Branch protection is enforced on `main`, `develop` and `release`: every change needs a pull request with one approval and passing tests.
- Every issue was written with clear steps, example code, expected values and deadlines, so starting work did not depend on asking first.

**What did not go well**
- Three members (Julia, Hassan and Kyle) made no commits this sprint, and Zain's work stopped after #124. Most planned tests were not written.
- The first stand-up was moved because of another module's assessment and was then not held, so the drop in activity was only seen two days before the deadline.
- #125 and #116 (Djiby) were finished late, after their planned dates.

**What we will change in Sprint 4**
1. **First commit within 48 hours** of the sprint starting. Each member opens a small first PR early, so problems show up straight away.
2. **Written stand-up twice a week** in the Teams chat (done / next / blockers), so it can't be cancelled the way a meeting can.
3. **Rotate the roles**, as agreed at the start of Sprint 3.
4. **Contact the teaching team** if a member still has no contribution by the mid-sprint check, as the assessment brief says to do when contributions are not even.

## Contribution (CR3)
_Draft from GitHub activity on 28 Oct 2026. To be agreed by the team and finalised on Sun 1 Nov._

| Member | GitHub | Sprint 3 contribution | CR3 % |
|---|---|---|---|
| Djiby Sow Rebollo | Lancelcode | Planning and all 48 Sprint 3 issues, shared test setup (#114), CI test jobs (#115), coverage (#116), user stories rewrite (#125), sprint notes (#127), branch protection, reviews | 50 |
| Zain | zainali-uni | JUnit, Surefire and Failsafe setup and 12 tests (#107, #109), database actor in the use case diagram (#124) | 40 |
| Julia | jkasiuk | Reviewed, approved and merged #113, #162 and #164 | 10 |
| Hassan | Hasni243 | No Sprint 3 activity on GitHub | 0 |
| Kyle | Ky1eM05 | No Sprint 3 activity on GitHub | 0 |
| **Total** | | | **100** |
