---
marp: true
theme: gaia
class: invert
paginate: true
header: "Spring Boot in 30 Minutes"
footer: "neilghosh/springboot-workshop"
size: 16:9
style: |
  section {
    box-sizing: border-box;
    padding: 96px 42px 88px;
    font-size: 24px;
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
  h1 { font-size: 1.65em; }
  h2 { font-size: 1.3em; }
  h3 { font-size: 1.05em; }
  table { font-size: 0.66em; }
  pre, code { font-size: 0.72em; }
  pre { max-height: 40vh; }
  .columns { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
  .columns3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 0.8rem; }
  .card {
    border: 2px solid #38bdf8;
    border-radius: 14px;
    padding: 0.6em 0.75em;
    background: #1f2937;
  }
  .card h4 { margin: 0 0 0.25em; color: #7dd3fc; }
  .good { background:#173126; border-color:#66bb6a; }
  .warn { background:#3a2818; border-color:#ffa726; }
  .bad { background:#3a1f26; border-color:#ef5350; }
  .accent { background:#30203a; border-color:#ba68c8; }
  .pill {
    display: inline-block;
    background: #0288d1;
    color: #fff;
    border-radius: 999px;
    padding: 0.18em 0.7em;
    font-size: 0.75em;
  }
  .flow {
    text-align: center;
    font-family: monospace;
    line-height: 1.5;
  }
  .arrow { color:#38bdf8; font-weight: 800; }
  .source { font-size: 0.55em; color: #94a3b8; }
---

<!-- _class: lead invert -->

# Spring Boot in 30 Minutes

## A visual guide to building Java services

`HTTP` <span class="arrow">→</span> `Controller` <span class="arrow">→</span> `Service` <span class="arrow">→</span> `Repository` <span class="arrow">→</span> `Database`

---

## Our Route

<div class="columns3">

<div class="card">

#### 1 · Why

Spring vs Spring Boot<br>
Build tools

</div>

<div class="card good">

#### 2 · How

IoC · dependency injection<br>
Annotations · layers

</div>

<div class="card warn">

#### 3 · Runtime

Requests · validation<br>
transactions · profiles

</div>

</div>

<br>

<div class="pill">Goal: read an unfamiliar Spring Boot service with confidence</div>

---

## Workshop Toolset

<div class="columns3">

<div class="card">

#### 🌐 Internet

Clone the repository<br>
Pull container images

</div>

<div class="card">

#### 🧑‍💻 VS Code

Editor<br>
Dev Containers extension

</div>

<div class="card">

#### 🐳 Docker Desktop

Runs the application and database containers

</div>

</div>

<br>

<div class="card good flow">

**Image pull provides:** Java 17 + Maven <span class="arrow">│</span> PostgreSQL 16

</div>

> No separate Java or PostgreSQL installation is required.

---

## Why Spring Boot?

<div class="columns3">

<div class="card">

#### Java

Type safety<br>
JVM performance<br>
Mature ecosystem

</div>

<div class="card good">

#### Spring

Dependency injection<br>
Web, data, security<br>
Clear application layers

</div>

<div class="card warn">

#### Boot

Sensible defaults<br>
Fast startup path<br>
Production conventions

</div>

</div>

<br>

**AI can generate code. You still verify the design, boundaries, and behavior.**

<p class="source">Node, Python, and Go have different strengths; choose for the workload and team.</p>

---

## Spring Framework + Spring Boot

<div class="columns">

<div class="card">

#### Spring Framework

`DI` · `MVC` · `Data` · `Transactions`

The programming model and infrastructure.

</div>

<div class="card good">

#### Spring Boot

`Starters` · `Defaults` · `Embedded Server` · `Operations`

The fast path to configuring and running Spring.

</div>

</div>

<br>

<div class="flow">

**Spring concepts** <span class="arrow">+</span> **Boot conventions**
<span class="arrow">→</span> **Runnable application**

</div>

---

## Build Tool = Repeatable Pipeline

<div class="card flow">

`source` <span class="arrow">→</span> `compile` <span class="arrow">→</span> `test` <span class="arrow">→</span> `package` <span class="arrow">→</span> `run`

</div>

<br>

<div class="columns3">

<div class="card good">

#### Maven

`pom.xml`<br>
Convention-driven

</div>

<div class="card">

#### Gradle

`build.gradle(.kts)`<br>
Programmable

</div>

<div class="card">

#### Ant

`build.xml`<br>
Older, task-oriented

</div>

</div>

<p class="source">The XML in pom.xml configures Maven, not the Spring application.</p>

---

## Five Things Boot Gives You

<div class="columns3">

<div class="card">

#### 📦 Starters

Compatible dependency bundles

</div>

<div class="card">

#### ⚙️ Auto-configuration

Defaults based on libraries and settings

</div>

<div class="card">

#### 🌐 Embedded server

Run directly as an application

</div>

<div class="card">

#### 🔧 External config

Properties and environment variables

</div>

<div class="card">

#### 🎛️ Profiles

Different environments, same build

</div>

</div>

---

## Who Should Create the Dependency?

<div class="columns">

<div class="card">

#### Option A

```java
class OrderService {
  private final Repo repo =
      new PostgresRepo();
}
```

</div>

<div class="card">

#### Option B

```java
@Service
class OrderService {
  private final Repo repo;

  OrderService(Repo repo) {
    this.repo = repo;
  }
}
```

</div>

</div>

<div class="pill">Which option is easier to test and change—and why?</div>

---

## Answer: Invert Control

<div class="flow">

`@Repository Repo` <span class="arrow">→</span> `OrderService(Repo)` <span class="arrow">→</span> `@RestController`

</div>

<div class="columns">

<div class="card good">

#### Spring's job

1. Discover components
2. Create objects
3. Connect dependencies
4. Manage lifecycle

</div>

<div class="card">

#### Your class's job

* Declare required dependencies
* Focus on business behavior
* Accept a fake dependency in tests

</div>

</div>

**Constructor injection makes required dependencies explicit and immutable.**

---

## Three Annotations, Three Responsibilities

<div class="columns3">

<div class="card">

#### `@RestController`

HTTP input/output<br>
Validation<br>
Status codes

</div>

<div class="card good">

#### `@Service`

Business rules<br>
Transactions<br>
DTO mapping

</div>

<div class="card bad">

#### `@Repository`

Persistence<br>
Queries<br>
Database abstraction

</div>

</div>

<br>

<div class="flow">

**Controller** <span class="arrow">→</span> **Service** <span class="arrow">→</span> **Repository**

</div>

> Keep API DTOs separate from database entities.

---

## One Request, End to End

<div class="card flow">

`JSON` <span class="arrow">→</span> **Route** <span class="arrow">→</span> **Validate** <span class="arrow">→</span> **Business logic** <span class="arrow">→</span> **JPA** <span class="arrow">→</span> **Database**

</div>

<br>

| Stage | Spring concept |
|---|---|
| Route | `DispatcherServlet` + `@PostMapping` |
| Validate | `@RequestBody` + `@Valid` |
| Business | `@Service` + `@Transactional` |
| Persist | `JpaRepository` + Hibernate |
| Respond | DTO serialized as JSON |

---

## Validation Happens at the Boundary

<div class="columns">

<div class="card bad">

#### Constraint metadata only

```java
create(@RequestBody
       CreateRequest request)
```

Method still receives invalid data.

</div>

<div class="card good">

#### Validation enforced

```java
create(@Valid @RequestBody
       CreateRequest request)
```

Invalid input returns `400` before the service runs.

</div>

</div>

<br>

`curl ... -d '{"name":""}'` <span class="arrow">→</span> `400 Bad Request`

---

## Transaction = One Business Operation

<div class="card flow">

**begin** <span class="arrow">→</span> `load` <span class="arrow">→</span> `change` <span class="arrow">→</span> `save` <span class="arrow">→</span> **commit**

<br>

`runtime exception` <span class="arrow">→</span> **rollback**

</div>

```java
@Transactional
public Order updateOrder(...) {
  // all repository work joins one transaction
}
```

Spring opens a JPA persistence context for the method and obtains a database
connection as needed. This is not a user login session.

---

## Same Code, Different Database

<div class="columns">

<div class="card">

#### Default profile

`H2`

Fast local development<br>
Zero setup

</div>

<div class="card good">

#### Production profile

`PostgreSQL`

`POSTGRES_URL`<br>
`POSTGRES_USER`<br>
`POSTGRES_PASSWORD`

</div>

</div>

<div class="flow">

**profile configuration** <span class="arrow">→</span> **Repository** <span class="arrow">→</span> **unchanged Service**

</div>

---

## Two Lifecycles

<div class="columns">

<div class="card">

#### Spring bean

`construct + inject`

<span class="arrow">↓</span>

`@PostConstruct`

<span class="arrow">↓</span>

`ready`

<span class="arrow">↓</span>

`@PreDestroy`

</div>

<div class="card accent">

#### JPA entity

```java
@PrePersist
void setCreatedAt() {
  createdAt = Instant.now();
}
```

Runs immediately before an `INSERT`.

</div>

</div>

---

## You Write Less Infrastructure

<div class="columns">

<div class="card">

#### You write

DTOs<br>
Business rules<br>
Entities and repositories<br>
Configuration<br>
Tests

</div>

<div class="card accent">

#### Boot provides

Server and routing<br>
JSON conversion<br>
Validation integration<br>
Database plumbing<br>
Transaction management

</div>

</div>

<br>

**Less setup does not mean less responsibility.**

---

## Keep These Five Ideas

<div class="columns3">

<div class="card">

#### 1

Boot configures Spring; it does not replace Spring.

</div>

<div class="card">

#### 2

Constructors make dependencies visible.

</div>

<div class="card">

#### 3

Controllers, services, and repositories have distinct jobs.

</div>

<div class="card">

#### 4

Validation and transactions define boundaries.

</div>

<div class="card">

#### 5

Profiles change infrastructure without changing business code.

</div>

</div>

---

<!-- _class: lead invert -->

# Step 5: Orders

`OrderEntity` uses `@ManyToOne` and `orders.product_id` references
`products.id` because both records belong to this service and database.

```text
POST /api/orders { productId, quantity }
  -> load ProductEntity
  -> save OrderEntity with product foreign key

GET /api/orders/{id}/summary
  -> order + product name + description + unit price + calculated total
```

The entity stores a `ProductEntity` relationship, while the summary DTO exposes
the useful product description instead of the relationship ID.

Keep only a scalar product ID when Product and Order are owned by separate
services with separate databases.

---

<!-- _class: lead invert -->

# Step 6: Dependency Inversion

```text
OrderController -> OrderService -> PaymentService
             ^       ^
             |       |
           UpiService  CardService
            @Primary
```

`OrderService` knows only the interface. Its constructor uses `@Autowired`;
Spring sees both implementations and injects `UpiService` because it is marked
`@Primary`.

Move `@Primary` to `CardService` to change payment behavior without changing
the order code.

---

<!-- _class: lead invert -->

# Continue Learning

<div class="columns">

<div class="card">

#### Advanced topics

* [Testing](https://docs.spring.io/spring-boot/reference/testing/)
* [Security](https://docs.spring.io/spring-security/reference/)
* [Transactions](https://docs.spring.io/spring-framework/reference/data-access/transaction.html)
* [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/)
* [Observability and Actuator](https://docs.spring.io/spring-boot/reference/actuator/)

</div>

<div class="card good">

#### Hands-on workshop

REST · validation · H2 · PostgreSQL · profiles · outbound HTTP

**github.com/neilghosh/springboot-workshop**

[Open the workshop repository](https://github.com/neilghosh/springboot-workshop)

</div>

</div>

<br>

## Questions?
