# Spring Boot 4 E-Commerce API Workshop

> **Format:** 3 hours · **Audience:** 2nd–4th year engineering students
> Build a REST API progressively with validation, service-layer design, JPA,
> H2, PostgreSQL, testing, profiles, error handling, and outbound HTTP.

Instructor materials are kept separately in [`docs/`](./docs/README.md).

## Workshop path

Each completed stage is a Git tag:

| Tag | Focus | Database |
|---|---|---|
| `step-0-starter` | Spring Boot application and Web MVC | None |
| `step-1-rest-dto` | Product REST API, DTOs, and validation | In-memory Java list |
| `step-2-service-db` | Service, repository, JPA entity, and persistence | H2 |
| `step-3-production` | PostgreSQL profile, tests, and global errors | H2 or PostgreSQL |
| `step-4-outbound-enrichment` | External client and composed response | H2 or PostgreSQL |

Tags are read-only checkpoints. Move between them with:

```bash
git switch --detach step-0-starter
git switch --detach step-1-rest-dto
git switch --detach step-2-service-db
git switch --detach step-3-production
git switch --detach step-4-outbound-enrichment
```

Return to the maintained version with:

```bash
git switch main
```

To change a checkpoint, create a temporary branch from its tag:

```bash
git switch -c my-workshop-change step-3-production
```

## Shared setup

- Choose either **Dev Container** or **Local**.
- Use the repository's Maven Wrapper (`./mvnw` or `.\mvnw.cmd`); do not install
  Maven separately.
- Participant-facing examples use `curl`. Optional: use the [`bruno`](./bruno)
  collection or another graphical API client.
- Press `Ctrl+C` before moving to another checkpoint so the next stage can use
  port 8080.

### Tools

| Setup | Required tools |
|---|---|
| Dev Container | Docker, [VS Code](https://code.visualstudio.com/), VS Code Dev Containers extension |
| Local | [JDK 17](https://learn.microsoft.com/en-us/java/openjdk/download/), PostgreSQL 16 for the production profile |

### Dev Container

- Windows Docker install:

```powershell
winget install --exact --id Docker.DockerDesktop --accept-package-agreements --accept-source-agreements
```

- Linux Docker install (Ubuntu):

```bash
sudo apt update && sudo apt install -y curl && curl -fsSL https://get.docker.com | sudo sh && sudo usermod -aG docker "$USER"
```

- Sign out and back in after the Linux Docker command so group membership
  takes effect.
- Open the repository in VS Code and run **Dev Containers: Rebuild and Reopen
  in Container**.
- The container supplies Java 17, Copilot CLI, and PostgreSQL 16 starting at
  step 3.
- Rebuild the Dev Container after switching checkpoints when VS Code prompts
  you.

### Local tools

- Windows PowerShell Java and PostgreSQL install:

```powershell
winget install --exact --id Microsoft.OpenJDK.17 --accept-package-agreements --accept-source-agreements; winget install --exact --id PostgreSQL.PostgreSQL.16 --accept-package-agreements --accept-source-agreements
```

- Linux Java and PostgreSQL install (Ubuntu 24.04 LTS):

```bash
sudo apt update && sudo apt install -y openjdk-17-jdk postgresql-16
```

- PostgreSQL is only needed when the production profile is introduced in
  step 3; the default profile uses H2.

## Step 0 — Starter

**Goal:** Understand the minimum structure needed to start Spring Boot.

**Flow:**

```text
EcommerceApplication.main()
  -> SpringApplication.run()
  -> embedded Tomcat starts on port 8080
```

**Do:**

```bash
git switch --detach step-0-starter
./mvnw spring-boot:run
```

**Expect:** The application starts successfully and logs that Tomcat is
listening on port 8080. A request to `/api/products` returns `404` because no
controller exists yet.

## Step 1 — REST and DTO validation

**Goal:** Accept JSON requests, validate them, and return product responses
without introducing a database.

**Flow:**

```text
curl JSON
  -> ProductController
  -> ProductRequestDTO validation
  -> in-memory Java list
  -> ProductResponseDTO JSON
```

APIs introduced in this step:

| Method | Endpoint | Behavior |
|---|---|---|
| `GET` | `/api/products` | List products held in memory |
| `POST` | `/api/products` | Validate and create an in-memory product |

**Do:**

```bash
git switch --detach step-1-rest-dto
./mvnw spring-boot:run
```

Products are temporarily stored in a Java list. Create and list products.

**Unix/macOS:**

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}'

curl http://localhost:8080/api/products
```

**Windows PowerShell:**

```powershell
curl.exe -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}'

