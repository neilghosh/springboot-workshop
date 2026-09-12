---
marp: true
theme: gaia
paginate: true
header: "Spring Boot Workshop"
footer: "neilghosh/springboot-workshop"
size: 16:9
style: |
  section {
    box-sizing: border-box;
    padding: 96px 42px 88px;
    font-size: 22px;
  }
  header {
    top: 18px;
    left: 42px;
    right: 42px;
    line-height: 1.2;
    padding-bottom: 6px;
    border-bottom: 1px solid rgba(127, 127, 127, 0.35);
  }
  footer {
    bottom: 16px;
    left: 42px;
    right: 82px;
    line-height: 1.2;
    padding-top: 6px;
    border-top: 1px solid rgba(127, 127, 127, 0.35);
  }
  section::after {
    right: 42px;
    bottom: 16px;
  }
  h1 { font-size: 1.5em; }
  h2 { font-size: 1.2em; }
  h3 { font-size: 1.0em; }
  table { font-size: 0.58em; }
  pre, code { font-size: 0.68em; }
  pre { max-height: 38vh; }
  .columns { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
  .columns3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 0.8rem; }
  .flow { grid-template-columns: 1fr auto 1fr; }
  .card { border: 2px solid #0288d1; border-radius: 14px; padding: 0.55em 0.7em; background: #f8fbff; }
  .card h4 { margin: 0 0 0.2em; color: #0288d1; }
  .pill { display:inline-block; background:#0288d1; color:#fff; border-radius:999px; padding:0.15em 0.6em; font-size:0.72em; }
  .arrow { color:#0288d1; font-weight: 800; }
---

<!-- _class: lead invert -->

# Spring Boot 4

## E-Commerce API Workshop

**A reusable hands-on backend development workshop**

`@RestController` → `@Service` → `JpaRepository` → **H2 / Postgres**

```
  [ curl ] → [ DispatcherServlet ] → [ Controller ] → [ Service ] → [ JPA ] → [ H2 / Postgres ]
```

---

## Agenda — 3 Hours

<div class="columns3">

<div class="card">

#### ⏱️ 15 min — Setup

`java -version`  
Run app → first API request

</div>

<div class="card" style="border-color:#43a047; background:#f6fdf6;">

#### 🎯 30 min — Concepts

Why Spring Boot · dependency injection · profiles

</div>

<div class="card" style="border-color:#ef6c00; background:#fff8f0;">

#### 🛠️ 120 min — Code

`step-0` → `step-4` live

</div>

</div>

<br>

<div class="pill">Q&A 15 min — API clients · Postgres · debug · conventions</div>

---

### Workshop Toolset

<div class="columns3">

<div class="card">

#### ✅ `curl` — Primary

All README examples<br>
No account needed · easy to automate<br>
Works in every terminal

</div>

<div class="card" style="background:#e8f5e9; border-color:#43a047;">

#### 🟢 Bruno — Optional

Open-source GUI client<br>
Open the `bruno/` collection<br>
Requests stay with the code

</div>

<div class="card">

#### Postman — Compatible

Can send the same requests<br>
Not required for the workshop

</div>

</div>

> Learn how HTTP requests and responses work, without depending on one API client.

---

### Workshop Learning Outcomes

| Learning objective | Workshop implementation |
|---|---|
| Spring Boot fundamentals | Boot, auto-configuration, IoC/DI, profiles |
| REST controllers + CRUD | `ProductController` from Step 1 onward |
| Service layer | `ProductService` introduced with persistence |
| DTO validation | Jakarta constraints + `@Valid` + `400` demo |
| Database integration | H2 default; PostgreSQL production profile |
| API testing | `curl` primary; Bruno optional; Postman compatible |
| Framework conventions | Layer boundaries, DTO/entity separation, transactions |
| Developer etiquette | Tests, small diffs, useful errors, no secrets, code review |

> Use portable HTTP tests instead of depending on one client product.

---

<!-- _class: lead -->

## From Manual Setup

## → Spring Conventions

Spring Boot provides a common structure for long-lived Java services.

---

### Slide 1B — Why Not Just Node / Python / Go?

<div class="columns3">

<div class="card">

#### 🟢 Node (JS/TS)

Fast prototyping · one language across the stack

*Consider: event-loop model and JavaScript runtime errors*

</div>

<div class="card">

#### 🐍 Python

Easy to learn · strong data and AI libraries

*Consider: dynamic typing and lower throughput for some services*

</div>

<div class="card">

#### 🐹 Go

Simple deployment · built-in concurrency

*Consider: a smaller enterprise framework ecosystem*

</div>

</div>

<div class="card" style="margin-top:0.6em; background:#e3f2fd; border-color:#0288d1; text-align:center;">

#### ☕ Java + Spring Boot — **Built for long-lived business systems**

Type safety · mature Security and Data libraries · JVM performance · clear application layers

</div>

---

### Slide 1C — Why Learn This When AI Can Generate Code? 🤖

<div class="columns">

<div>

**AI can draft routine code**

It can quickly create controllers, DTOs, and repositories.

<br>

**You are still responsible for correctness**

* Is `@Valid` present, and are status codes correct?
* Is the database configuration replaceable?
* Is the transaction boundary correct?

</div>

<div class="card" style="background:#fff3e0; border-color:#ef6c00;">

#### Review Before You Ship

`describe → generate → verify → ship`

Without understanding dependency injection, you may approve incorrect code.

<br>

**We first build it ourselves, so we can review generated code confidently.**

</div>

</div>

---

<!-- _class: invert -->

# What Does Spring Boot *Actually* Give You?

`pom.xml` → auto-configured app. No XML. No WAR.

---

### Slide 2 — Boot in 5 Features

<div class="columns">

<div>

| Feature | One-liner |
|---------|-----------|
| **Starter dependencies** | Add a tested group of related libraries |
| **Auto-configuration** | Configure components from available libraries and settings |
| **Embedded Tomcat** | Run the application directly with `java -jar` |
| **External configuration** | Read settings from properties and environment variables |
| **Profiles** | Use the same build with H2 or PostgreSQL |

</div>

<div class="card">

#### Where you see it

* `pom.xml` parent `4.0.8`
* `@SpringBootApplication`
* `mvn spring-boot:run`
* `application.properties:2`
* H2 vs Postgres

</div>

</div>

---

### Slide 3 — How It Works Under the Hood

<div class="columns">

<div class="card">

#### 🔍 Auto-configuration

`@SpringBootApplication` = 
`@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`

Spring Boot applies only the configurations whose libraries and settings are present.

</div>

<div class="card">

#### 📦 Starter = Dependency Bundle

`data-jpa` = 
`spring-data` + `hibernate` + `HikariCP` + transaction support

*One starter adds the libraries needed for database access.*

</div>

</div>

<div class="card" style="margin-top:0.6em;">

**Configuration order:** base H2 settings → production profile overrides → environment variables from `.env` (never committed)

`port 8080` · `show-sql=true` · `/h2-console` · `/health` (Actuator)

</div>

---

### Slide 4 — Inversion of Control: Who Creates Objects?

<div class="columns flow">

<div>

**❌ Manual setup** — your code creates every dependency

```java
repo = new Repo(
  new PostgresDS("jdbc:..."));
// test needs Postgres
```

</div>

<div style="text-align:center; padding-top:1.2em; font-size:2.2em;">

`→`

<br>

<span style="font-size:0.45em; color:#6a7a7d;">Hollywood Principle<br><i>"Don't call us,<br>we'll call you"</i></span>

</div>

<div>

**✅ Spring setup** — the container provides dependencies

```java
@Service
class ProductService {
  ProductService(Repo r){
    this.r = r;
  }
}
```

</div>

</div>

<div class="card" style="text-align:center; margin-top:0.5em;">

The `ApplicationContext` scans `com.example.ecommerce`, creates each component, injects its dependencies, and manages its lifecycle.

You do not create the service in the controller. Spring creates and injects it (`ProductController.java:14`).

</div>

---

### Slide 5 — Dependency Injection: Prefer Constructors

<div class="columns">

<div>

| Style | Verdict |
|-------|---------|
| **Constructor** `Service(Repo r)` | ✅ Required, explicit, and easy to test |
| Setter `setRepo()` | Useful only for optional, changeable dependencies |
| Field `@Autowired` | ❌ Hidden dependency; not used in this workshop |

</div>

<div class="card">

#### Specialized Component Annotations

* `@Service` marks business logic and transaction boundaries.
* `@Repository` marks database access.
* `@RestController` handles HTTP requests and responses.

Spring finds these classes automatically. With one constructor, `@Autowired` is unnecessary.

</div>

</div>

<div class="pill" style="margin-top:0.6em;">Demo: two matching beans cause `NoUniqueBeanDefinitionException`; choose one with `@Qualifier`.</div>

---

### Slide 6 — Bean Lifecycle

```
Instantiate → Inject (DI) → @PostConstruct → Ready → @PreDestroy
                  ↑
         @Transactional proxy wraps the bean
```

`ProductEntity @PrePersist` is a JPA callback. It belongs to the entity lifecycle, not the Spring bean lifecycle.

---

### Slide 7 — Request Lifecycle

<div class="card" style="text-align:center; font-family: monospace; line-height:1.25;">

`curl POST /api/products` <span class="arrow">→</span> **DispatcherServlet** *(auto)* <span class="arrow">→</span> **Controller** `@Valid` <span class="arrow">→</span> **Service** `@Transactional` <span class="arrow">→</span> **Repository** <span class="arrow">→</span> **HikariCP** <span class="arrow">→</span> **H2 / Postgres**

</div>

*Each `@GetMapping` method handles a route; you do not write a servlet yourself.*

---

### Slide 8 — Three Layers, One Rule

<div class="columns3">

<div class="card" style="text-align:center;">

#### 🎮 Controller

`@RestController`

HTTP · `@Valid` · status `201 / 404`

*No SQL*

</div>

<div class="card" style="text-align:center; background:#e8f5e9; border-color:#43a047;">

#### ⚙️ Service

`@Service`

Business rules · `@Transactional` · DTO-to-entity mapping

*Throws not-found errors for the global handler*

</div>

<div class="card" style="text-align:center; background:#fce4ec; border-color:#e53935;">

#### 🗄️ Repository

`JpaRepository`

`findAll()` · `findByCategory()`

*Usually no SQL; Hibernate handles H2 and PostgreSQL differences*

</div>

</div>

> **Rule:** Controllers do not expose entities, and services do not depend on HTTP status codes.

---

### Slide 8B — Validation: Annotation vs Enforcement

<div class="columns">

<div class="card" style="border-color:#e53935; background:#ffebee;">

#### ❌ Looks validated, but is not

```java
public ResponseEntity<?> create(
    @RequestBody ProductRequestDTO request) {
    return service.createProduct(request);
}
```

DTO constraints exist, but the boundary never triggers them.

</div>

<div class="card" style="border-color:#43a047; background:#e8f5e9;">

#### ✅ Reject before business logic

```java
public ResponseEntity<?> create(
    @Valid @RequestBody ProductRequestDTO request) {
    return service.createProduct(request);
}
```

Blank name or negative price → `400 Bad Request`

</div>

</div>

Demo with `bruno/Create Product - Invalid.bru` or the README `curl`.

---

### Slide 8C — Transaction: One Product Is Enough

<div class="columns">

<div class="card" style="border-color:#e53935; background:#ffebee;">

#### ❌ Unclear business boundary

```java
public ProductResponseDTO update(Long id, DTO dto) {
    ProductEntity product = repository.findById(id).orElseThrow();
    mapChanges(product, dto);
    return map(repository.save(product));
}
```

Repository calls may each have their own transaction.

</div>

<div class="card" style="border-color:#43a047; background:#e8f5e9;">

#### ✅ Read-modify-write as one unit

```java
@Transactional
public ProductResponseDTO update(Long id, DTO dto) {
    // lookup + mutation + save
}
```

The service method states the all-or-nothing boundary.

</div>

</div>

**Important:** `@Transactional` defines the commit/rollback boundary. It does
not prevent concurrent updates.

* A `sleep` does not prove that a transaction is safe.
* Use pessimistic locking when one request must block another.
* Use optimistic locking with `@Version` to detect overwritten updates.

Advanced locking and multi-entity transactions belong in a later workshop.

---

### Slide 8D — Conventions and Developer Etiquette

| Convention | Why professionals care |
|---|---|
| Controller → Service → Repository | Each layer has one responsibility |
| DTO ≠ Entity | API changes do not leak into the database model |
| Constructor injection | Dependencies are explicit and testable |
| `@Valid` at the boundary | Invalid data stops before business logic |
| `@Transactional` in services | Business operations define atomicity |
| Profiles + environment variables | Configuration changes without code or secrets |
| Tests for happy and invalid paths | Confidence before sharing or deploying |

**Good team habits:** meaningful names · small changes · useful errors · no secrets · review generated code

---

### Slide 9 — Why DI Wins Here

<div class="columns">

<div class="card" style="border-color:#e53935; background:#ffebee;">

#### ❌ Hard-coded Dependency

`new PostgresDataSource(...)`

Every participant needs PostgreSQL, tests depend on it, and changing databases requires code changes.

</div>

<div class="card" style="border-color:#43a047; background:#e8f5e9; text-align:center;">

#### ✅ Spring Selects the Dependency

```
default    → H2 DS
production → Postgres DS
      ↓
  Repository → Service
```

*Configuration selects the database; application code stays the same.*

</div>

</div>

---

### Slide 9b — Same Code, Different Database

<div class="columns">

<div class="card">

#### 🧪 Workshop

Spring injects H2.

`.\mvnw.cmd test` runs without installing a database.

`MockMvc` + H2

</div>

<div class="card" style="background:#e3f2fd;">

#### 🚀 Production

`-Dspring-boot.run.profiles=production`

Spring configures `HikariCP` and the PostgreSQL dialect.

`ProductService` does not change.

</div>

</div>

At `step-0-starter`, the application can serve HTTP requests. By
`step-3-production`, the production profile stores products in PostgreSQL.

---

### Slide 10 — What You Type vs What Boot Creates

<div class="columns">

<div class="card">

#### ✍️ You Write (~200 Lines)

DTOs · Entity · `JpaRepository` · `Service` · `Controller` · `application*.properties`

*The code is explicit and uses no Lombok, which keeps it easy to teach.*

</div>

<div class="card" style="background:#f3e5f5; border-color:#8e24aa;">

#### ⚙️ Spring Boot Provides

Tomcat · `DispatcherServlet` · `Jackson` · `Validator` · `HikariCP` · `TxManager` · `ExceptionHandler`

</div>

</div>

---

<!-- _class: lead -->

## Part 2 — Hands-On

### 120 min · `step-0` → `step-4`

---

| Step | Tag | Add | File | Time |
|------|--------|-----|------|------|
| 0 | `step-0-starter` | Maven + Web MVC | `pom.xml` | 10 min |
| 1 | `step-1-rest-dto` | REST + validation | `ProductRequestDTO @NotBlank` | 25 min |
| 2 | `step-2-service-db` | Entity + Repo + Service | `ProductEntity @Entity` | 40 min |
| 3 | `step-3-production` | Handler + tests + profiles | `GlobalExceptionHandler` | 35 min |
| 4 | `step-4-outbound-enrichment` | Optional outbound enrichment | `GET /api/products/{id}/summary` | 10 min |

`git switch --detach step-0-starter` → `.\mvnw.cmd spring-boot:run`

---

## Appendix — Backup

* Spring Boot replaces lengthy XML setup with a few property settings.
* `new ProductService(mockRepo)` creates a unit test without a database.
* `@Transactional` keeps `existsById` and `deleteById` in one transaction.
* A later advanced example could save an order and reduce inventory together; basic CRUD does not need it.
