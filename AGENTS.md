# AGENTS.md — Spring Boot Workshop

## Mission

This repository is a progressive teaching workshop, not only a finished Spring
Boot application. Preserve the order in which concepts are introduced.

A change is incomplete if the final code works but any of these become
inconsistent:

- workshop checkpoints;
- participant instructions;
- presentation slides;
- tests and expected output;
- local, Dev Container, Windows, or production setup.

Prefer code that is explicit and easy to teach over abstractions that merely
reduce line count.

## Sources of Truth

- `README.md`: participant journey, commands, endpoints, and expected results.
- `docs/PRESENTATION.md`: instructor narrative and live-coding sequence.
- `pom.xml`: Java, Spring Boot, and dependency versions.
- `src/main/resources/application*.properties`: profile behavior.
- `.env.example`: documented environment variables without real credentials.
- Git tags: published snapshots of the workshop stages.
- This file: rules for modifying the repository safely.

When these disagree, do not silently choose one. Determine the intended
workshop behavior, then align every affected surface.

## Workshop Progression

Each stage is cumulative. A concept introduced in one stage normally remains
present in all later stages.

| Stage | Introduces | Must not introduce early |
|---|---|---|
| `step-0-starter` | Application entrypoint and Web MVC dependency | Product API, persistence, production configuration |
| `step-1-rest-dto` | Product DTOs, validation, controller, in-memory storage | JPA, repository, database-backed service |
| `step-2-service-db` | Service layer, entity, repository, JPA, H2, full CRUD | PostgreSQL profile, outbound enrichment |
| `step-3-production` | Global errors, tests, profiles, PostgreSQL | Outbound enrichment, orders |
| `step-4-outbound-enrichment` | `RestTemplate`, proxy-ready external client, product summary | Orders and entity relationships |
| `step-5-order-relationship` | Order creation, `ManyToOne` product relationship, order summary DTO | Payment processing and dependency inversion |
| `step-6-dependency-inversion` | Payment interface, fixed `@Primary` implementation, order processing | Nothing beyond the current workshop |
| `main` | Maintained form of the latest completed stage | Unplanned concepts that bypass the workshop sequence |

Pedagogical boundaries are requirements. Do not move an implementation into an
earlier stage just because it is cleaner or more production-like.

## Before Making a Change

1. Identify the earliest stage whose behavior or teaching material is affected.
2. Determine every later stage that inherits the changed concept.
3. Read the corresponding sections of `README.md` and
   `docs/PRESENTATION.md`.
4. Inspect related code, tests, configuration, environment templates, and
   Dev Container files.
5. Decide whether the request changes only `main` or also requires publishing
   replacement checkpoint snapshots.

Examples:

- DTO validation or REST contract changes affect `step-1` and every later stage.
- Entity, repository, service, or H2 changes affect `step-2` and every later stage.
- error handling, tests, PostgreSQL, or profile changes affect `step-3` onward.
- external client or product-summary changes affect `step-4`, `step-5`, and `main`.
- order or product-relationship changes affect `step-5` and `main`.
- payment or dependency-inversion changes affect `step-6` and `main`.
- Java, Maven, Dev Container, or command changes may affect every stage.
- wording changes affect only the documents unless they alter an instruction,
  command, expected response, or teaching sequence.

## Required Change Propagation

Do not update only the latest implementation when a change belongs to an
earlier workshop stage.

For every behavioral change:

1. Preserve the API contract in later stages unless the change intentionally
   modifies that contract.
2. Propagate the concept through all later checkpoints where it remains relevant.
3. Keep README commands and expected output executable against the matching stage.
4. Keep presentation snippets and explanations consistent with the code shown at
   that point in the workshop.
5. Update tests at the stage where the behavior first becomes testable.
6. Check both H2 and production-profile implications when persistence or
   configuration changes.

If checkpoint publication is not part of the request, modify `main` and report
which checkpoint tags would need regeneration. Never move tags as an incidental
side effect of an ordinary code change.

## Checkpoint and Tag Guardrails

Tags are published workshop artifacts. Treat them as immutable during normal
development.

- Do not create, delete, move, or force-push a tag unless the user explicitly
  requests checkpoint publication.
- Do not make an old tag point directly at `main`; each tag must remain a valid,
  independently runnable teaching stage.
- Do not add later-stage code to an earlier checkpoint.
- Do not rewrite remote history or force-push without explicit approval.
- Before changing checkpoints, inspect the existing stage with `git show` or a
  temporary worktree instead of assuming it matches `main`.

When explicitly asked to regenerate checkpoints:

1. Rebuild the progression in stage order, starting with the earliest affected
   stage.
2. Use isolated branches or worktrees so stages do not contaminate each other.
3. Verify each stage before preparing the next cumulative stage.
4. Confirm that the diff from one stage to the next teaches only the concepts
   assigned to that transition.
5. Update local tags only after every affected stage passes verification.
6. Present the old and new tag commit IDs before any remote tag update.

## Repository Invariants

### Teaching and code style

- Use Java 17 and the Spring Boot version declared in `pom.xml`.
- Do not add Lombok. Explicit constructors, getters, setters, and mappings are
  intentional workshop material.
- Use constructor injection.
- Keep controllers focused on HTTP concerns and services focused on business
  behavior.
- Keep request/response DTOs separate from JPA entities.
- Do not return `ProductEntity` directly from controller endpoints.
- Keep mappings explicit, including `mapToResponseDTO`, unless the workshop is
  deliberately changed to teach a mapping library.
