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
  .source { font-size: 0.55em; color: #607d8b; }
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

## Agenda

<div class="columns3">

<div class="card">

#### Setup

Java and Maven · run the application · make an API request

</div>

<div class="card" style="border-color:#43a047; background:#f6fdf6;">

#### Concepts

Spring vs Spring Boot · build tools · dependency injection · profiles

</div>

<div class="card" style="border-color:#ef6c00; background:#fff8f0;">

#### Hands-on Workshop

REST API · validation · persistence · production · outbound HTTP

</div>

</div>

<br>

<div class="pill">Q&A — API clients · Postgres · debugging · conventions</div>

---

### Workshop Toolset

<div class="columns">

<div class="card">

#### ✅ `curl` — Primary

All README examples<br>
No account needed · easy to automate<br>
Works in every terminal

</div>

<div class="card" style="background:#e8f5e9; border-color:#43a047;">

#### Bruno / Insomnia / Postman — Optional Visual Clients

</div>

</div>

> Learn how HTTP requests and responses work, without depending on one API client.

---

### Workshop Learning Outcomes

| Learning objective | Workshop implementation |
|---|---|
| Spring Boot fundamentals | Spring vs Boot, auto-configuration, IoC/DI, profiles |
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

**AI can draft application code**

It can quickly turn a description into a plausible implementation.

<br>

**You are still responsible for correctness**

* Does the design match the requirements?
* Are responsibilities separated clearly?
* Can the behavior be tested and changed safely?

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

### Slide 1D — Spring Framework vs Spring Boot

> **Spring Boot uses Spring Framework. It does not replace it.**

| Question | Spring Framework | Spring Boot |
|---|---|---|
| What does it provide? | Dependency injection, MVC, data access, transactions | A simpler way to configure and run Spring applications |
| Dependencies | Choose and configure individual libraries | Use starter dependencies such as `spring-boot-starter-web` |
| Configuration | More setup is written explicitly | Auto-configuration supplies sensible defaults |
| Web server | Configure deployment or server integration | Embedded Tomcat is ready by default |
| Running the app | Setup depends on the chosen deployment model | Run `main()` or an executable JAR |
| Production support | Add and configure the required tools | Actuator and external configuration integrate easily |

<p class="source">Reference: geeksforgeeks.org/java/difference-between-spring-and-spring-boot/</p>

---

### Slide 1E — Same Spring Concepts, Less Setup

<div class="columns">

<div class="card">

#### Spring Framework: Explicit Setup

```java
@Configuration
@EnableWebMvc
@ComponentScan("com.example")
class AppConfig {
  @Bean
  ProductService productService() {
    return new ProductService();
  }
}
```

You choose and configure the application pieces.

</div>

<div class="card" style="background:#e8f5e9; border-color:#43a047;">

#### Spring Boot: Defaults and Auto-configuration

```java
@SpringBootApplication
public class EcommerceApplication {
  public static void main(String[] args) {
    SpringApplication.run(
        EcommerceApplication.class, args);
  }
}
```

Boot scans components, configures Spring MVC, and starts embedded Tomcat.

</div>

</div>

**Both can use the same** `@RestController`, `@Service`, and `@Repository`
classes. Boot mainly removes repetitive application setup.

---

<!-- _class: invert -->

# Build Tools: From Source Code to Application

A build tool resolves dependencies, compiles code, runs tests, and packages the application.

---

### Slide 1F — Maven, Gradle, and Ant

<div class="columns3">

<div class="card" style="background:#e8f5e9; border-color:#43a047;">

#### Maven — This Workshop

Uses a declarative `pom.xml` and established conventions.

</div>

<div class="card">

#### Gradle — Modern Alternative

Uses `build.gradle` or `build.gradle.kts` and offers a programmable build.

</div>

<div class="card">

#### Ant — Older Example

Uses `build.xml` to define explicit tasks with fewer built-in conventions.

</div>

</div>

> `pom.xml` configures the Maven build. Spring Boot application configuration normally uses annotations and properties instead of traditional Spring XML.

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

#### First Look at Spring Code

```java
@Service
class GreetingService {
  private final MessageSource source;

  GreetingService(MessageSource source) {
    this.source = source;
  }
}
```

`@Service` tells Spring to manage the class. The constructor declares what the
class needs, so Spring can provide it and tests can replace it.

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

<div class="columns">

<div class="card">

#### Option A

```java
class ProductService {
  private final Repo repo =
      new PostgresRepo();
}
```

</div>

<div class="card">

#### Option B

```java
@Service
class ProductService {
  private final Repo repo;

  ProductService(Repo repo) {
    this.repo = repo;
  }
}
```

</div>

</div>

<div class="pill" style="margin-top:0.6em;">

Which option is easier to test and change? What problem could the `new` keyword create?

</div>

---

### Slide 4B — Answer: Let the Container Create Dependencies

**Option B** separates using a repository from choosing its implementation.

1. `@Service` tells Spring that `ProductService` is an application component.
2. The constructor tells Spring that the service requires a `Repo`.
3. The `ApplicationContext` creates a matching repository and passes it in.
4. A test can pass a fake repository without starting PostgreSQL.

This reversal of responsibility is **Inversion of Control**: application code
declares what it needs; the Spring container creates and connects the objects.

---

### Slide 5 — Dependency Injection: Prefer Constructors

<div class="columns">

<div class="card">

#### Option A — Field Injection

```java
@Service
class CheckoutService {
  @Autowired
  private PaymentClient client;
}
```

</div>

<div class="card">

#### Option B — Constructor Injection

```java
@Service
class CheckoutService {
  private final PaymentClient client;

  CheckoutService(PaymentClient client) {
    this.client = client;
  }
}
```

</div>

</div>

<div class="pill" style="margin-top:0.6em;">

Both work in Spring. Which one makes the dependency and a missing setup problem easier to see?

</div>

---

### Slide 5B — Answer: Constructor Injection

<div class="columns">

<div>

**Why prefer the constructor?**

* The class cannot be created without its required dependency.
* The dependency can be `final`.
* A unit test can call `new CheckoutService(fakeClient)`.
* With one constructor, `@Autowired` is unnecessary.

Field injection hides the requirement and leaves the field unset when the class
is created outside Spring.

</div>

<div class="card">

#### What the annotations mean

* `@Service` — business logic managed by Spring
* `@Repository` — database access managed by Spring
* `@RestController` — HTTP requests handled by Spring

The annotation makes the class discoverable; the constructor makes its required
collaborators explicit.

</div>

</div>

---

### Slide 6 — Bean Lifecycle

```
Construct + inject → @PostConstruct → Ready for requests → @PreDestroy
```

```java
@Component
class ProductCache {
  ProductCache(ProductRepository repo) { ... }

  @PostConstruct
  void loadProducts() { ... }

  @PreDestroy
  void clearCache() { ... }
}
```

Spring first calls the constructor and supplies the repository. It then calls
`loadProducts()` once before the bean is used and `clearCache()` during a
graceful application shutdown.

For example, JPA calls
`@PrePersist void setCreatedAt() { createdAt = Instant.now(); }` immediately
before an entity `INSERT`. That is an entity lifecycle callback, not a Spring
bean lifecycle callback.

---

### Slide 7 — Request Lifecycle

<div class="card" style="text-align:center; font-family: monospace; line-height:1.25;">

`curl POST /api/products` <span class="arrow">→</span> **DispatcherServlet** <span class="arrow">→</span> **Controller** <span class="arrow">→</span> **Service** <span class="arrow">→</span> **Repository** <span class="arrow">→</span> **H2 / PostgreSQL**

</div>

1. The `DispatcherServlet` finds the controller method matching the HTTP route.
2. The controller converts JSON to a DTO and validates the request.
3. The service performs the business operation inside a transaction.
4. The repository asks JPA to read or write database rows.
5. The result returns through the controller as an HTTP response.

Spring Boot configures the dispatcher; the application defines routes with
annotations such as `@PostMapping`.

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

`@RequestBody` converts JSON into the DTO, but it does not run Bean Validation.
The constraint annotations are metadata until the controller requests validation.

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

`@Valid` tells Spring to check the DTO before entering the method. If a
constraint fails, Spring returns `400` and does not call the service.

</div>

</div>

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"","description":"Invalid example","price":-1,"stockQuantity":-2,"category":""}'
```

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

When another Spring bean calls this method through the Spring proxy:

1. Spring opens a JPA persistence context and begins a database transaction.
2. Repository operations in the method participate in that same transaction.
3. A normal return commits; an unhandled runtime exception rolls back.
4. Spring closes the persistence context after the method finishes.

This is not a user login session. It is a transaction and persistence context
scoped to the business operation; a database connection is obtained as needed.

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

**Example:** the production profile reads `POSTGRES_URL`, `POSTGRES_USER`, and
`POSTGRES_PASSWORD` from the environment, so deployment settings stay outside
the Java code.

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

Spring loads `application-production.properties` and connects the repository to PostgreSQL.

`ProductService` does not change because it depends on the repository
abstraction, not on database configuration.

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
