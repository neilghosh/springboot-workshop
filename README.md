# Convergence 2026 — Spring Boot 4 E-Commerce API Workshop

> **Host:** GDGC VNR VJIET · **Format:** 3 Hours · **Level:** 2nd–4th Year Engineering Students  
> Build a production-grade E-Commerce REST API from scratch: REST → Validation → Service → JPA → PostgreSQL/H2

---

## ⏱️ Workshop Flow (3 Hours)

| Block | Duration | Content |
|-------|----------|---------|
| Registration & Setup | 10 min | Clone repo, verify JDK 17 & run default profile (`curl []`) |
| Concepts Before Code | 45 min | `PRESENTATION.md` — why Spring Boot vs Node/Python/Go, why code in agent era, Boot features, IoC/DI — **expanded** |
| Hands-on Live Coding | 105 min | Follow git branches `step-0` → `step-3` (pace unchanged, checkpoints trimmed 5 min) |
| Q&A & Wrap-up | 20 min | Profiles, `curl`, Postgres, debug + agent-generated code review |

**Live-coding branches — checkout to follow along:**
```bash
git switch step-0-starter    # Starter: application + Web MVC
git switch step-1-rest-dto   # REST + DTOs + validation
git switch step-2-service-db # JPA Entity + Repository + Service + H2
git switch step-3-complete   # PostgreSQL + tests + profiles + errors
```

Each branch is independently buildable and adds only the dependencies needed for
that stage:

| Branch | Adds | Main dependency additions |
|---|---|---|
| `step-0-starter` | Running application | Web MVC |
| `step-1-rest-dto` | Product REST API and request validation | Validation |
| `step-2-service-db` | Persistence, service layer, and H2 Console | Data JPA, H2, H2 Console |
| `step-3-complete` | Profiles, PostgreSQL, errors, and tests | PostgreSQL, test starters |

---

## 🏛️ Architecture at a Glance

```text
[ Postman / curl / Frontend ]
              │  JSON
              ▼
[ DispatcherServlet ] → [ ProductController @RestController ] → DTO Validation
                              │
                              ▼
                    [ ProductService @Service ]  ← @Transactional
                              │
                              ▼
              [ ProductRepository JpaRepository ] → Hibernate → [ H2 (default) | PostgreSQL (production) ]
```

Full diagrams & talk notes: [`PRESENTATION.md`](./PRESENTATION.md)

---

## 🚀 Quick Start (5 min — For Students Who Want to Run Now)

```bash
# 1. Clone
git clone <your-github-url> springboot-workshop
cd springboot-workshop

# 2. Verify Java 17 (required — this workshop uses Spring Boot 4.0.8)
java -version
# → openjdk 17.x  or Oracle JDK 17.x

# 3. Linux/macOS only: make the Maven wrapper executable
chmod +x mvnw

# 4. Run — zero Maven/database setup, uses H2 in-memory DB
# Windows PowerShell: .\mvnw.cmd spring-boot:run
# Linux/macOS:       ./mvnw spring-boot:run

# 5. Test
curl http://localhost:8080/api/products
# → []   (empty list — H2 ready)
# H2 Console: http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:ecommercedb, User: sa)
```

> No Maven install is needed. This repository includes the Maven Wrapper (`mvnw`/`mvnw.cmd`), which downloads Maven 3.9.6 on first use. A normal `git clone` preserves the executable bit on `mvnw`; run `chmod +x mvnw` if the repository was downloaded as a ZIP or the permission was not preserved.

