# Convergence 2026 — Spring Boot 4 E-Commerce API Workshop

> **Format:** 3 hours · **Audience:** 2nd–4th year engineering students
> Build a REST API with validation, service-layer design, JPA, H2, PostgreSQL,
> testing, profiles, and error handling.

## Workshop checkpoints

Each checkpoint is a Git tag:

| Tag | Adds |
|---|---|
| `step-0-starter` | Spring Boot application and Web MVC |
| `step-1-rest-dto` | Product REST API, DTOs, and validation |
| `step-2-service-db` | Service, repository, JPA entity, and H2 |
| `step-3-complete` | PostgreSQL profile, tests, and global errors |
| `step-4-outbound-enrichment` | External client and composed response |

To inspect a checkpoint:

```bash
git switch --detach step-3-complete
```

To make changes from a checkpoint, create a branch:

```bash
git switch -c my-step-3 step-3-complete
```

## Architecture

```text
HTTP request
    |
ProductController -> ProductService -> ProductRepository -> H2 / PostgreSQL
       DTOs          transactions       Spring Data JPA
```

See [`PRESENTATION.md`](./PRESENTATION.md) for diagrams and workshop notes.

## Recommended setup: VS Code Dev Container

Install:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [VS Code](https://code.visualstudio.com/)
- The VS Code **Dev Containers** extension

The Dev Container provides Java 17, Maven, Copilot CLI, PostgreSQL 16, and the
PostgreSQL 16 client. No host Java, Maven, or PostgreSQL installation is needed.

Create the local environment file:

```bash
cp .env.example .env
```

Set `POSTGRES_PASSWORD` in `.env`, then open the repository in VS Code and run:

```text
Dev Containers: Rebuild and Reopen in Container
```

Docker Compose starts two containers:

| Service | Purpose |
|---|---|
| `app` | Java development environment and VS Code terminal |
| `database` | PostgreSQL 16 server |

VS Code opens terminals in `app` because `.devcontainer/devcontainer.json`
contains `"service": "app"`. The application connects to PostgreSQL using the
Compose service hostname `database`.

## Run

From the VS Code Dev Container terminal:

```bash
./mvnw spring-boot:run
```

The Dev Container activates the `production` profile. Verify the API:

```bash
curl http://localhost:8080/api/products
```

Connect to PostgreSQL from the same terminal:

```bash
psql -h database -U postgres -d ecommerce_db
```

Useful `psql` commands:

```text
\dt
SELECT * FROM products;
\q
```

## Run with H2 instead

Override the Dev Container's production profile:

```bash
SPRING_PROFILES_ACTIVE=default ./mvnw spring-boot:run
```

Open <http://localhost:8080/h2-console> and use:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:ecommercedb` |
| User Name | `sa` |
| Password | Leave blank |

H2 data is cleared when the application stops.

## Build and test

```bash
SPRING_PROFILES_ACTIVE=default ./mvnw clean test
./mvnw clean package
```

Tests use H2 by default. The test suite contains Mockito service tests and
MockMvc controller integration tests.

## API

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/products` | List products |
| `GET` | `/api/products/{id}` | Get a product |
| `POST` | `/api/products` | Create a product |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}'

curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/1

curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Keyboard","description":"RGB Wireless","price":89.99,"stockQuantity":40,"category":"Electronics"}'

curl -X DELETE http://localhost:8080/api/products/1
```

## Debug

Press `F5` in VS Code and select **Debug Spring Boot App**. A breakpoint in
`ProductController` or `ProductService` is hit by the next API request.

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

Replace the console's default `jdbc:h2:~/test` value with:

```text
jdbc:h2:mem:ecommercedb
```

Use user `sa`, leave the password blank, and ensure the application is running
with `SPRING_PROFILES_ACTIVE=default`.
</details>

<details>
<summary><b>PostgreSQL authentication fails</b></summary>

Confirm that `.env` exists and that these values agree:

```ini
POSTGRES_DB=ecommerce_db
POSTGRES_URL=jdbc:postgresql://database:5432/ecommerce_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
SPRING_PROFILES_ACTIVE=production
```

Never commit `.env`.
</details>

<details>
<summary><b>Port 8080 is already in use</b></summary>

Stop the process using port 8080 or run Spring Boot on another port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```
</details>
