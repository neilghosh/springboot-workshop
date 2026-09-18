# Spring Boot E-Commerce Workshop Guide

This guide walks through each cumulative checkpoint. Complete the stages in
order.

## 1. Prepare the repository

Complete the setup in [README.md](README.md), then fetch the workshop tags:

```bash
git fetch --tags
```

On Windows PowerShell, use `.\mvnw.cmd` in place of `./mvnw`.

## 2. Navigate checkpoints safely

Workshop tags are read-only snapshots. Stop the running application with
`Ctrl+C` before switching to another tag.

```bash
git switch --detach refs/tags/step-0-starter
```

The detached HEAD message is expected. To return to the maintained version:

```bash
git switch main
```

To experiment without changing a checkpoint, create a branch from it:

```bash
git switch -c my-workshop-change step-3-production
```

## 3. Workshop path

| Tag | Focus | Database |
|---|---|---|
| `step-0-starter` | Spring Boot application and Web MVC | None |
| `step-1-rest-dto` | Product REST API, DTOs, and validation | In-memory Java list |
| `step-2-service-db` | Service, repository, JPA entity, and persistence | H2 |
| `step-3-production` | PostgreSQL profile, tests, and global errors | H2 or PostgreSQL |
| `step-4-outbound-enrichment` | External client and composed response | H2 or PostgreSQL |
| `step-5-order-relationship` | Order creation, JPA relationship, and summary DTO | H2 or PostgreSQL |
| `step-6-dependency-inversion` | Payment interface, `@Primary`, and order processing | H2 or PostgreSQL |

Common commands:

| Task | Unix/macOS | Windows PowerShell |
|---|---|---|
| Run with H2 | `./mvnw spring-boot:run` | `.\mvnw.cmd spring-boot:run` |
| Run tests | `./mvnw clean test` | `.\mvnw.cmd clean test` |
| Run with PostgreSQL | `SPRING_PROFILES_ACTIVE=production ./mvnw spring-boot:run` | `.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=production"` |

API examples use `curl`. The [Bruno collection](bruno/) is available as an
optional graphical client.

Examples assume a fresh H2 database, where generated IDs begin at `1`. When
using PostgreSQL, substitute the IDs returned by creation requests.

### Navigate code in VS Code

After switching tags, press `Ctrl+P` (`Cmd+P` on macOS), type a file name from
the code tour, and press Enter. Use `Ctrl+Shift+O` (`Cmd+Shift+O` on macOS) to
jump to a class method or field. Files linked below open the current workspace
version; switch to the stated tag first so the editor shows that stage's code.

To debug a stage, press `F5`, select **Debug Spring Boot App**, and set a
breakpoint in its controller or service. For PostgreSQL, create `.env`, set
`POSTGRES_PASSWORD`, and select **Debug Spring Boot App (production)**.

## Step 0 - Starter

**Goal:** Understand the minimum structure needed to start Spring Boot.

```bash
git switch --detach refs/tags/step-0-starter
./mvnw spring-boot:run
```

```text
EcommerceApplication.main()
  -> SpringApplication.run()
  -> embedded Tomcat starts on port 8080
```

### Code tour

- [pom.xml](pom.xml) declares the Spring Boot parent and Web starter. Maven uses
  it to download dependencies and package the application.
- [EcommerceApplication.java](src/main/java/com/example/ecommerce/EcommerceApplication.java)
  is the entry point. `@SpringBootApplication` enables component scanning and
  auto-configuration from the package containing this class.

```java
@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
```

In the editor, start at `main`, then use **Go to Definition** on
`SpringApplication.run` to see where control enters Spring Boot.

**Expect:** Tomcat starts on port 8080. `/api/products` returns `404` because no
controller exists yet.

> **Optional experiment - inspect the application context:** Assign the result
> of `SpringApplication.run` to a `ConfigurableApplicationContext`, print
> `context.getBeanDefinitionCount()`, and call
> `context.getBean(Environment.class)`. Observe the infrastructure beans Spring
> creates without an application controller, then remove the temporary code.