curl.exe http://localhost:8080/api/products
```

**Expect:** `POST` returns `201 Created` with an assigned ID, and `GET` returns
the product from the in-memory list. Removing `name` or using a negative `price`
returns `400 Bad Request`. Data disappears when the application stops.

Try the validation boundary explicitly:

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"","description":"Invalid example","price":-1,"stockQuantity":-2,"category":""}'
```

The request must fail before the controller calls the service. Without `@Valid`
on the controller parameter, the DTO annotations still exist but Spring MVC
does not enforce them at that boundary.

## Step 2 — Service, JPA, and H2

**Goal:** Separate HTTP handling from business logic and replace the Java list
with database persistence.

**Flow:**

```text
ProductController
  -> ProductService
  -> ProductRepository
  -> Hibernate/JPA
  -> H2 in-memory database
```

The same REST contract now uses H2, with additional CRUD operations:

| Method | Endpoint | H2 operation |
|---|---|---|
| `GET` | `/api/products` | Select all products |
| `GET` | `/api/products/{id}` | Select one product by ID |
| `POST` | `/api/products` | Insert a product |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |

**Do:**

```bash
git switch --detach step-2-service-db
./mvnw spring-boot:run
```

Open <http://localhost:8080/h2-console>:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:ecommercedb` |
| User Name | `sa` |
| Password | Leave blank |

Create a product using the Step 1 `POST`, then run this query in the H2 Console:

```sql
SELECT * FROM products;
```

**Expect:** The API response remains the same, but the row is now stored in the
`products` table. Hibernate creates the table automatically. H2 data is still
cleared when the application stops.

## Step 3 — Production profile and tests

**Goal:** Keep H2 convenient for local work while adding explicit PostgreSQL
configuration, persistent data, error handling, and automated tests.

**Flow:**

```text
SPRING_PROFILES_ACTIVE=production
  -> application-production.properties
  -> JDBC connection to database:5432
  -> PostgreSQL products table
```

**Do:**

```bash
git switch --detach step-3-production
```

Dev Container and Codespaces users can start this stage without creating
`.env`; Docker Compose uses workshop defaults. To customize PostgreSQL
credentials, copy `.env.example` to `.env`, set `POSTGRES_PASSWORD`, then
rebuild the Dev Container.

Docker Compose starts:

| Service | Purpose |
|---|---|
| `app` | Java environment and VS Code terminal |
| `database` | PostgreSQL 16 server |

VS Code enters `app` because `devcontainer.json` specifies `"service": "app"`.
The PostgreSQL hostname inside the Compose network is `database`.

Run this stage with PostgreSQL:

```bash
SPRING_PROFILES_ACTIVE=production ./mvnw spring-boot:run
```

To use local H2 instead, omit the production profile:

```bash
./mvnw spring-boot:run
```

The production profile disables the H2 Console. Connect to PostgreSQL from
another Dev Container terminal:

```bash
psql -h database -U postgres -d ecommerce_db
```

Run the tests:

```bash
./mvnw clean test
```

**Expect:** The startup log reports the `production` profile, PostgreSQL JDBC
driver, and PostgreSQL 16. In `psql`, `\dt` lists the `products` table and data
survives application restarts. Tests finish with `BUILD SUCCESS` using Mockito
service tests and MockMvc controller integration tests.

## Step 4 — Outbound enrichment

**Goal:** Compose local product data with a price obtained through an outbound
HTTP call.

New API in this step:

| Method | Endpoint | Behavior |
|---|---|---|
| `GET` | `/api/products/{id}/summary` | Combine the stored product with an external live price |

**Do:**

```bash
git switch --detach step-4-outbound-enrichment
```

This stage adds `RestTemplate`, proxy-ready client configuration, and a composed
product summary. To keep the workshop offline, `/external-product.json`
simulates a third-party product API. In a real integration,
`external.product-url` would point to another service.

