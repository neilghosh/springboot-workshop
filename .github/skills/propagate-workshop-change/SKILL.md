---
name: propagate-workshop-change
description: 'Propagate a code, configuration, test, or documentation change from a selected Spring Boot workshop stage through every later checkpoint and main. Use when asked to check in a change across tags, update workshop checkpoints, regenerate stage tags, or make a change appear from step 0, 1, 2, 3, 4, 5, or 6 onward.'
argument-hint: 'Describe the change to propagate'
user-invocable: true
disable-model-invocation: false
---

# Propagate Workshop Change

Use this workflow after implementing a change on `main`, or when the user asks
to make a change appear in one or more workshop checkpoint tags.

## Repository Model

- `main` is the maintained implementation of the latest completed workshop.
- `main` is not necessarily the same commit as the latest checkpoint tag.
- Tags are immutable published snapshots. A normal commit to `main` does not
  change any existing tag.
- Each checkpoint is an independently runnable teaching stage. Never point all
  tags at `main` or copy later-stage code into an earlier stage.
- Existing checkpoint histories may be non-linear. Do not assume one tag is an
  ancestor of the next; inspect every tag directly.

Checkpoint order:

1. `step-0-starter`
2. `step-1-rest-dto`
3. `step-2-service-db`
4. `step-3-complete`
5. `step-3-production`
6. `step-4-outbound-enrichment`
7. `step-5-order-relationship`
8. `step-6-dependency-inversion`
9. `main`

Treat both step 3 tags as distinct published snapshots unless the user
explicitly asks to retire or rename one.

## Choose The Earliest Stage

Before changing checkpoints, use the question tool to ask:

> From which workshop stage should this change first appear?

Offer these choices:

- Step 0: update every checkpoint and `main`
- Step 1: update step 1 and every later checkpoint plus `main`
- Step 2: update step 2 and every later checkpoint plus `main`
- Step 3: update both step 3 checkpoints, steps 4 through 6, and `main`
- Step 4: update steps 4 through 6 plus `main`
- Step 5: update steps 5 and 6 plus `main`
- Step 6: update only step 6 and `main`
- Main only: do not update checkpoint tags

If the change itself clearly belongs to a later concept, explain the conflict
with `AGENTS.md` and recommend the earliest valid stage. Do not silently choose
a stage or introduce concepts early.

## Procedure

1. Read `AGENTS.md`, the affected sections of `README.md` and
   `docs/PRESENTATION.md`, and the directly related implementation and tests.
2. Inspect the working tree. Preserve unrelated user changes and generated
   files. Never include secrets, `.env`, `target/`, or unrelated lock-file
   changes.
3. Inspect each affected tag with `git show` before editing it. Record every old
   tag object ID.
4. Ensure the intended final form of the change exists on `main`, including
   tests and synchronized workshop documentation. Commit only when the user has
   explicitly asked for commits.
5. Starting at the selected stage, create an isolated branch or worktree from
   each affected tag. Apply only the form of the change valid at that stage.
   Carry it forward through each later checkpoint without importing unrelated
   content from `main`.
6. Verify every affected stage independently using the minimum command required
   by `AGENTS.md`. Use `./mvnw`; broaden to `./mvnw clean test` when required.
7. Compare adjacent rebuilt stages and confirm each transition introduces only
   concepts assigned to that workshop step.
8. Before moving any local tag, present a table containing tag name, old commit
   ID, proposed new commit ID, and verification result. Ask for explicit
   approval to move the tags.
9. Before updating remote tags, show the exact non-interactive push command,
   explain that rewriting published tags affects existing clones, and ask for
   separate explicit approval. Prefer `--force-with-lease=<tag>:<old-object-id>`
   over an unrestricted force push when supported.
10. Clean up only temporary branches and worktrees created by this workflow.
    Never delete user branches or discard pre-existing changes.

## Required Stop Conditions

Stop and ask the user before:

- moving, deleting, or renaming any tag;
- force-pushing any tag or rewriting remote history;
- resolving a mismatch between source code and workshop documentation when the
  intended teaching behavior is unclear;
- including a dirty working-tree change that was not created for this task.

Do not claim completion until `main` and every selected checkpoint have been
verified, or clearly report which external dependency prevented verification.