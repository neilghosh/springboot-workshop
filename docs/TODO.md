# Advanced Annexure Roadmap

These modules are optional extensions after `step-4-outbound-enrichment`. They
must not be introduced into earlier checkpoints because the core three-hour
workshop should remain focused on REST, validation, services, persistence, and
testing.

## Recommended next annexure: Production Insight and Data Integrity

**Suggested duration:** 60–90 minutes

**Goal:** Show participants how to observe a running service and protect
persistent data under realistic API usage.

### 1. Observability

- [ ] Add Spring Boot Actuator.
- [ ] Expose only the required health, info, and metrics endpoints.
- [ ] Demonstrate `/actuator/health` with database and application status.
- [ ] Inspect HTTP request count and latency through Micrometer metrics.
- [ ] Add a request correlation ID and include it in structured application
      logs.
- [ ] Explain the difference between logs, metrics, and traces.
- [ ] Add tests for exposed and non-exposed actuator endpoints.
- [ ] Document safe production exposure; do not expose every actuator endpoint.

**Participant outcome:** Diagnose whether the application is running, whether
its database is reachable, and which API calls are slow or failing.

### 2. Advanced persistence

- [ ] Add pagination and sorting to `GET /api/products`.
- [ ] Add optional category filtering without changing the response DTO/entity
      separation.
- [ ] Add JPA auditing for `createdAt` and `updatedAt`.
- [ ] Add an `@Version` field for optimistic locking.
- [ ] Map optimistic-lock conflicts to `409 Conflict`.
- [ ] Demonstrate concurrent stale updates with an integration test rather than
      an artificial `Thread.sleep` endpoint.
- [ ] Add appropriate database constraints and indexes.
- [ ] Verify behavior with both H2 and PostgreSQL where database behavior may
      differ.

**Participant outcome:** Build scalable list APIs, track record changes, and
detect conflicting updates without silently overwriting another request.

### Acceptance criteria

- [ ] Existing CRUD and summary API behavior remains compatible.
- [ ] Pagination parameters have documented defaults and maximum sizes.
- [ ] Health information does not expose credentials or sensitive details.
- [ ] A stale update produces a deterministic `409 Conflict`.
- [ ] Relevant service and controller integration tests pass.
- [ ] README commands and presentation slides match the implementation.
- [ ] The annexure is published as a new checkpoint without modifying the
      existing stage boundaries.

## Additional optional annexures

### Caching

- [ ] Add Spring Cache around read-heavy product queries.
- [ ] Demonstrate cache hits, misses, eviction after updates, and stale-data
      risks.
- [ ] Start with an in-memory cache; introduce Redis only as a separate
      infrastructure decision.

### OpenAPI documentation

- [ ] Generate an OpenAPI specification and interactive API documentation.
- [ ] Document validation errors, status codes, pagination, and optimistic-lock
      conflicts.
- [ ] Keep `curl` as the primary participant-facing command format.

### Security

- [ ] Add Spring Security after the API behavior is already understood.
- [ ] Keep product reads public and protect write operations.
- [ ] Demonstrate authentication separately from authorization.
- [ ] Avoid committing sample passwords, tokens, or private keys.

### Database migrations

- [ ] Decide explicitly whether schema migration tooling belongs in the
      workshop.
- [ ] If approved, introduce Flyway or Liquibase only in this advanced
      annexure—not in earlier checkpoints.
- [ ] Replace automatic production schema updates with versioned migrations.

## Suggested order

1. Actuator health and metrics.
2. Pagination, sorting, and filtering.
3. JPA auditing.
4. Optimistic locking and `409 Conflict`.
5. Caching.
6. OpenAPI documentation.
7. Security.
8. Database migrations.

Observability plus advanced persistence should be completed before adding the
other annexures. Together they provide the clearest progression from a working
CRUD service to a service that can be operated safely.
