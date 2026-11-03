---
name: User story
about: One specific report or feature, written for a real user role, with testable acceptance criteria
title: "USxx.y: "
labels: user story
---

## User story
As a <specific role, e.g. population analyst in the research team>, I want <one report or feature> so that <the business value>.

<!--
Name a real role. "As a user" is not a legitimate role (Unit 05b).
One story = one report or lookup. If the story covers several reports, split it.
-->

## Acceptance criteria
<!-- Given / When / Then, with real example values from the World database and the exact messages. Every line must be testable. -->
- [ ] Given ..., when I choose option ... and enter ..., then ... (for example: 46 countries are listed, Russian Federation first)
- [ ] When I enter a name that does not exist, then `...` is printed, with no exception.
- [ ] 

## Tests that prove it
<!-- Name the unit (*Test) and integration (*IT) tests that prove the criteria above. -->
- [ ] `ClassNameIT.methodName`
- [ ] All tests pass with `mvn verify` locally and in GitHub Actions.

## Links
- Parent epic: #
- Use case: `docs/use-cases/UC0x-....md`
- Assessment brief: report ...

## Estimate (story points)
<!-- 1, 2, 3, 5 or 8 -->

## Definition of Done
See `docs/DEFINITION_OF_DONE.md`.