## Step 1 - REST and DTO validation

**Goal:** Accept and validate JSON without introducing a database.

```bash
git switch --detach refs/tags/step-1-rest-dto
./mvnw spring-boot:run
```

```text
curl JSON -> ProductController -> DTO validation -> Java list -> response JSON
```

### Code tour

- [ProductController.java](src/main/java/com/example/ecommerce/controller/ProductController.java)
  defines `/api/products`. At this stage it owns an `ArrayList`, assigns IDs,
  and maps request fields into response objects.
- [ProductRequestDTO.java](src/main/java/com/example/ecommerce/dto/ProductRequestDTO.java)
  describes accepted JSON and declares validation rules such as `@NotBlank`,
  `@Positive`, and `@PositiveOrZero`.
- [ProductResponseDTO.java](src/main/java/com/example/ecommerce/dto/ProductResponseDTO.java)
  describes returned JSON, including the generated ID and creation time.

```java
private final List<ProductResponseDTO> products = new ArrayList<>();

@PostMapping
public ResponseEntity<ProductResponseDTO> createProduct(
        @Valid @RequestBody ProductRequestDTO request) {
    // Map, store, and return the product.
}
```

Follow one request in the editor: open `createProduct`, then use **Go to
Definition** on `ProductRequestDTO`. Notice that `@Valid` activates the rules
declared in that DTO before the method body runs.

Create a product on Unix/macOS:

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}'
```

Create a product on Windows PowerShell:

```powershell
'{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}' | curl.exe -i http://localhost:8080/api/products --json '@-'
```

List products:

```bash
curl http://localhost:8080/api/products
```

Try the validation boundary:

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"","description":"Invalid","price":-1,"stockQuantity":-2,"category":""}'
```

**Expect:** Valid creation returns `201`; invalid input returns `400`. Data
disappears when the application stops.

> **Optional experiment - bypass validation:** Remove `@Valid`, repeat the
> invalid request, and observe that the controller accepts it. Restore `@Valid`,
> then remove individual DTO constraints to see which inputs they reject.

## Step 2 - Service, JPA, and H2

**Goal:** Separate HTTP and business logic, then replace the Java list with JPA
persistence.

```bash
git switch --detach refs/tags/step-2-service-db
./mvnw spring-boot:run
```

```text
ProductController -> ProductService -> ProductRepository -> Hibernate -> H2
```

### Code tour

- [ProductController.java](src/main/java/com/example/ecommerce/controller/ProductController.java)
  now handles HTTP concerns and delegates business work to `ProductService`.
- [ProductService.java](src/main/java/com/example/ecommerce/service/ProductService.java)
  owns create, read, update, delete, DTO mapping, and transaction boundaries.
- [ProductEntity.java](src/main/java/com/example/ecommerce/model/ProductEntity.java)
  maps the Java product model to the `products` database table with `@Entity`.
- [ProductRepository.java](src/main/java/com/example/ecommerce/repository/ProductRepository.java)
  extends `JpaRepository`, so Spring Data supplies CRUD operations without a
  handwritten implementation.
- [application.properties](src/main/resources/application.properties) selects
  the in-memory H2 database and enables Hibernate schema updates.

```java
public interface ProductRepository
        extends JpaRepository<ProductEntity, Long> {
}
```

Trace `ProductController.createProduct` through `ProductService.createProduct`
to `productRepository.save`.

Open <http://localhost:8080/h2-console> and connect with:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:ecommercedb` |
| User Name | `sa` |
| Password | Leave blank |

Create a product with the Step 1 request, then run:

```sql
SELECT * FROM products;
```

**Expect:** The API contract remains the same, but Hibernate stores products in
the H2 `products` table. H2 data is cleared when the application stops.

> **Optional experiment - change a JPA mapping:** Add `unique = true` to the
> `name` column's `@Column`, restart, and create two products with the same name.
> Inspect the schema and error, then restore the mapping. Use **Go to
> Definition** on `JpaRepository` to inspect its inherited CRUD methods.

## Step 3 - Production profile and tests

