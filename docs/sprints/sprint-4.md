# Sprint 4 - CR4 (deployment)

**Dates:** 19 - 24 Nov 2026  
**Milestone:** Sprint 4 - CR4 (deployment)  
**Code Review 4:** Tuesday 24 Nov 2026

## Sprint goal

Complete deployment and bug-reporting work for CR4, keep the GitHub Actions workflow passing, publish the application through GitHub Releases, and prepare clear evidence for the final code review.

## Roles

The Sprint 3 roles were rotated for Sprint 4.

| Member | Sprint 3 role | Sprint 4 role | What it means |
|---|---|---|---|
| Julia (jkasiuk) | Product Owner | Scrum Master | Keeps the sprint organised, checks progress, reviews PRs and writes the CR4 evidence page |
| Hassan (Hasni243) | Review evidence lead | Deployment | GitHub Releases, deployment documentation and deployment fixes |
| Kyle (Ky1eM05) | Release manager | Code quality and testing | PMD fixes in `App.java`, README fix and the bug hunt |
| Zain (zainali-uni) | Design and diagrams lead | Bug reporting and design | Bug-reporting process, issue templates and the deployment diagram |
| Djiby (Lancelcode) | Scrum Master, CI and test setup | Release manager and code quality setup | PMD in CI, versioned releases and the v1.0 release (taken over from Kyle on 23 Nov) |

## Planning

Sprint 4 is a short final sprint before Code Review 4.

The team agreed on these deadlines:

- **Sun 22 Nov, 18:00** - first PR open
- **Mon 23 Nov, 15:00** - all work merged into `develop`
- **Mon 23 Nov, 20:00** - v1.0 release
- **Tue 24 Nov** - Code Review 4

### Tasks

| Issue | Task | Owner | Status |
|---|---|---|---|
| #229 | Automate deployment to GitHub Releases | Hassan | Done (PR #230) |
| #231 | Bug: workflow file does not parse | Hassan | Done (PR #230) |
| #232 | Bug: `latest` release points to the old commit | Hassan | Done (PR #230) |
| #233 | Document deployment and releases in the README | Hassan | Done (PR #255) |
| #234 | Add PMD static analysis to the build and CI | Djiby | Done (PR #250) |
| #235 | Read database credentials from environment variables | Djiby | Done (PR #251) |
| #236 | Test failed connection and disconnect paths | Djiby | Done (PR #252) |
| #237 | CR4 contribution spreadsheet and Moodle submission | Djiby | Done before CR4 |
| #238 | Sprint 4 notes | Hassan | Done (PR #256) |
| #239 | CR4 evidence page | Julia | Done |
| #240 | Fix PMD findings in Menu and the model classes | Julia | Done (PR #254) |
| #241 | Unit tests for `Menu.runAll` and uncovered Menu branches | Julia | Done (PR #253) |
| #242 | Bug: README report count | Kyle | Done (PR #257), corrected to 36 menu options |
| #243 | Fix PMD findings in `App.java` | Kyle | Done (PR #262), PMD now reports 0 findings |
| #244 | Final release v1.0 with automated versioned releases | Djiby | Done |
| #245 | Bug hunt in Docker | Kyle | Done, no new bugs found |
| #246 | Feature request template and issue chooser config | Zain | Done (PR #258) |
| #247 | Bug process in CONTRIBUTING | Zain | Done (PR #259) |
| #248 | README "Reporting a bug" section | Zain | Done (PR #260) |
| #249 | UML deployment diagram | Zain | Done (PR #261) |

## Key dates

| Planned | What | Actual |
|---|---|---|
| Sun 22 Nov, 18:00 | First PR open | The first PRs were opened from 20 Nov, most of them on 22 and 23 Nov |
| Mon 23 Nov, 15:00 | All work merged into `develop` | 7 PRs merged at 18:00, the rest on the evening of 23 Nov |
| Mon 23 Nov, 20:00 | v1.0 release | Published on the evening of 23 Nov |
| Tue 24 Nov | Code Review 4 | |

## Acting on the Sprint 3 retrospective

| Sprint 3 action | What happened in Sprint 4 |
|---|---|
| First commit within 48 hours of the sprint starting | Some members opened PRs within 48 hours, but most of the work was done in the last two days |
| Written stand-up twice a week in Teams | Written check-ins in Teams on 21 and 23 Nov |
| Rotate the roles | Roles were rotated (see Roles) |
| Contact the teaching team if a member has no contribution by the mid-sprint check | On 23 Nov a 13:00 deadline was set in Teams, after which unstarted tasks would be reassigned. Everyone delivered before it, so the teaching team did not need to be contacted |