### Why there is no `mvn archetype` command here
This project is already generated and includes its wrapper, so you do not need Maven installed to start it. Maven archetypes are templates for generating a generic Maven project; Spring Boot projects are normally created with [Spring Initializr](https://start.spring.io) instead. The exact Initializr settings used for this workshop are documented in `AGENTS.md`. After generating or cloning a project, the Maven Wrapper is the recommended way to run it:

```bash
./mvnw spring-boot:run       # Linux/macOS
.\mvnw.cmd spring-boot:run   # Windows
```

### Generate a matching starter skeleton
If you want to recreate the workshop's initial project before checking out the branches, use these Spring Initializr settings:

| Setting | Value |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | `4.0.8` |
| Group | `com.convergence` |
| Artifact | `ecommerce-api` |
| Package name | `com.convergence.ecommerce` |
| Packaging | Jar |
| Java | 17 |

Add **Spring Web MVC**, **Spring Data JPA**, **Validation**, **PostgreSQL Driver**, and **H2 Console**. Select stable `4.0.8`, not a snapshot or milestone.

The package name must be entered explicitly. Otherwise Initializr derives `com.convergence.ecommerce_api` from the hyphenated artifact name, which differs from this repository. Initializr will also generate `EcommerceApiApplication`; that starter class can be renamed to `EcommerceApplication` if you want it to match this repository exactly.

The same skeleton can be downloaded without Maven:

```bash
curl "https://start.spring.io/starter.zip?type=maven-project&language=java&javaVersion=17&bootVersion=4.0.8&groupId=com.convergence&artifactId=ecommerce-api&name=ecommerce-api&packageName=com.convergence.ecommerce&packaging=jar&dependencies=webmvc,data-jpa,validation,h2console,h2,postgresql" -o ecommerce-api.zip
unzip ecommerce-api.zip
cd ecommerce-api
chmod +x mvnw
./mvnw spring-boot:run
```

After confirming the generated starter runs, it can be discarded; the workshop branches contain the progressively completed version:

```bash
git clone <your-github-url> springboot-workshop
cd springboot-workshop
git switch --detach refs/tags/step-0-starter
```

For the future outbound HTTP exercise, `examples/external-product.json` is the
entire external response fixture. It intentionally matches the sample local
product (`Mechanical Keyboard`) so the service-layer enrichment example stays
easy to follow. No mock server or external account is required.

---

## 📋 Prerequisites

| Tool | Version | Install | Verify |
|------|---------|---------|--------|
| **Oracle JDK 17+** | 17 LTS | [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or `winget install Oracle.JDK.17` | `java -version` |
| **Maven** | 3.8+ *or* use wrapper | [Maven](https://maven.apache.org/download.cgi) or just use `.\mvnw.cmd` | `mvn -version` or `.\mvnw.cmd --version` |
| **VS Code** | Latest | [VS Code](https://code.visualstudio.com/) | — |
| **PostgreSQL** *(optional, for production profile)* | 16 | [PostgreSQL](https://www.postgresql.org/download/) or `winget install PostgreSQL.PostgreSQL.16` | `psql --version` |
| **curl** | — | Preinstalled on macOS/Linux; Windows 10+ includes `curl.exe` | `curl --version` |

### Dev Container

The Dev Container is the recommended workshop setup. It provides Java 17, Maven,
the Copilot CLI, and PostgreSQL 16 through Docker Compose. Open the repository in
VS Code and run **Dev Containers: Reopen in Container**.

Before starting the container, copy `.env.example` to `.env` and set
`POSTGRES_PASSWORD`. The `.env` file is ignored by Git and supplies credentials
to both Compose services.

The application uses the `production` profile in this stage. From the container
terminal:

```bash
./mvnw spring-boot:run
psql -h database -U postgres -d ecommerce_db
```

The database host inside the Dev Container is `database`. Do not commit `.env`.

**Debugging in VS Code:** Press `F5` → *Debug Spring Boot App* (config in `.vscode/launch.json`). Breakpoints in `ProductController` / `ProductService` will be hit on next Postman/curl request.

---

## ▶️ Running the Application

### A. Default Profile — H2 In-Memory (Workshop Default)
No database setup. Data resets on restart — ideal for live coding & tests.
```bash
.\mvnw.cmd spring-boot:run                          # H2 auto
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=default"
# env alternative
$env:SPRING_PROFILES_ACTIVE="default"; .\mvnw.cmd spring-boot:run  # PowerShell
SPRING_PROFILES_ACTIVE=default ./mvnw spring-boot:run              # Linux/macOS
```

### B. Production Profile — PostgreSQL (Persistent)
```bash
# 1. Create DB once
psql -U postgres -c "CREATE DATABASE ecommerce_db;"
# Windows if psql not in PATH: & "C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres -c "CREATE DATABASE ecommerce_db;"

# 2. Set credentials (see .env.example — auto-loaded via spring.config.import=optional:file:.env[.properties])
# Edit .env: POSTGRES_PASSWORD=your_real_password
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=production"
# env alternative (no quoting needed)
$env:SPRING_PROFILES_ACTIVE="production"; .\mvnw.cmd spring-boot:run
```

`application-production.properties` externalizes credentials:
```properties
spring.datasource.url=${POSTGRES_URL:jdbc:postgresql://localhost:5432/ecommerce_db}
spring.datasource.username=${POSTGRES_USER:postgres}
spring.datasource.password=${POSTGRES_PASSWORD:secret}
```

### C. Remote Debugging
```bash
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"
# Attach VS Code debugger to port 5005
```

---

## 🔧 Build & Test (TDD)

```bash
# Unit + integration tests (H2, fast)
.\mvnw.cmd test

# Tests against PostgreSQL (requires running DB)
.\mvnw.cmd test "-Dspring.profiles.active=production"

# Single test class always on production profile: add @ActiveProfiles("production") on the class
```
Test classes: `ProductServiceTest` (Mockito unit) + `ProductControllerIntegrationTest` (MockMvc + H2).

### Clean Build Commands
```bash
.\mvnw.cmd clean                     # remove target/
.\mvnw.cmd clean compile             # clean + compile
.\mvnw.cmd clean test                # clean + tests
.\mvnw.cmd clean package             # executable JAR in target/*.jar
.\mvnw.cmd clean package "-DskipTests"
.\mvnw.cmd clean install             # install to local ~/.m2
```

---

## 📬 API Reference & Testing (curl-only)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/{id}` | Get by ID |
| `POST` | `/api/products` | Create (validates `@NotBlank`, `@Positive`) |
| `PUT` | `/api/products/{id}` | Update |
| `DELETE` | `/api/products/{id}` | Delete |

### cURL — Bash / Linux / macOS

**Create a product:**

```bash
curl -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d '{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}'
```

**Get products:**

```bash
curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/1
```

**Update and delete a product:**

```bash
curl -X PUT http://localhost:8080/api/products/1 -H "Content-Type: application/json" -d '{"name":"Updated Keyboard","description":"RGB Wireless","price":89.99,"stockQuantity":40,"category":"Electronics"}'
curl -X DELETE http://localhost:8080/api/products/1
```

### cURL — Windows PowerShell
Use `curl.exe` (bare `curl` is an alias for `Invoke-WebRequest`):

**Create a product:**

```powershell
'{"name":"Mechanical Keyboard","description":"RGB Wireless","price":79.99,"stockQuantity":50,"category":"Electronics"}' | curl.exe -i http://localhost:8080/api/products --json '@-'
```

**Get products:**

```powershell
curl.exe http://localhost:8080/api/products
curl.exe http://localhost:8080/api/products/1
```

**Update and delete a product:**

```powershell
curl.exe -X PUT http://localhost:8080/api/products/1 -H "Content-Type: application/json" -d '{"name": "Updated Keyboard", "description": "RGB Wireless", "price": 89.99, "stockQuantity": 40, "category": "Electronics"}'
curl.exe -X DELETE http://localhost:8080/api/products/1
```

---

## 🐘 PostgreSQL Essentials (When You Need It)

**Install:**
```powershell
winget install PostgreSQL.PostgreSQL.16          # Windows
sudo apt update && sudo apt install postgresql postgresql-contrib  # Ubuntu/Debian
brew install postgresql@16                        # macOS
```

**psql usage:**
```sql
CREATE DATABASE ecommerce_db;  -- once
\l                             -- list databases
\c ecommerce_db                 -- connect
\dt                            -- list tables (after app has run)
SELECT * FROM products;
```

See *Troubleshooting* below for service status, `psql` PATH fix, and password help.

---

## 🆘 Troubleshooting (Appendix — Read Only If Stuck)

<details>
<summary><b>PowerShell: Unknown lifecycle phase .run.profiles=production</b></summary>

PowerShell parses `-D` as a parameter. **Quote it:**
```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=production"
.\mvnw.cmd test "-Dspring.profiles.active=production"
```
Or avoid quoting by using env vars: `$env:SPRING_PROFILES_ACTIVE="production"; .\mvnw.cmd spring-boot:run`
</details>

<details>
<summary><b>FATAL: password authentication failed for user "postgres"</b></summary>

Your local Postgres password ≠ `secret` (the `.env.example` default). Update `.env`:
```ini
POSTGRES_PASSWORD=your_real_password
```
Then rerun. Or:
```powershell
$env:POSTGRES_PASSWORD="your_real_password"; .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=production"
```
To reset Postgres password: `psql -U postgres -c "ALTER USER postgres PASSWORD 'secret';"`
</details>

<details>
<summary><b>psql is not recognized (Windows)</b></summary>

Installer doesn't add to PATH. Use absolute path:
```powershell
& "C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres -c "CREATE DATABASE ecommerce_db;"
```
Or add permanently (Admin PowerShell, then restart terminal):
```powershell
[Environment]::SetEnvironmentVariable("PATH", $env:PATH + ";C:\Program Files\PostgreSQL\16\bin", [EnvironmentVariableTarget]::Machine)
```
</details>

<details>
<summary><b>Service start: Access is denied / System error 5</b></summary>

Starting services needs Admin. Run PowerShell **as Administrator**:
```powershell
Start-Service postgresql-x64-16
# or CMD Admin: net start postgresql-x64-16
# or GUI: Win+R → services.msc → postgresql-x64-16 → Start
```
Check status (no admin needed):
```powershell
Get-Service -Name "*postgres*"          # PowerShell
sc query postgresql-x64-16             # CMD
systemctl status postgresql            # Linux
pg_isready                             # Linux alt
brew services list | grep postgresql   # macOS
```
Start on other OS:
```bash
sudo systemctl start postgresql            # Linux
sudo systemctl enable postgresql           # auto-start on boot
brew services start postgresql@16          # macOS
```
</details>

<details>
<summary><b>The JAVA_HOME environment variable is not defined correctly / release version 17 not supported</b></summary>

This workshop uses Spring Boot 4.0.8 and requires JDK 17+. If an older JDK is active, set `JAVA_HOME` to JDK 17:
```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17.0.20", [EnvironmentVariableTarget]::Machine)
# Then restart terminal; verify:
java -version
$env:JAVA_HOME="C:\Program Files\Java\jdk-17.0.20"; $env:PATH="$env:JAVA_HOME\bin;$env:PATH"; java -version
```
`.\mvnw.cmd` respects `JAVA_HOME` — no Maven reinstall needed.
</details>

<details>
<summary><b>winget install Apache.Maven didn't work</b></summary>

Use the wrapper instead — no install needed: `.\mvnw.cmd --version` (auto-downloads Maven 3.9.6).  
Or install manually from [maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) and add `bin` to PATH.
</details>

<details>
<summary><b>Port 8080 already in use</b></summary>

```powershell
# Find and kill
netstat -ano | findstr :8080
taskkill /PID <pid> /F
# Or run on different port:
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```
</details>

<details>
<summary><b>.env not loading</b></summary>

This project auto-loads `.env` via `spring.config.import=optional:file:.env[.properties]` in `application.properties:2`.  
If you still need manual load:
```powershell
Get-Content .env | ForEach-Object { if ($_ -match "^\s*([^#][^=]+)=(.*)$") { Set-Item -Path Env:$($matches[1]) -Value $matches[2].Trim() } }
# Linux/macOS:
set -a; source .env; set +a
```
</details>