**Goal:** Add PostgreSQL, persistent data, global errors, and automated tests
while retaining H2 as the default.

```bash
git switch --detach refs/tags/step-3-production
```

### Code tour

- [application.properties](src/main/resources/application.properties) remains
  the zero-setup H2 configuration used when no profile is active.
- [application-production.properties](src/main/resources/application-production.properties)
  overrides database settings for PostgreSQL and reads `POSTGRES_URL`,
  `POSTGRES_USER`, and `POSTGRES_PASSWORD` from the environment.
- [GlobalExceptionHandler.java](src/main/java/com/example/ecommerce/exception/GlobalExceptionHandler.java)
  uses `@RestControllerAdvice` to translate validation and missing-resource
  exceptions into consistent HTTP error responses.
- [ProductServiceTest.java](src/test/java/com/example/ecommerce/service/ProductServiceTest.java)
  isolates service behavior with Mockito.
- [ProductControllerIntegrationTest.java](src/test/java/com/example/ecommerce/controller/ProductControllerIntegrationTest.java)
  starts Spring and exercises the HTTP boundary with MockMvc.
- [devcontainer.json](.devcontainer/devcontainer.json) configures the VS Code
  development environment used with the workshop's PostgreSQL service.

```properties
spring.datasource.url=${POSTGRES_URL:jdbc:postgresql://localhost:5432/ecommerce_db}
spring.datasource.username=${POSTGRES_USER:postgres}
spring.datasource.password=${POSTGRES_PASSWORD:secret}
```

Compare the two properties files side by side. Then open
`GlobalExceptionHandler` and find its `@ExceptionHandler` methods to see how a
Java exception becomes an HTTP status and JSON body.

Dev Container users can use the workshop database defaults. To customize them,
copy `.env.example` to `.env`, set `POSTGRES_PASSWORD`, and rebuild the Dev
Container. The PostgreSQL hostname inside Docker Compose is `database`.

Before starting the application, verify that PostgreSQL is reachable and that
the `ecommerce_db` database exists:

```bash
psql -h database -U postgres -d postgres -c "SELECT datname FROM pg_database WHERE datname = 'ecommerce_db';"
```

**Expect:** The command prints one row containing `ecommerce_db`. When running
PostgreSQL directly on the host instead of in the Dev Container, replace
`database` with `localhost`. If no row is returned, create the database with:

```bash
psql -h database -U postgres -d postgres -c "CREATE DATABASE ecommerce_db;"
```

Start the application with the PostgreSQL command in the common commands table.

Connect from another Dev Container terminal:

```bash
psql -h database -U postgres -d ecommerce_db
```

At the `psql` prompt, inspect the schema Hibernate created:

```text
\dn
\dt public.*
\d+ public.products
SELECT * FROM public.products;
```

These commands list schemas and tables and describe the `products` table. Use
`\q` to exit `psql`.

Run the tests:

```bash
./mvnw clean test
```

**Expect:** The production profile uses PostgreSQL, `\dt` lists `products`, data
survives application restarts, and tests finish with `BUILD SUCCESS`.

> **Optional experiment - read a profile property:** Add
> `workshop.label=production` to `application-production.properties` and inject
> it into a Spring-managed class with
> `@Value("${workshop.label:default}")`. Compare the value with and without the
> `production` profile, then remove the temporary field and property.

## Step 4 - Outbound enrichment

**Goal:** Combine a stored product with a price obtained through outbound HTTP.

```bash
git switch --detach refs/tags/step-4-outbound-enrichment
./mvnw spring-boot:run
```

### Code tour

- [ExternalProductClient.java](src/main/java/com/example/ecommerce/client/ExternalProductClient.java)
  owns the outbound call. Spring injects both `RestTemplate` and the configured
  `external.product-url` value through its constructor.
- [RestTemplateConfig.java](src/main/java/com/example/ecommerce/config/RestTemplateConfig.java)
  creates the reusable `RestTemplate` bean and applies proxy settings when
  enabled.
