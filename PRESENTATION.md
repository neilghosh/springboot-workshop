---
marp: true
theme: gaia
paginate: true
header: "Convergence 2026 — Spring Boot Workshop"
footer: "GDGC VNR VJIET · neilghosh/springboot-workshop"
size: 16:9
style: |
  section { padding: 30px 42px; font-size: 23px; }
  h1 { font-size: 1.5em; }
  h2 { font-size: 1.2em; }
  h3 { font-size: 1.0em; }
  table { font-size: 0.58em; }
  pre, code { font-size: 0.68em; }
  pre { max-height: 38vh; }
  .columns { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
  .columns3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 0.8rem; }
  .card { border: 2px solid #0288d1; border-radius: 14px; padding: 0.55em 0.7em; background: #f8fbff; }
  .card h4 { margin: 0 0 0.2em; color: #0288d1; }
  .pill { display:inline-block; background:#0288d1; color:#fff; border-radius:999px; padding:0.15em 0.6em; font-size:0.72em; }
  .arrow { color:#0288d1; font-weight: 800; }
---

<!-- _class: lead invert -->

# Spring Boot 4

## E-Commerce API Workshop

**Convergence 2026 · GDGC VNR VJIET**

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

Why Boot · IoC/DI · Profiles

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
Zero account · scriptable<br>
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

> Learn HTTP requests and responses—not dependence on one API-client product.

---

### Briefing Requirements — Covered

| Requested module | Workshop implementation |
|---|---|
| Spring Boot fundamentals | Boot, auto-configuration, IoC/DI, profiles |
| REST controllers + CRUD | `ProductController` from Step 1 onward |
| Service layer | `ProductService` introduced with persistence |
| DTO validation | Jakarta constraints + `@Valid` + `400` demo |
| Database integration | H2 default; PostgreSQL production profile |
| API testing | `curl` primary; Bruno optional; Postman compatible |
| Framework conventions | Layer boundaries, DTO/entity separation, transactions |
| Developer etiquette | Tests, small diffs, useful errors, no secrets, code review |

> Intentional substitution: teach portable HTTP testing, not one proprietary client.

---

<!-- _class: lead -->

## From `main()` + JDBC

## → Spring Idioms

70%+ Java microservices — Spring is the enterprise standard

---

### Slide 1B — Why Not Just Node / Python / Go?

<div class="columns3">

<div class="card">

#### 🟢 Node (JS/TS)

Fast MVP · Full-stack JS

*⚠️ Single-thread · runtime bugs*

</div>

<div class="card">

#### 🐍 Python

AI/ML king · Fastest to write

*⚠️ GIL · slow · weak Tx*

</div>

<div class="card">

#### 🐹 Go

Infra · Concurrency

*⚠️ Thin enterprise eco*

</div>

</div>

<div class="card" style="margin-top:0.6em; background:#e3f2fd; border-color:#0288d1; text-align:center;">

#### ☕ Java + Spring Boot — **Run the business for 10 years with 50 engineers**

Type-safe · Ecosystem (Security/Data) · JVM perf (HikariCP) · Team-readable layers

</div>

---

### Slide 1C — Why Code When Agents Code? 🤖

<div class="columns">

<div>

**Agents write 80%**

`ProductController` · DTOs · `Repository` in seconds

<br>

**You own the 20% they get wrong**

* missing `@Valid` / wrong status
* hard-wired `DataSource`
* no `@Transactional`

</div>

<div class="card" style="background:#fff3e0; border-color:#ef6c00;">

#### Review > Generation

`spec → generate → verify → ship`

Without IoC/DI you **ship the bug**.

<br>

**We code *without* agent first — so you can *judge* one after.**

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
| **Starter** | `web/data-jpa/validation` = full stack |
| **Auto-config** | Creates beans if on classpath |
| **Embedded Tomcat** | `java -jar` runs |
| **Config** | `properties` → `production` → env |
| **Profiles** | One build, two `DataSource` |

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

150+ `*AutoConfiguration` → conditional on classpath

</div>

<div class="card">

#### 📦 Starter = Bundle

`data-jpa` = 
`spring-data` + `hibernate` + `HikariCP` + Tx

*One import = whole stack*

</div>

</div>

<div class="card" style="margin-top:0.6em;">

**Config precedence:** `application.properties` (H2 safe for 200) → `application-production.properties` (`${POSTGRES_URL}`) → `.env` (never committed)

`port 8080` · `show-sql=true` · `/h2-console` · `/health` (Actuator)

</div>

---

### Slide 4 — IoC: Who Creates Objects?

<div class="columns">

<div>

**❌ Before** — you `new` everything

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

**✅ After** — container injects

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

`ApplicationContext` : **scan** `com.convergence.ecommerce` → **instantiate** → **inject** → **singleton** → destroy

You never `new` a Service — *Boot does* (`ProductController.java:14`)

</div>

---

### Slide 5 — DI: Constructor Wins

<div class="columns">

<div>

| Style | Verdict |
|-------|---------|
| **Constructor** `Service(Repo r)` | ✅ Immutable, testable |
| Setter `setRepo()` | Mutable only |
| Field `@Autowired` | ❌ Hidden — not used |

</div>

<div class="card">

#### Stereotypes = `@Component`

* `@Service` → business + `@Transactional`
* `@Repository` → DB
* `@RestController` → HTTP

All auto-scanned. Single constructor → `@Autowired` optional.

</div>

</div>

<div class="pill" style="margin-top:0.6em;">Demo: two beans → `NoUniqueBeanDefinitionException` → fix with `@Qualifier`</div>

---

### Slide 6 — Bean Lifecycle

```
Instantiate → Inject (DI) → @PostConstruct → Ready → @PreDestroy
                  ↑
         @Transactional proxy wraps the bean
```

`ProductEntity @PrePersist` → JPA hook (different lifecycle, same idea)

---

### Slide 7 — Request Lifecycle

<div class="card" style="text-align:center; font-family: monospace; line-height:1.25;">

`curl POST /api/products` <span class="arrow">→</span> **DispatcherServlet** *(auto)* <span class="arrow">→</span> **Controller** `@Valid` <span class="arrow">→</span> **Service** `@Transactional` <span class="arrow">→</span> **Repository** <span class="arrow">→</span> **HikariCP** <span class="arrow">→</span> **H2 / Postgres**

</div>

*Each `@GetMapping` is a handler — not a servlet you write.*

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

Logic · `@Transactional` · DTO↔Entity

*Decides 404 → handler*

</div>

<div class="card" style="text-align:center; background:#fce4ec; border-color:#e53935;">

#### 🗄️ Repository

`JpaRepository`

`findAll()` · `findByCategory()`

*No SQL — dialect hides H2/Postgres*

</div>

</div>

> **Rule:** Controllers never touch `Entity` · Services never touch `HttpStatus`

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

**Important:** `@Transactional` defines commit/rollback scope. It does not
automatically lock a row or prevent two requests from overwriting each other.

* A `sleep` does not prove transaction safety.
* Blocking needs explicit pessimistic locking.
* Lost-update detection typically needs optimistic locking with `@Version`.

No second entity or artificial slow endpoint is required for this workshop.
Use Order + Inventory later for an advanced multi-entity example.

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

**Etiquette:** meaningful names · small diffs · useful errors · no secrets · review generated code

---

### Slide 9 — Why DI Wins Here

<div class="columns">

<div class="card" style="border-color:#e53935; background:#ffebee;">

#### ❌ Hard-wired

`new PostgresDataSource(...)`

→ 200 DB installs · tests fail · switch = edit code

</div>

<div class="card" style="border-color:#43a047; background:#e8f5e9; text-align:center;">

#### ✅ Container picks

```
default    → H2 DS
production → Postgres DS
      ↓
  Repository → Service
```

*Profile decides, not code*

</div>

</div>

---

### Slide 9b — Payoff: Zero Code Change

<div class="columns">

<div class="card">

#### 🧪 Workshop

H2 injected

`.\mvnw.cmd test` **<3s** · no install

`MockMvc` + H2

</div>

<div class="card" style="background:#e3f2fd;">

#### 🚀 Production

`-Dspring-boot.run.profiles=production`

→ `HikariCP` + `PostgreSQLDialect`

`ProductService` **unchanged**

</div>

</div>

```
step-0-starter → curl []  (Web MVC)      step-3-production + production → psql \dt → products
```

---

### Slide 10 — What You Type vs What Boot Creates

<div class="columns">

<div class="card">

#### ✍️ You type (~200 LOC)

DTOs · Entity · `JpaRepository` · `Service` · `Controller` · `application*.properties`

*Verbose, no Lombok — readable*

</div>

<div class="card" style="background:#f3e5f5; border-color:#8e24aa;">

#### ⚙️ Boot creates

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

* 50 lines XML → 3 lines `properties`
* `new ProductService(mockRepo)` — DB-free test
* `@Transactional` → `existsById` + `deleteById` atomic
* Two entities? Save Order + reduce Inventory — useful later, unnecessary for CRUD
