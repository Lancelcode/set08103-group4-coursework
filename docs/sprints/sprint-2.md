# Sprint 2 - CR2 (task management and requirements)

**Dates:** 28 Sep - 11 Oct 2026
**Milestone:** Sprint 2 - CR2 (task management and requirements)

## Sprint goal
Meet the CR2 checklist (issues, user stories, Kanban board, sprints, use cases and use case diagram), finish the CR1 gaps carried over from Sprint 1, and get the first population reports working.

## Planning
Work was agreed in a short conversation during a class break (Djiby and Julia; Hassan and Zain joined later), then recapped in a Teams stand-up (see below):
- Zain (zainali-uni) agreed to produce the use case and activity diagrams.
- Djiby (Lancelcode) created the backlog, split the six user stories into issues (#13-#18) and assigned an owner to each.
- Each owner picked up their story from the Group_4 board.

| Story / task | Issue | Owner | Points | Outcome |
|---|---|---|---|---|
| US01 Country reports | #13 | jkasiuk | 5 | Done (PR #41) |
| US02 City reports | #14 | Hasni243 | 5 | Done (PR #39) |
| US03 Capital city reports | #15 | Hasni243 | 3 | Done (PR #42) |
| US04 Population breakdown | #16 | zainali-uni | 5 | In review (PR #54) |
| US05 Population totals | #17 | Ky1eM05 | 3 | Done (PR #38); single-place lookups moved to #53 |
| US06 Language statistics | #18 | Lancelcode | 3 | Done (PR #59) |
| Report menu for all reports (covers #43, #44) | #60, #43, #44 | Lancelcode | 5 | Done (PR #61) |
| JAR in CI and Docker | #25, #27-#29 | Lancelcode | 3 | Done (PRs #35-#37, #52) |
| Contributing guide, DoD, templates | #9, #10 | Lancelcode | 4 | Done (PRs #30, #31) |
| Use cases UC01-UC06 | #12 | Lancelcode | 3 | Done (PR #51) |
| Use case and activity diagrams | #63 | zainali-uni | 2 | First version done; update in progress (#63) |
| Code of Conduct and diagrams on develop | - | Lancelcode | 1 | Done (PR #46) |
| DB connection retry | #55 | Lancelcode | 2 | Done (PR #56) |
| App and MySQL in Compose and CI | #57 | Lancelcode | 3 | Done (PR #58) |

## Stand-ups / check-ins
**Teams stand-up, 7 Oct 2026**
Attendees: Djiby (Lancelcode), Julia (jkasiuk); Hassan (Hasni243) and Zain (zainali-uni) joined later. Kyle (Ky1eM05) did not attend.
- Short recap of what had been agreed during the class break.
- Confirmed owners for US01-US06 and that Zain would do the use case and activity diagrams.
- No blockers raised.

Between meetings, progress was shared through the Teams chat, issue assignments on the board, and pull request reviews.

## Sprint review
**Done**
- All six user stories are GitHub issues with acceptance criteria, estimates and owners, on the Group_4 board.
- Full use cases UC01-UC06 in `docs/use-cases/`.
- First version of the use case and activity diagrams (by Zain) in `docs/diagrams/`.
- Country, city, capital city, population total and language reports are on `develop`, and all 28 can be run from a menu in the app.
- The JAR builds in CI, runs in Docker, and CI now runs the app against the real World database with Docker Compose.
- Every change went into `develop` through a pull request, and most PRs were reviewed by someone other than the author.

**Not done**
- US04 (#16) is still in review (PR #54).
- The diagrams need more work to match the use cases (e.g. all six use cases shown, consistent names).
- No first GitHub release yet; `main` and `release` have not been updated from `develop`.

## Retrospective
**What went well**
- Every user story had an owner and every owner delivered code.
- Code review became routine: jkasiuk reviewed most PRs, and review comments led to real fixes (e.g. #42 restored deleted Javadoc and added input checks).
- CI now tests the app against a real database instead of only building it.

**What did not go well**
- Planning happened informally during breaks, and we only held one short stand-up, so there is little written evidence of planning.
- Work was uneven. Most process, CI and documentation work, and a large share of the PRs, came from one person, and some members waited to be handed tasks instead of picking up work from the board.
- PRs #36 and #37 were merged into feature branches instead of `develop`, so the JAR changes were missing from `develop` until #52.
- Some PRs reformatted whole files or deleted unrelated comments, which made them hard to review.

**What we will change in Sprint 3**
1. **Hold a short planning meeting** at the start of the sprint and record it here, with owners and points agreed by everyone.
2. **Weekly 10-minute stand-up** (in class or on Teams), with a dated note added to this folder, instead of a single recap.
3. **Pick up work from the board.** When your issue is done, take the next item from Ready rather than waiting to be assigned.
4. **Everyone reviews.** Each member reviews at least two PRs per sprint.
5. **Keep PRs focused.** No whole-file reformatting; always target `develop`.
6. **Update the diagrams** so they match UC01-UC06 (Zain, #63).