- [ProductSummaryDTO.java](src/main/java/com/example/ecommerce/dto/ProductSummaryDTO.java)
  defines the composed response containing stored and external information.
- [ProductService.java](src/main/java/com/example/ecommerce/service/ProductService.java)
  loads the local product, calls `ExternalProductClient`, and calculates the
  price difference in `getProductSummary`.
- [ProductController.java](src/main/java/com/example/ecommerce/controller/ProductController.java)
  exposes `GET /api/products/{id}/summary`.
- [external-product.json](src/main/resources/static/external-product.json) is
  the offline third-party response used by the default workshop configuration.

```java
public ExternalProductResponse getProduct() {
    return restTemplate.getForObject(
            externalProductUrl, ExternalProductResponse.class);
}
```

Trace `ProductController.getProductSummary` through the service to the client.

To use PostgreSQL instead, run the production-profile command from the common
commands table. Inspect the simulated external API:

```bash
curl http://localhost:8080/external-product.json
```

Create a product with a different stored price:

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":89.99,"stockQuantity":50,"category":"Electronics"}'
```

Use the returned product ID:

```bash
curl http://localhost:8080/api/products/1/summary
```

**Expect:** The response combines the stored product with `livePrice`,
`priceDifference`, `externalSource`, and `externalUrl` from the fixture.

> **Optional experiment - simulate an unavailable dependency:** Set
> `external.product-url` to a missing path, request a summary, and inspect the
> error. Restore the URL, then set a breakpoint in
> `ExternalProductClient.getProduct` to observe the outbound call.

## Step 5 - Product orders

**Goal:** Model a same-database relationship from orders to products and expose
a client-focused summary DTO.

```bash
git switch --detach refs/tags/step-5-order-relationship
./mvnw spring-boot:run
```

### Code tour

- [OrderEntity.java](src/main/java/com/example/ecommerce/model/OrderEntity.java)
  maps each order to one product through the `orders.product_id` foreign key.
  Many order rows may reference the same product row.
- [OrderRepository.java](src/main/java/com/example/ecommerce/repository/OrderRepository.java)
  supplies persistence operations for `OrderEntity`.
- [OrderRequestDTO.java](src/main/java/com/example/ecommerce/dto/OrderRequestDTO.java)
  accepts a product ID and positive quantity from the client.
- [OrderResponseDTO.java](src/main/java/com/example/ecommerce/dto/OrderResponseDTO.java)
  returns the compact result of order creation.
- [OrderSummaryDTO.java](src/main/java/com/example/ecommerce/dto/OrderSummaryDTO.java)
  returns product description and calculated price information instead of
  exposing the entity relationship itself.
- [OrderService.java](src/main/java/com/example/ecommerce/service/OrderService.java)
  resolves the product ID, saves the relationship, and maps entities to DTOs.
- [OrderController.java](src/main/java/com/example/ecommerce/controller/OrderController.java)
  exposes order creation and summary endpoints.
- [OrderControllerIntegrationTest.java](src/test/java/com/example/ecommerce/controller/OrderControllerIntegrationTest.java)
  verifies the persisted relationship and JSON response.

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "product_id", nullable = false)
private ProductEntity product;
```

Compare `OrderEntity`, which stores the relationship, with `OrderSummaryDTO`,
which exposes only client-facing fields.