**Flow:**

```text
curl GET /api/products/{id}/summary
  -> ProductController
  -> ProductService loads the requested product from PostgreSQL
  -> ExternalProductClient
  -> RestTemplate GET /external-product.json
  <- external live price
  -> ProductService combines both responses
```

**Run and observe:**

Stop any application started before switching tags, then start Step 4 so the JVM
loads the outbound client code:

```bash
SPRING_PROFILES_ACTIVE=production ./mvnw spring-boot:run
```

In another terminal, first inspect the simulated external response:

```bash
curl http://localhost:8080/external-product.json
```

Create a local product with a different price:

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":89.99,"stockQuantity":50,"category":"Electronics"}'
```

Copy the `id` from the POST response. PostgreSQL data persists across restarts,
so the new product is not always ID `1`. Request the composed response with the
returned ID:

```bash
PRODUCT_ID=2 # replace with the ID returned by POST
curl "http://localhost:8080/api/products/$PRODUCT_ID/summary"
```

The response makes the external value explicit:

```json
{
  "product": {
    "price": 89.99
  },
  "livePrice": 79.99,
  "priceDifference": 10.0,
  "externalSource": "local external-product.json fixture",
  "externalUrl": "http://localhost:8080/external-product.json"
}
```

Open `externalUrl` directly to inspect the payload used as the live price. The
application log also shows the outbound request:

```text
Calling external product API: http://localhost:8080/external-product.json
```

**Expect:** The response contains the stored product, `livePrice` from the
outbound JSON, the calculated `priceDifference`, and the URL of the external
payload.

## Debug

Press `F5` and select **Debug Spring Boot App**. A breakpoint in
`ProductController` or `ProductService` is hit by the next API request.

To debug against PostgreSQL, create `.env` from `.env.example`, set
`POSTGRES_PASSWORD`, then press `F5` and select
**Debug Spring Boot App (production)**. That VS Code launch profile loads
`.env` and activates the `production` Spring profile.

## Troubleshooting

<details>
<summary><b>Windows: accessing specified distro mount service</b></summary>

This failure happens before the Dev Container starts when VS Code tries to
forward a WSLg Wayland socket:

```text
accessing specified distro mount service:
stat /run/guest-services/distro-services/ubuntu.sock: no such file or directory
```

The workshop does not use Linux GUI applications. In VS Code, open **User
Settings (JSON)**, add the following setting, and then run **Dev Containers:
Rebuild and Reopen in Container**:

```json
"dev.containers.mountWaylandSocket": false
```

This disables an optional Linux GUI socket that the workshop does not use.

</details>

<details>
<summary><b>macOS: docker-credential-desktop not found</b></summary>

This failure happens on the host before the Dev Container starts:

```text
error getting credentials - err: exec: "docker-credential-desktop": executable file not found in $PATH
```

Open **Docker Desktop → Settings → Advanced** and configure or reinstall the
Docker CLI tools. Restart VS Code and verify:

```bash
command -v docker-credential-desktop
docker pull mcr.microsoft.com/devcontainers/java:1-17-bookworm
```

If the helper exists but is not on `PATH`, create a symlink on Apple Silicon:

```bash
ln -s /Applications/Docker.app/Contents/Resources/bin/docker-credential-desktop \
  /opt/homebrew/bin/docker-credential-desktop
```

On an Intel Mac, use `/usr/local/bin/docker-credential-desktop` as the
destination.
</details>

<details>
<summary><b>H2 reports /home/vscode/test not found</b></summary>

Replace the console default `jdbc:h2:~/test` with:

```text
jdbc:h2:mem:ecommercedb
```

Use user `sa`, leave the password blank, and run with the default profile.
</details>

<details>
<summary><b>PostgreSQL authentication fails</b></summary>

The Dev Container uses workshop defaults when `.env` is absent. If you created
`.env` to customize credentials, confirm that it contains matching values:

```ini
POSTGRES_DB=ecommerce_db
POSTGRES_URL=jdbc:postgresql://database:5432/ecommerce_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
```

Never commit `.env`.
</details>

<details>
<summary><b>Port 8080 is already in use</b></summary>

Stop the process using port 8080 or choose another port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```
</details>
