---
marp: true
theme: gaia
class: invert
paginate: true
header: "Spring Boot Concepts"
footer: "Spring Boot Concepts"
size: 16:9
style: |
  section {
    --ink: #f2f5f5;
    --muted: #c4d3d7;
    --cyan: #6eced4;
    --green: #90d9b1;
    --amber: #f6c46b;
    --red: #ffabab;
    box-sizing: border-box;
    padding: 96px 42px 88px;
    font-size: 24px;
    line-height: 1.4;
    letter-spacing: 0;
    background: #1b272b;
    color: var(--ink);
  }
  header { top: 18px; left: 42px; right: 42px; line-height: 1.2; color: var(--muted); padding-bottom: 6px; border-bottom: 1px solid rgba(127, 127, 127, 0.35); }
  footer { bottom: 16px; left: 42px; right: 82px; line-height: 1.2; color: var(--muted); padding-top: 6px; border-top: 1px solid rgba(127, 127, 127, 0.35); }
  section::after { right: 42px; bottom: 16px; }
  h1, h2, h3, h4 { letter-spacing: 0; }
  h1 { font-size: 1.65em; }
  section.lead h1 { font-size: 2.2em; }
  h2 { font-size: 1.3em; margin: 0 0 22px; }
  h3 { font-size: 1.05em; }
  h4 { margin: 0 0 12px; font-size: 1em; color: var(--cyan); }
  p { margin: 0.65em 0; }
  a { color: var(--cyan); }
  table { font-size: 0.82em; }
  th, td { padding: 8px 12px; }
  th { background: #30454c; color: var(--ink); }
  td { background: #223238; color: var(--ink); }
  code { font-size: 0.86em; }
  pre { font-size: 19px; line-height: 1.4; padding: 16px 20px; }
  pre code { font-size: inherit; }
  .columns { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 32px; }
  .panel { border-left: 3px solid var(--cyan); padding-left: 20px; }
  .panel.good { border-color: var(--green); }
  .node { border: 2px solid var(--cyan); border-radius: 6px; padding: 16px; min-height: 104px; display: flex; flex-direction: column; justify-content: center; text-align: center; background: #223238; }
  .node strong { font-size: 1em; }
  .node small { display: block; margin-top: 8px; font-size: 0.78em; color: var(--muted); }
  .node.good { border-color: var(--green); }
  .node.warn { border-color: var(--amber); }
  .node.bad { border-color: var(--red); }
  .diagram { display: grid; gap: 8px; align-items: center; margin: 24px 0; }
  .diagram.three { grid-template-columns: repeat(2, minmax(0, 1fr) 44px) minmax(0, 1fr); }
  .diagram.four { grid-template-columns: repeat(3, minmax(0, 1fr) 40px) minmax(0, 1fr); }
  .diagram.five { grid-template-columns: repeat(4, minmax(0, 1fr) 32px) minmax(0, 1fr); }
  .arrow, .connector { color: var(--cyan); font-weight: 700; }
  .connector { text-align: center; font-size: 36px; }
  .return-path { color: var(--green); text-align: center; font-size: 20px; }
  .minor { font-size: 0.8em; color: var(--muted); }
  .route { display: grid; grid-template-columns: repeat(5, minmax(0, 1fr)); gap: 20px; list-style: none; padding: 0; margin: 32px 0; }
  .route li { border-top: 3px solid var(--cyan); padding-top: 12px; font-size: 23px; }
  .route strong { display: block; font-size: 34px; color: var(--cyan); margin-bottom: 12px; }
  .merge { display: grid; grid-template-columns: minmax(0, 1fr) 60px minmax(0, 1fr); gap: 20px; align-items: center; margin: 24px 0; }
  .sources { display: grid; gap: 16px; }
---

<!-- _class: lead invert -->

# Spring Boot Concepts

## How the pieces work together

**Request** <span class="arrow">→</span> **Java code** <span class="arrow">→</span> **Response**

<!--
Assume basic Java knowledge, but no prior knowledge of Spring or an example app.
All small code illustrations are independent teaching examples, with imports
omitted. No application needs to be running to follow this presentation.
Ask: what happens between a request reaching the server and JSON coming back?
-->

---

## Five Questions We'll Answer

<ol class="route">
  <li><strong>1</strong>What does Boot do for me?</li>
  <li><strong>2</strong>Who creates and connects objects?</li>
  <li><strong>3</strong>What happens during an object's life?</li>
  <li><strong>4</strong>How does a request get answered?</li>
  <li><strong>5</strong>How do data, settings, and tests fit?</li>
</ol>

Start with what happens. Then give it a name.

<!--
Do not open with an acronym glossary. Introduce dependency injection, DTO,
IoC, JPA, and profiles after the audience has seen the problem each addresses.
-->

---

## Start with One Request

<div class="diagram three">
  <div class="node"><strong>Ask for a greeting</strong><small><code>GET /hello</code></small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>A Java method runs</strong><small><code>hello()</code></small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Send back a reply</strong><small>Plain text: Hello!</small></div>
</div>

```java
@RestController
class HelloController {
  @GetMapping("/hello")
  public String hello() {
    return "Hello!";
  }
}
```

`@GetMapping` connects a URL to a method. Spring sends its return value back.

<!--
This standalone illustration supplies the entire endpoint logic; no existing
API or stored data is assumed. Imports and an application entry point are
omitted here. Put the controller under the application package when running
it in a separate demo. A String returns text; a response object can become JSON.
-->

---

## Spring Connects. Boot Sets Up.

<div class="columns">
<div class="panel">

#### Spring

Creates and connects the objects you ask it to manage.

Provides support for web requests and transactions.

</div>
<div class="panel good">

#### Spring Boot

Sets up Spring based on your libraries and settings.

Starts a built-in server for a web application.

</div>
</div>

**Boot reduces setup. Your code still decides what the application does.**

<!--
Auto-configuration means conditional defaults, not code generation or magic.
A starter bundles compatible dependencies for a task, such as building a web
API. Spring Data is a related project, not something to conflate with Boot.
-->

---

## From main() to a Running Server

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    }
}
```

<div class="diagram three">
  <div class="node"><strong>Read the setup</strong><small>Libraries + settings</small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Prepare the app</strong><small>Create and connect objects</small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Accept requests</strong><small>Built-in Tomcat server</small></div>
</div>

No separate Tomcat installation. No manual server deployment.

<!--
This is a mental model, not an exact list of internal startup callbacks.
@SpringBootApplication combines configuration, auto-configuration, and scanning
the application package and its subpackages. Object lifecycle comes next.
Boot does not invent endpoints: a controller still has to declare them.
-->

---

## Maven Builds. Boot Runs.

<div class="diagram four">
  <div class="node"><strong>Java source</strong></div>
  <div class="connector">→</div>
  <div class="node"><strong>Compile</strong></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Test</strong></div>
  <div class="connector">→</div>
  <div class="node"><strong>Package</strong><small>Runnable JAR</small></div>
</div>

| I want to... | Command |
|---|---|
| Run the app | `./mvnw spring-boot:run` |
| Run the tests | `./mvnw clean test` |
| Build the JAR | `./mvnw package` |

`pom.xml` lists libraries and build settings. It is not Spring application XML.

<p class="minor">Windows PowerShell: replace <code>./mvnw</code> with <code>.\mvnw.cmd</code>.</p>

<!--
The Maven Wrapper selects Maven; a separate Maven install is unnecessary.
The Boot Maven plugin packages the executable JAR. Skip comparisons with Ant
and Gradle: the distinction that matters here is building versus running.
-->

---

## IoC: Who Is in Control?

<div class="columns">
<div>

#### You run the setup

<div class="node"><strong>Your startup code</strong><small>Creates every object with <code>new</code><br>Connects the objects itself</small></div>
<div class="connector">↓</div>
<div class="node"><strong>Objects ready to use</strong><small>You also arrange their cleanup</small></div>

</div>
<div>

#### Spring runs the setup

<div class="node good"><strong>Spring's container</strong><small>Reads your class declarations<br>Creates and connects the objects</small></div>
<div class="connector">↓</div>
<div class="node good"><strong>Objects ready to use</strong><small>Spring manages their lifecycle</small></div>

</div>
</div>

**Inversion of Control (IoC):** hand control of object setup to the framework.

<!--
The inversion is who drives setup, not reversing the direction of a data flow.
Your classes declare what they need; Spring coordinates their creation and use.
IoC is a broader framework idea. Here we focus on object creation, connection,
and lifecycle. Business decisions still belong in your code.
-->

---

## What Is a Bean?

A **bean** is an ordinary Java object managed by Spring.

<div class="columns">
<div class="panel good">

#### Inside Spring's container

<div class="node good"><strong>A registered object</strong><small>Spring knows how to create it,<br>connect it, and manage its lifecycle.</small></div>

It can be supplied to another bean.

</div>
<div class="panel">

#### Outside the container

<div class="node warn"><strong>An object you create yourself</strong><small><code>new Helper()</code><br>No automatic Spring management</small></div>

It is not automatically a bean.

</div>
</div>

**ApplicationContext** is the name of Spring's container, not a Docker container.

<!--
The container lives inside the Java application and tracks bean definitions
and instances. A class is a blueprint; a bean is a managed object, not a file.
A factory method can use new and return that object for Spring to manage.
Do not say that any use of new prevents an object from becoming a bean.
-->

---

## Two Ways to Register a Bean

<div class="columns">
<div class="panel">

#### Mark a class you own

```java
@Component
class GreetingService {
}
```

Spring finds the class while scanning your application package.

</div>
<div class="panel good">

#### Describe how to create an object

```java
@Configuration
class TimeConfig {
  @Bean
  Clock appClock() {
    return Clock.systemUTC();
  }
}
```

Useful for a library class you cannot annotate.

</div>
</div>

`@Component` marks a class. `@Bean` marks a factory method. Both register beans.

<!--
Clock is java.time.Clock from the Java standard library. These independent
snippets omit package and import statements. @Service and @RestController
are specialized component annotations. Keep configuration in the packages
scanned by @SpringBootApplication, or register it explicitly.
The next slide expands GreetingService to request the Clock bean.
-->

---

## Dependency Injection: Pass In the Helper

<div class="columns">
<div>

```java
@Component
class GreetingService {
  private final Clock clock;

  GreetingService(Clock clock) {
    this.clock = clock;
  }
}
```

</div>
<div>
  <div class="node good"><strong>Spring finds the Clock bean</strong><small>Created by <code>appClock()</code></small></div>
  <div class="connector">↓</div>
  <div class="node"><strong>Passes it into the constructor</strong><small><code>GreetingService(clock)</code></small></div>
</div>
</div>

**Dependency injection (DI)** = receiving the helper objects you need.

IoC says **who runs the setup**. DI says **how the helpers arrive**.

<!--
A dependency is another object the class needs. The receiving class does not
choose how to create the Clock. With one constructor, @Autowired is optional.
This example assumes a single Clock bean; selection between several candidates
can be a follow-up question. A test can pass a fixed Clock without Spring.
-->

---

## Bean Lifecycle: From Creation to Cleanup

**Lifecycle** = what happens to an object from creation until cleanup.

<div class="diagram four">
  <div class="node"><strong>Create + connect</strong><small>Constructor runs<br>Helpers are supplied</small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Initialize</strong><small><code>@PostConstruct</code><br>Prepare for use</small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Use</strong><small>Ready to do work<br>Methods may run many times</small></div>
  <div class="connector">→</div>
  <div class="node warn"><strong>Clean up</strong><small><code>@PreDestroy</code><br>Release resources</small></div>
</div>

<div class="columns">
<div class="panel">

#### At startup, by default

Spring creates and initializes the bean.

</div>
<div class="panel good">

#### When the container closes normally

Spring calls its cleanup method.

</div>
</div>

Constructor injection happens during creation, **before** `@PostConstruct`.

<!--
This is the simplified lifecycle of a normal singleton bean, not a JPA entity.
With field or setter injection, dependencies are populated after construction;
initialization callbacks still run after injection. Omit post-processor and
proxy internals from this first explanation. Lazy beans start at first use.
Cleanup is not guaranteed on a forced process kill; Spring does not automatically
run destruction callbacks for prototype-scoped objects.
-->

---

## Lifecycle Hooks Run at Specific Moments

<div class="columns">
<div>

```java
@Component
class AppResources {
  @PostConstruct
  void prepare() {
    System.out.println("Bean initialized");
  }

  @PreDestroy
  void cleanup() {
    System.out.println("Bean cleaned up");
  }
}
```

</div>
<div class="sources">
  <div class="node"><strong>After dependencies are supplied</strong><small><code>prepare()</code> runs once for this instance</small></div>
  <div class="node good"><strong>While the application runs</strong><small>Normal methods handle the work<br>Initialization does not repeat for each request</small></div>
  <div class="node warn"><strong>During normal shutdown</strong><small><code>cleanup()</code> runs once for this instance</small></div>
</div>
</div>

<!--
Use jakarta.annotation.PostConstruct and jakarta.annotation.PreDestroy.
The printed messages make callback timing visible without extra infrastructure.
In real code these hooks can prepare a resource and close it later. Do not
claim that this illustrative class handles HTTP requests or opens a connection.
If initialization fails, the bean is not ready for normal use.
-->

---

## One Shared Bean, Many Requests

<div class="merge">
  <div class="sources">
    <div class="node"><strong>Request A</strong><small>A method call</small></div>
    <div class="node"><strong>Request B</strong><small>Another method call</small></div>
  </div>
  <div class="connector">→</div>
  <div class="node good"><strong>The same service bean</strong><small>Created and initialized once<br>Reused for both requests</small></div>
</div>

The default scope is **singleton**: one instance per bean definition, per container.

**Keep request-specific data out of shared fields.** Calls can arrive together.

<!--
Singleton here does not mean one object across all servers or across every
ApplicationContext. There is no new service instance or @PostConstruct call
for every HTTP request. Discuss request or prototype scope only if asked;
their creation and cleanup rules differ from this default.
-->

---

## When a Request Needs Stored Data

<div class="diagram four">
  <div class="node"><strong>Handle HTTP</strong><small>Controller<br><code>@RestController</code></small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Apply the rules</strong><small>Service<br><code>@Service</code></small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Read or save</strong><small>Repository<br><code>JpaRepository</code></small></div>
  <div class="connector">→</div>
  <div class="node warn"><strong>Store rows</strong><small>Database<br>Persistent storage</small></div>
</div>

<p class="return-path">← The result comes back; Spring turns the response object into JSON.</p>

Spring calls methods on ready beans. A request does not recreate those beans.

**Each part has one job.** A simple greeting endpoint needs fewer parts.

<!--
Point along the arrows, then trace the result back. @GetMapping and @PostMapping
connect HTTP methods and paths to controller methods. The framework handles
routing and JSON conversion. Name DispatcherServlet only if someone asks.
Contrast this request flow with the bean lifecycle: method calls can happen
many times between initialization and cleanup. Do not add layers without need.
-->

---

## Check Input Before Doing Work

<div class="columns">
<div>

#### Declare the rules on the input class

```java
@NotBlank
private String name;

@Email
private String email;
```

`@Valid` on the controller input activates these rules.

</div>
<div class="sources">
  <div class="node bad"><strong>Blank name or malformed email</strong><small>400 Bad Request<br>The controller method does not run.</small></div>
  <div class="node good"><strong>Valid input</strong><small>Continue with the requested operation</small></div>
</div>
</div>

<!--
This is an illustrative input-class field excerpt; accessors are omitted.
The validation dependency must be present. @Valid beside @RequestBody activates
these field rules. @Email checks a supplied value; add @NotBlank when the email
must be present. A DTO describes data sent into or out of the API.
Ask what happens if @Valid is removed, then return to the two outcomes.
-->

---

## API Data and Database Data Have Different Jobs

<div class="diagram three">
  <div class="node"><strong>What the client sends</strong><small>A request DTO<br>For example: name and email</small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Copy the allowed fields</strong><small>Your service code</small></div>
  <div class="connector">→</div>
  <div class="node"><strong>What the database stores</strong><small>An entity<br>ID and saved fields</small></div>
</div>

<div class="columns">
<div class="panel">

#### DTO: the API's data shape

Choose what callers may send and what they get back.

</div>
<div class="panel good">

#### Entity: a Java object mapped to a table

`@Entity` marks the class. `@Id` marks its key.

</div>
</div>

Choose response fields deliberately. Keep internal database details private.

<!--
DTO expands to Data Transfer Object; the practical meaning matters more.
Request and response DTOs can have different fields. A service maps those
fields explicitly. A JPA entity is not a Spring-managed service bean; its
persistence lifecycle is different from the bean lifecycle explained earlier.
-->

---

## Let the Repository Handle Saving

<div class="diagram four">
  <div class="node good"><strong>Service</strong><small><code>save(entity)</code></small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Repository</strong><small>Spring Data supplies it</small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Hibernate</strong><small>Turns mapped objects into SQL</small></div>
  <div class="connector">→</div>
  <div class="node warn"><strong>Database</strong><small>Stores the row</small></div>
</div>

<div class="columns">
<div class="panel">

#### JPA: the mapping contract

Defines how Java objects map to database tables.

</div>
<div class="panel good">

#### Hibernate: an implementation

Uses those mappings to perform database work.

</div>
</div>

Spring Data JPA supplies methods such as `save`, `findById`, and `findAll`.

<!--
Spring Data JPA creates an implementation for a repository interface extending
JpaRepository. Hibernate performs the database work; JPA is not a database.
Avoid promising that every query needs no SQL knowledge, or that save always
performs an INSERT immediately. A flush or commit may happen later.
-->

---

## A Transaction: Save Together, or Undo Together

`@Transactional` groups the database work in a service operation.

<div class="diagram four">
  <div class="node"><strong>Begin</strong></div>
  <div class="connector">→</div>
  <div class="node"><strong>Account A</strong><small>Subtract 100</small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Account B</strong><small>Add 100</small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Commit</strong><small>Keep the changes</small></div>
</div>

<div class="diagram three">
  <div class="node bad"><strong>A runtime exception escapes</strong><small>Before the operation completes</small></div>
  <div class="connector">→</div>
  <div class="node bad"><strong>Roll back</strong><small>Undo this transaction's database changes</small></div>
  <div class="connector">→</div>
  <div class="node"><strong>No partial update</strong></div>
</div>

A transfer between two database accounts must not stop halfway.

<!--
Default rollback applies to unchecked exceptions and errors that escape the
transactional operation, not every checked or caught exception. Explain this
qualification if asked. This does not undo HTTP calls or external payments.
Skip persistence-context and proxy internals in the main talk.
-->

---

## Turn Failures into Useful Responses

| What happened? | What the caller receives |
|---|---|
| A required field is blank | `400 Bad Request` with field messages |
| The requested record does not exist | `404 Not Found` with an explanation |
| An unexpected server failure occurs | `500 Internal Server Error` |

<div class="diagram three">
  <div class="node bad"><strong>Something fails</strong><small>Invalid input or missing data</small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Shared error handler</strong><small><code>@RestControllerAdvice</code></small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Useful HTTP response</strong><small>Status + explanation</small></div>
</div>

Keep error formatting in one place instead of repeating it in every controller.

<!--
This is a design sketch for an API, not a demonstration of an existing error
handler. Map a specific missing-record exception to 404, not every runtime
exception. Keep internal details out of server-error responses. The status
and useful explanation matter more here than memorizing annotation names.
-->

---

## Same Code, Different Settings

A **profile** is a named group of settings. Your Java code stays the same.

<div class="diagram three">
  <div class="node"><strong>Default settings</strong><small><code>application.properties</code><br>Example port: 8080</small></div>
  <div class="connector">→</div>
  <div class="node warn"><strong>Active profile overrides</strong><small><code>application-local.properties</code><br>Example port: 9090</small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Effective setting</strong><small>The app uses port 9090</small></div>
</div>

Select this example profile with `SPRING_PROFILES_ACTIVE=local`.

Environment variables can override file settings. Keep secrets out of source code.

<!--
These are illustrative files, not prerequisites or changes to any existing
application. The property in each file is server.port. When local is active,
its value overrides the default. With no active profile in this illustration,
the port remains 8080. Other sources, including environment variables and
command-line arguments, can override configuration files.
-->

---

## Test the Rules. Then Test the Request.

<div class="columns">
<div class="panel">

#### Does the service behave correctly?

Give it fake helpers. Check the returned data and decisions.

<div class="node"><strong>Class under test + controlled helper</strong><small>A fixed <code>Clock</code> makes time predictable.</small></div>

</div>
<div class="panel good">

#### Does the HTTP endpoint behave correctly?

Send a request through Spring. Check validation, status, and JSON.

<div class="node good"><strong>Request → controller → response</strong><small><code>MockMvc</code> exercises the HTTP layer.</small></div>

</div>
</div>

`./mvnw clean test` · Windows: `.\mvnw.cmd clean test`

<!--
Connect fake helpers back to constructor injection. A service unit test does
not need to start Spring. The illustrated GreetingService can receive a fixed
Clock in a test; Mockito can supply other kinds of fake helpers. MockMvc does
not need a separately running server. Green tests cover the scenarios tested,
not every possible failure. Choose a test database only when testing data access.
-->

---

## One Picture to Remember

<div class="diagram four">
  <div class="node"><strong>Create + connect</strong><small>Spring supplies the helpers</small></div>
  <div class="connector">→</div>
  <div class="node"><strong>Initialize</strong><small>Prepare the bean</small></div>
  <div class="connector">→</div>
  <div class="node good"><strong>Use</strong><small>Handle many calls</small></div>
  <div class="connector">→</div>
  <div class="node warn"><strong>Clean up</strong><small>Container closes</small></div>
</div>

**IoC:** Spring runs the setup. **DI:** it passes in the required helpers.

**Beans** are managed objects. **Lifecycle** is their creation, use, and cleanup.

**Requests** call methods on ready beans; they do not restart that lifecycle.

**Boot** configures Spring using your libraries and settings.

<!--
Ask the audience to explain the arrows in their own words. Who creates an
object? How does its Clock arrive? Does a second request run @PostConstruct
again? When can @PreDestroy run? Use the answers to find what needs another
picture. This recap depicts the default singleton lifecycle.
-->

---

<!-- _class: lead invert -->

# Questions?

[Spring Boot documentation](https://docs.spring.io/spring-boot/)

[Spring's container and beans](https://docs.spring.io/spring-framework/reference/core/beans.html)

[Bean lifecycle callbacks](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html)

<!--
Revisit the picture that answers the question before introducing more terms.
-->
