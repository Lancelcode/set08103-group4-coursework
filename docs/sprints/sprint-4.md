# Sprint 4 - CR4 (deployment)

**Dates:** 19 - 24 Nov 2026  
**Milestone:** Sprint 4 - CR4 (deployment)  
**Code Review 4:** Tuesday 24 Nov 2026

## Sprint goal

Complete deployment and bug-reporting work for CR4, keep the GitHub Actions workflow passing, publish the application through GitHub Releases, and prepare clear evidence for the final code review.

## Roles

The Sprint 3 roles were rotated for Sprint 4.

| Member | Role | What it means |
|---|---|---|
| Julia (jkasiuk) | Scrum Master | Keeps the sprint organised, checks progress and written stand-ups |
| Hassan (Hasni243) | Deployment | Works on GitHub Releases, deployment documentation and deployment fixes |
| Kyle (Ky1eM05) | Release manager | Prepares and publishes the final v1.0 release |
| Zain (zainali-uni) | Bug reporting and design | Works on the bug-reporting process, templates and design |
| Djiby (Lancelcode) | Code quality | Works on PMD, CI quality checks and code-quality fixes |

## Planning

Sprint 4 is a short final sprint before Code Review 4.

The team agreed on these deadlines:

- **Sun 22 Nov, 18:00** - first PR open
- **Mon 23 Nov, 15:00** - all work merged into `develop`
- **Mon evening** - Kyle publishes v1.0
- **Tue 24 Nov** - Code Review 4

### Tasks

| Issue | Task | Owner | Status |
|---|---|---|---|
| #229 | Automate deployment to GitHub Releases | Hassan | In review |
| #231 | Fix GitHub Actions workflow parsing | Hassan | Done as part of PR #230 |
| #232 | Fix `latest` release pointing to old commit | Hassan | Done as part of PR #230 |
| #233 | Document deployment and releases in README | Hassan | In review (PR #255) |
| #234 | Add PMD static analysis to the build and CI | Djiby | In progress |
| #235 | Read database credentials from environment variables | Djiby | In progress |
| #236 | Test failed connection and disconnect paths | Djiby | In progress |
| #238 | Sprint 4 notes, roles, stand-ups, review and retrospective | Hassan | In progress |
| #240 | Fix PMD findings in Menu.java and model classes | Julia | In progress |
| #241 | Unit tests for Menu.runAll and uncovered Menu branches | Julia | In progress |

## Key dates

| Planned | What | Actual |
|---|---|---|
| Sun 22 Nov, 18:00 | First PR open | In progress |
| Mon 23 Nov, 15:00 | All work merged into `develop` | |
| Mon evening | Publish v1.0 | |
| Tue 24 Nov | Code Review 4 | |

## Acting on the Sprint 3 retrospective

Sprint 3 identified four actions for Sprint 4:

1. Open a first PR early.
2. Hold written stand-ups in Teams.
3. Rotate team roles.
4. Raise contribution problems early rather than waiting until the deadline.

Sprint 4 follows these actions by using rotated roles and written progress updates in Teams.

## Stand-ups / check-ins

**21 Nov 2026 (Teams):** Djiby announced Sprint 4 and confirmed that it is the final short sprint before CR4. The team was given the Sunday first-PR deadline and Monday merge deadline. Julia became Scrum Master, Hassan took deployment, Kyle remained release manager, Zain took bug reporting and design, and Djiby took code quality.

**21 Nov 2026 (Teams update):** Djiby reported that PMD work was progressing and that Hassan's deployment PR #230 was failing CI because of workflow and release-tag problems. Issues #231 and #232 were created to record and fix those bugs.

**22 Nov 2026:** Hassan fixed the deployment workflow problems and the GitHub Actions checks passed. Deployment documentation was added in #233 with PR #255 and sent for review.

## Sprint review

_To be completed on 23-24 Nov after the final merges and v1.0 release._

The review will check the CR4 requirements, including:

- deployment through GitHub Actions
- GitHub Releases
- bug-reporting process
- code quality / PMD
- passing unit and integration tests
- final documentation
- evidence for the marker

## Retrospective

_To be completed after the Sprint 4 work is finished._

### What went well

- To be completed.

### What did not go well

- To be completed.

### What we would improve

- To be completed.

## Contribution (CR4)

_To be completed from the final GitHub activity before the code review._