- Use Jakarta validation on request DTOs and `@Valid` at controller boundaries.
- Use Spring Data repositories rather than adding direct JDBC code.
- Use `@Transactional` at appropriate service boundaries.
- Avoid adding architecture, frameworks, or patterns that have not been assigned
  a place in the workshop progression.

### Configuration and data

- H2 is the zero-setup default profile.
- PostgreSQL is enabled through the `production` profile.
- Keep profile-specific values in `application-production.properties`.
- Keep environment-variable names aligned across properties, `.env.example`,
  Docker Compose, README instructions, and presentation material.
- Never commit `.env`, passwords, tokens, connection secrets, or real credentials.
- The Dev Container reads the root `.env`; do not hardcode credentials in
  `.devcontainer` files.
- Use `database` as the PostgreSQL hostname inside Docker Compose and `localhost`
  for a PostgreSQL server running directly on the host.
- Preserve proxy support for outbound HTTP unless the workshop requirement is
  intentionally changed.

### Participant experience

- Participant-facing API examples use `curl`, not Postman.
- Preserve both Unix/macOS and Windows usability when changing commands.
- In PowerShell, quote Maven `-D` arguments.
- Keep `curl.exe` JSON examples PowerShell-safe and on one command line where
  documented that way.
- Do not require software outside the documented local or Dev Container setup.
- Error responses and status codes shown in documentation must match runtime
  behavior.

### Repository hygiene

- Do not commit `target/`, `.env`, IDE state, operating-system files, or other
  generated artifacts.
- Keep `.vscode/launch.json` and `.vscode/extensions.json` tracked; other
  `.vscode` state is intentionally ignored.
- Do not introduce generated code or migration tooling without an explicit
  workshop decision.
- Avoid unrelated refactors. Small teaching diffs are easier to explain and
  compare between stages.

## Project Map

```text
src/main/java/com/example/ecommerce/
├── EcommerceApplication.java
├── client/          # outbound product integration, introduced at step 4
├── config/          # RestTemplate and infrastructure configuration
├── controller/      # HTTP endpoints and validation boundary
├── dto/             # request, response, and composed response types
├── exception/       # global API error handling
├── model/           # JPA entities
├── repository/      # Spring Data repositories
└── service/         # transactions, business behavior, and DTO mapping

src/test/java/com/example/ecommerce/
├── controller/      # MockMvc/Spring Boot integration tests
└── service/         # Mockito unit tests
```

The project is a single Maven module with package root
`com.example.ecommerce`.

## Commands

Use the Maven wrapper rather than relying on a globally installed Maven version.

### Unix/macOS

```bash
./mvnw spring-boot:run
./mvnw clean test
./mvnw -Dtest=ProductServiceTest test
./mvnw -Dtest=ProductControllerIntegrationTest test
SPRING_PROFILES_ACTIVE=production ./mvnw spring-boot:run
```

### Windows PowerShell

This workshop requires JDK 17. If the machine default differs, set `JAVA_HOME`
to the JDK 17 installation before running the wrapper.

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd clean test
.\mvnw.cmd "-Dtest=ProductServiceTest" test
.\mvnw.cmd "-Dtest=ProductControllerIntegrationTest" test
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=production"
```

PowerShell must quote Maven arguments beginning with `-D`.

## Verification Strategy

Run the smallest verification that proves the change, then expand when the
affected surface is broader.

| Change | Minimum verification |
|---|---|
| DTO validation or mapping | related service test and controller integration test |
| Controller endpoint or status code | controller integration test |
| Service behavior | `ProductServiceTest` |
| Entity, repository, JPA, or H2 config | full `./mvnw clean test` |
| Production profile or PostgreSQL config | full tests plus production-profile startup when PostgreSQL is available |
| Outbound client or summary endpoint | relevant tests plus a live summary request when practical |
| Maven, Java, or shared infrastructure | full clean test |
| Documentation only | verify commands, paths, stage names, and examples against the repository |
| Checkpoint regeneration | verify every affected tag independently |

Default full verification:

```bash
./mvnw clean test
```

Expected live checks where relevant:

```bash
curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/{id}/summary
```

Do not claim PostgreSQL verification if only H2 tests were run. State clearly
when an external dependency prevented a live check.

## Documentation Synchronization

Update related documentation in the same change when any of these change:

| Changed surface | Also inspect |
|---|---|
| endpoint, payload, status, or validation | README, presentation, controller tests |
| stage boundary or teaching order | README workshop path, presentation agenda/slides, all later checkpoints |
| dependency or Java/Spring version | README setup, Dev Container, wrapper behavior, presentation references |
| profile or environment variable | both properties files, `.env.example`, Compose, README, presentation |
| test count or test command | README expectations and workshop instructions |
| outbound URL or proxy behavior | client configuration, properties, README, presentation, static example response |

Do not duplicate large explanations between documents. Keep participant
instructions in `README.md`; keep agent execution rules here.

## Definition of Done

A repository change is complete only when:

- the earliest affected workshop stage is identified;
- later stages remain valid cumulative extensions;
- code follows the repository invariants;
- relevant tests and live checks pass, or an unverified dependency is stated;
- README commands and expected results match the implementation;
- presentation content matches the workshop sequence;
- configuration and environment-variable names agree everywhere;
- no secret, build output, or unrelated file is included;
- checkpoint impact is reported, and tags are changed only when explicitly
  requested.