Create a product using the Step 1 request. Use its returned ID to create an
order:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"quantity":2}'
```

Use the returned order ID:

```bash
curl http://localhost:8080/api/orders/1/summary
```

```json
{
  "orderId": 1,
  "productDescription": "RGB Wireless",
  "productName": "Mechanical Keyboard",
  "quantity": 2,
  "unitPrice": 79.99,
  "totalPrice": 159.98
}
```

**Expect:** `orders.product_id` references `products.id`. The summary DTO
returns a useful description instead of exposing that relationship ID.

> **Optional experiment - test the relationship constraint:** Create an order
> with a nonexistent `productId` and inspect the error, then view
> `orders.product_id` in H2 after a valid order. Change `FetchType.LAZY` to
> `EAGER` and debug a summary request to compare when Hibernate resolves the
> product, then restore `LAZY`.

## Step 6 - Dependency inversion

**Goal:** Make order processing depend on a payment abstraction rather than a
concrete payment method.

```bash
git switch --detach refs/tags/step-6-dependency-inversion
./mvnw spring-boot:run
```

### Code tour

- [PaymentService.java](src/main/java/com/example/ecommerce/payment/PaymentService.java)
  is the abstraction that order processing depends on.
- [UpiService.java](src/main/java/com/example/ecommerce/payment/UpiService.java)
  implements the interface and uses `@Primary`, so Spring selects it when more
  than one `PaymentService` bean exists.
- [CardService.java](src/main/java/com/example/ecommerce/payment/CardService.java)
  is the alternative implementation. It is available to Spring but is not the
  default.
- [OrderService.java](src/main/java/com/example/ecommerce/service/OrderService.java)
  receives `PaymentService` through constructor `@Autowired`; it never imports
  `UpiService` or `CardService`.
- [ProcessOrderResponseDTO.java](src/main/java/com/example/ecommerce/dto/ProcessOrderResponseDTO.java)
  returns only the payment method selected for processing.
- [OrderController.java](src/main/java/com/example/ecommerce/controller/OrderController.java)
  exposes `POST /api/orders/{id}/process`.
- [OrderServiceTest.java](src/test/java/com/example/ecommerce/service/OrderServiceTest.java)
  replaces the payment interface with a Mockito mock, demonstrating that the
  service does not require either concrete implementation during testing.

```java
@Autowired
public OrderService(
        OrderRepository orderRepository,
        ProductRepository productRepository,
        PaymentService paymentService) {
    this.paymentService = paymentService;
}
```

Use **Find All References** on `PaymentService` to follow both implementations
and its injection into `OrderService`.

```text
OrderController -> OrderService -> PaymentService <- UpiService (@Primary)
                                               <- CardService
```

Create a product and order using the earlier requests, then process the order:

```bash
curl -X POST http://localhost:8080/api/orders/1/process
```

```json
{
  "paymentMethod": "UPI"
}
```

**Expect:** Constructor `@Autowired` requests a `PaymentService`. Spring injects
`UpiService` because it has `@Primary`. Moving `@Primary` to `CardService`
changes the implementation without changing `OrderService`.

> **Optional experiment - make bean selection ambiguous:** Temporarily remove
> `@Primary` from `UpiService` and observe the ambiguous-bean startup error.
> Restore it, then move it to `CardService` and process an order.
>
> To override `@Primary` at one injection point, add
> `@Qualifier("cardService")` to the `PaymentService` constructor parameter in
> `OrderService`:
>
> ```java
> public OrderService(
>         OrderRepository orderRepository,
>         ProductRepository productRepository,
>         @Qualifier("cardService") PaymentService paymentService) {
>     // Keep the existing constructor body.
> }
> ```
>
> Restart and expect `"paymentMethod": "CARD"`, then remove the qualifier and
> its import.

## Troubleshooting

### Dev Container reports a missing Wayland socket

The workshop does not use Linux GUI applications. Open VS Code **User Settings
(JSON)**, add the following setting, and rebuild the Dev Container:

```json
"dev.containers.mountWaylandSocket": false
```

### macOS reports `docker-credential-desktop` not found

Open Docker Desktop settings and configure or reinstall the Docker CLI tools,
then restart VS Code. Verify the helper and container image:

```bash
command -v docker-credential-desktop
docker pull mcr.microsoft.com/devcontainers/java:1-17-bookworm
```

### H2 reports `/home/vscode/test` not found

Replace the H2 Console default URL with `jdbc:h2:mem:ecommercedb`. Use user
`sa` and leave the password blank.

### PostgreSQL authentication fails

Confirm `.env` uses matching values:

```ini
POSTGRES_DB=ecommerce_db
POSTGRES_URL=jdbc:postgresql://database:5432/ecommerce_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
```

Never commit `.env`.

### Port 8080 is already in use

Stop the existing process or use another port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```