## Stand-ups / check-ins

**21 Nov 2026 (Teams):** Djiby announced Sprint 4 as the final short sprint before CR4, with the Sunday first-PR deadline and the Monday merge deadline. Julia became Scrum Master, Hassan took deployment, Zain took bug reporting and design, Kyle took code quality and the release, and Djiby took code quality setup.

**21 Nov 2026 (Teams update):** Djiby reported that PMD work was progressing and that Hassan's deployment PR #230 was failing CI because of workflow and release-tag problems. Issues #231 and #232 were created with the bug template to record and fix them.

**22 Nov 2026:** Hassan fixed the deployment workflow and the checks passed. Djiby's PMD, credentials and connection test PRs (#250 - #252) and Julia's Menu PRs (#253, #254) were merged. Hassan opened #255 (deployment README) and #256 (these notes).

**23 Nov 2026, morning (Teams):** Status check: 8 of 20 issues closed and several tasks not started yet. A 13:00 deadline was set for the remaining first PRs. Djiby took over the v1.0 release (#244) to add automated versioned releases, as recommended by the lecturer.

**23 Nov 2026, during the day:** Zain opened PRs for all four bug-reporting and design issues (#258 - #261). Kyle opened #257 and #262 and posted the bug hunt results on #245.

**23 Nov 2026, evening:** All open PRs were reviewed and merged, PMD was set to fail the build on any finding, and v1.0 was released through `develop` -> `release` -> `main`.

## Sprint review

_Written on 23 Nov 2026._

| CR4 item | Evidence |
|---|---|
| Deployment working | Every push to `release` publishes the JAR to the `latest` GitHub Release (#229, #232). Every version tag on `main` publishes its own release with the JAR and notes (#244). v1.0 was released this way |
| Bug reporting set up | Bug report and feature request templates, issue chooser config (#246), bug labels for priority and severity, and the bugfix process in CONTRIBUTING (#247). Bugs #231, #232 and #242 were logged with the bug template and priority and severity labels, and fixed through PRs |
| Code quality | PMD runs in CI (#234). All findings were fixed (#240, #243) and the PMD job now fails on any new finding |
| CI working | Four jobs on every push: unit tests, integration tests, PMD, then build and run the app |
| Tests | 70 unit tests and 93 integration tests |
| Documentation | README sections on deployment (#233) and reporting a bug (#248), UML deployment diagram (#249), CR4 evidence page (#239) |

## Retrospective

### What went well

- Deployment is fully automated: `latest` on every release push and a versioned release on every tag.
- The bug-reporting process was used on real bugs during the sprint (#231, #232, #242).
- PMD findings went down to 0 and the PMD job now fails on any new finding.
- PRs were small and focused, one per issue, and every PR passed CI before it was merged.

### What did not go well

- Some members started late and most of the work was done in the last two days, leaving little time for reviews.
- The PRs opened on 23 Nov were not reviewed until the evening, so the 15:00 merge deadline was missed.
- The v1.0 release was reassigned on the last day to add automated versioned releases.

### What we would improve

- Start every task within 48 hours and push early work, even if it is unfinished.
- Review PRs the same day they are opened instead of in one batch at the deadline.
- Raise blockers in Teams straight away instead of staying silent.

## Contribution (CR4)

_Based on GitHub activity up to 23 Nov 2026._

| Member | GitHub | Sprint 4 contribution | CR4 % |
|---|---|---|---|
| Djiby Sow Rebollo | Lancelcode | Sprint planning and all 20 issues, PMD in CI (#234), DB credentials (#235), connection tests (#236), versioned releases and v1.0 (#244), contribution and Moodle (#237), reviewed and merged the final PRs | 26 |
| Hassan | Hasni243 | Automated deployment (#229) and its bug fixes (#231, #232), deployment README (#233), Sprint 4 notes (#238) | 20 |
| Kyle | Ky1eM05 | All PMD findings in `App.java` and Menu with 18 test classes updated (#243), README count (#242), bug hunt (#245) | 18 |
| Zain | zainali-uni | Feature request template and issue config (#246), bug process (#247), README bug section (#248), UML deployment diagram (#249), CR4 evidence page (#239) | 20 |
| Julia | jkasiuk | Scrum Master, PMD fixes in Menu (#240), Menu coverage tests (#241), reviewed the Sprint 4 notes and PRs | 16 |
| **Total** | | | **100** |
