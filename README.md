# Convergence 2026 — Spring Boot 4 E-Commerce API Workshop

> **Format:** 3 hours · **Audience:** 2nd–4th year engineering students
> Build a REST API progressively with validation, service-layer design, JPA,
> H2, PostgreSQL, testing, profiles, error handling, and outbound HTTP.

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

Install:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [VS Code](https://code.visualstudio.com/)
- The VS Code **Dev Containers** extension

After switching checkpoints, run **Dev Containers: Rebuild and Reopen in
Container** when VS Code prompts you. The Dev Container supplies Java 17, Maven,
and Copilot CLI; later stages also include PostgreSQL 16 and its matching client.

From the VS Code terminal, start Spring Boot with:

```bash
./mvnw spring-boot:run
```

The default profile is local development with H2. Press `Ctrl+C` before moving
to another checkpoint.

## Step 0 — Starter

```bash
git switch --detach step-0-starter
```

This stage contains only `EcommerceApplication` and Spring Web MVC. Run the
application and identify the generated startup behavior, embedded server, and
application entry point. Product endpoints are intentionally not present yet.

## Step 1 — REST and DTO validation

```bash
git switch --detach step-1-rest-dto
./mvnw spring-boot:run
```

Products are temporarily stored in a Java list. Create and list products:

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}'

curl http://localhost:8080/api/products
```

Try removing `name` or using a negative `price` to observe DTO validation.

## Step 2 — Service, JPA, and H2

```bash
git switch --detach step-2-service-db
./mvnw spring-boot:run
```

This stage introduces the controller → service → repository flow and persists
products with Spring Data JPA.

Open <http://localhost:8080/h2-console>:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:ecommercedb` |
| User Name | `sa` |
| Password | Leave blank |

H2 data is cleared when the application stops.

## Step 3 — Production profile and tests

```bash
git switch --detach step-3-production
cp .env.example .env
```

Set `POSTGRES_PASSWORD` in `.env`, then rebuild the Dev Container. Docker
Compose starts:

| Service | Purpose |
|---|---|
| `app` | Java environment and VS Code terminal |
| `database` | PostgreSQL 16 server |

VS Code enters `app` because `devcontainer.json` specifies `"service": "app"`.
The PostgreSQL hostname inside the Compose network is `database`.

Local H2 remains the default:

```bash
./mvnw spring-boot:run
```

Activate PostgreSQL explicitly:

```bash
SPRING_PROFILES_ACTIVE=production ./mvnw spring-boot:run
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

The suite contains Mockito service tests and MockMvc controller integration
tests.

## Step 4 — Outbound enrichment

```bash
git switch --detach step-4-outbound-enrichment
cp .env.example .env
```

This stage adds `RestTemplate`, proxy-ready client configuration, and a composed
product summary. The external response fixture is served locally, so no external
account or mock server is required.

Start the application, create a product, then request its enriched summary:

```bash
./mvnw spring-boot:run

curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}'

curl http://localhost:8080/api/products/1/summary
```

## API from Step 2 onward

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/products` | List products |
| `GET` | `/api/products/{id}` | Get a product |
| `POST` | `/api/products` | Create a product |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |
| `GET` | `/api/products/{id}/summary` | Enriched response; Step 4 only |

## Debug

Press `F5` and select **Debug Spring Boot App**. A breakpoint in
`ProductController` or `ProductService` is hit by the next API request.

See [`PRESENTATION.md`](./PRESENTATION.md) for architecture diagrams and workshop
notes.

## Troubleshooting

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

Confirm that `.env` contains:

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
