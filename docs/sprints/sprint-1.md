# Sprint 1 - CR1 (workflow set-up)

> Written retrospectively on 2 Oct 2026 from the repo, issue and PR history. The sprint ran before we had a written sprint routine, so planning and stand-up meetings were not recorded. From Sprint 2 onwards we record them (see `docs/CONTRIBUTING.md`).

## Sprint goal
Set up the project workflow for Code Review 1: GitHub project, product backlog, GitFlow branches, Maven JAR, Dockerfile, GitHub Actions, first release and Code of Conduct.

## What was planned and what happened
| Item | Issue / PR | Outcome |
|---|---|---|
| Create base backlog | #2, PR #1 | Done (plain text `backlog` file on `release`) |
| Create development branch | #3 | Done (`develop` exists) |
| Create release branch | #4 | Done (`release` exists) |
| Code of Conduct | PR #5 | Done (on `release` only, not visible on `main` or `develop`) |
| Complete product backlog (CR1) | #6 | In review |
| GitHub Actions workflow and Dockerfile | commits on `main` and `develop` | Partly done: builds and runs Docker, but no JAR |
| Self-contained Maven JAR | not started | Moved to Sprint 2 (#25, #27, #28, #29) |
| First GitHub release | not started | Moved to Sprint 2 |
| Branches merged and visible to the reviewer | not done | Moved to Sprint 2 |

## Stand-up / check-ins
Not recorded in Sprint 1.

## Sprint review
- Done: repository, branches (`main`, `develop`, `release`), backlog file, Code of Conduct, CI workflow running Docker.
- Not done: Maven JAR, first release, backlog and Code of Conduct visible on the branch the reviewer sees, branches merged.
- Result: Code Review 1 scored 6/20.

## Retrospective
**What went well**
- The repository, branches, backlog and Code of Conduct were all created.
- A CI workflow that builds and runs a Docker image was in place.
- The team started using Issues, PRs and a Project board.

**What did not go well**
- The JAR and first release were missing at the review.
- Work was spread across separate branches that were never merged, so the reviewer could not see the Code of Conduct and backlog together.
- Some changes were pushed directly to `main` and `develop` instead of through reviewed PRs.
- Contributions were concentrated in a few members, and there was no recorded sprint planning.

**What we will change in Sprint 2**

1. **Review before merge.** Every PR into `develop` gets approved by someone other than its author before it is merged.
    - Owner: all members
    - Evidence: an approving review on every PR from now on
2. **Plan and record the sprint.** Hold a sprint planning meeting and a weekly stand-up, and record them in `docs/sprints/`.
    - Owner: to be agreed at the next team meeting
    - Evidence: dated notes in `sprint-2.md`
3. **Everyone owns work.** Each member owns at least one user story and opens at least one PR.
    - Owner: all members
    - Evidence: issues assigned on the board and PRs on GitHub
4. **Make the work visible to the reviewer.** Merge the Code of Conduct and backlog into `develop`, then `main`.
    - Owner: to be agreed at the next team meeting
    - Evidence: both files visible on `main`