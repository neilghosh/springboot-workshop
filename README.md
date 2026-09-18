# Spring Boot 4 E-Commerce API Workshop

> **Format:** 3 hours | **Audience:** 2nd-4th year engineering students

Build a REST API progressively with validation, service-layer design, JPA, H2,
PostgreSQL, testing, profiles, error handling, outbound HTTP, entity
relationships, and dependency inversion.

Follow the complete checkpoint journey in **[WORKSHOP.md](WORKSHOP.md)**.
Instructor presentation materials are in [docs/](docs/README.md).

## Clone

```bash
git clone https://github.com/neilghosh/springboot-workshop.git
cd springboot-workshop
git fetch --tags
```

## Prerequisites

Choose one setup:

| Setup | Install | Best for |
|---|---|---|
| **Dev Container (recommended)** | Docker, [VS Code](https://code.visualstudio.com/), and the VS Code Dev Containers extension | Ready-to-use Java 17 and PostgreSQL environment |
| **Local** | [JDK 17](https://learn.microsoft.com/en-us/java/openjdk/download/) | Running directly on your computer |

PostgreSQL 16 is optional until the production-profile workshop stage. The
default profile uses the embedded H2 database.

## Dev Container setup

1. Install and start Docker.
2. Open this repository in VS Code.
3. Run **Dev Containers: Rebuild and Reopen in Container**.
4. Verify the tools:

```bash
java -version
./mvnw --version
```

The container supplies Java 17, PostgreSQL 16, Copilot CLI, and the development
tools used by the workshop.

## Local setup

Install JDK 17. On Windows PowerShell:

```powershell
winget install --exact --id Microsoft.OpenJDK.17 --accept-package-agreements --accept-source-agreements
```

On Ubuntu 24.04 LTS:

```bash
sudo apt update && sudo apt install -y openjdk-17-jdk
```

Verify the repository Maven Wrapper:

**Unix/macOS:**

```bash
java -version
./mvnw --version
```

**Windows PowerShell:**

```powershell
java -version
.\mvnw.cmd --version
```

A separate Maven installation is not required.

### Optional local PostgreSQL

Install PostgreSQL 16 only when running the production profile outside the Dev
Container.

**Windows PowerShell:**

```powershell
winget install --exact --id PostgreSQL.PostgreSQL.16 --accept-package-agreements --accept-source-agreements
& "C:\Program Files\PostgreSQL\16\bin\psql.exe" -h localhost -U postgres -d postgres -c "CREATE DATABASE ecommerce_db;"
```

**Ubuntu 24.04 LTS:**

```bash
sudo apt install -y postgresql-16
sudo -u postgres createdb ecommerce_db
```

## Run the maintained application

**Unix/macOS:**

```bash
./mvnw spring-boot:run
```

**Windows PowerShell:**

```powershell
.\mvnw.cmd spring-boot:run
```

Open <http://localhost:8080/api/products>. Stop the application with `Ctrl+C`.

## Test

**Unix/macOS:**

```bash
./mvnw clean test
```

**Windows PowerShell:**

```powershell
.\mvnw.cmd clean test
```

Continue with the [step-by-step workshop guide](WORKSHOP.md).