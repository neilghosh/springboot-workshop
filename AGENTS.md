# AGENTS.md — springboot-workshop

## Bootstrap — How This Repo Was Generated

To regenerate from zero, follow this exact order (verified against `pom.xml:6`, `src/main/resources/application*.properties`):

1. **Scaffold:** `https://start.spring.io` → Maven, Java 17, Spring Boot 4.0.8, group `com.convergence`, artifact `ecommerce-api`, package `com.convergence.ecommerce`. Dependencies: Web MVC, Data JPA, Validation, H2 Console, H2, and PostgreSQL. No Lombok — workshop is intentionally verbose.
2. **Wrapper:** Add custom `mvnw` / `mvnw.cmd` (see `mvnw.cmd:4` — checks `where mvn`, else bootstraps Maven 3.9.6 to `%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6` via PowerShell download). Add `.mvn/wrapper/maven-wrapper.properties` + `maven-wrapper.jar`.
3. **Config:** `src/main/resources/application.properties` = H2 default (port 8080, `jdbc:h2:mem:ecommercedb`, `H2Dialect`, `ddl-auto=update`, `spring.config.import=optional:file:.env[.properties]`). Copy to `application-production.properties` with `${POSTGRES_URL}`, `${POSTGRES_USER}`, `${POSTGRES_PASSWORD}` + `PostgreSQLDialect`. Add `.env.example` (template, committed) and `.env` (real secrets, gitignored via `.gitignore:2`). The production Dev Container loads the root `.env` through Compose; never hardcode credentials in `.devcontainer` files.
4. **Code layers (package `com.convergence.ecommerce`):** `EcommerceApplication.java` → DTOs (`dto/Product*DTO.java` with `jakarta.validation`) → `model/ProductEntity.java` (JPA `@Entity`) → `repository/ProductRepository.java` (JpaRepository) → `service/ProductService.java` (constructor injection, `@Transactional`, explicit `mapToResponseDTO`) → `controller/ProductController.java` (explicit constructor, no Lombok) → `exception/GlobalExceptionHandler.java` (`@RestControllerAdvice`).
5. **Tests:** `ProductServiceTest.java` (Mockito unit, uses setters not builders) + `ProductControllerIntegrationTest.java` (`@SpringBootTest` + `MockMvc`).
6. **Tooling/docs:** `.vscode/launch.json:8` (`mainClass: com.convergence.ecommerce.EcommerceApplication`), `PRESENTATION.md` (Slides 1-5, Slide 5 is profile-based DI), `README.md` (curl-only).
7. **Checkpoint tags (create in order):** `step-0-starter` (application + Web MVC), `step-1-rest-dto` (+DTOs/Controller + validation), `step-2-service-db` (+Entity/Repo/Service + H2 + H2 Console), `step-3-production` (+exception handler + tests + profiles + PostgreSQL), `step-4-outbound-enrichment` (+RestTemplate + proxy-ready external client + composed response). `main` contains the latest completed workshop. Tags may be recreated when the workshop flow changes.

## Commands

- **Java:** This Spring Boot 4.0.8 workshop requires **JDK 17**. Machine default is JDK 16 — set `JAVA_HOME=C:\Program Files\Java\jdk-17.0.20` (`java -version` must show 17). Wrapper respects `JAVA_HOME`.
- **Run (default H2, no DB setup):** `.\mvnw.cmd spring-boot:run` (Linux/macOS: `./mvnw spring-boot:run`)
- **Run (production Postgres):** `.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=production"` — **PowerShell requires quotes around `-D`** or use `$env:SPRING_PROFILES_ACTIVE="production"; .\mvnw.cmd spring-boot:run`
- **Tests:** `.\mvnw.cmd test` (H2) · `.\mvnw.cmd test "-Dspring.profiles.active=production"` (Postgres) · single class: `.\mvnw.cmd -Dtest=ProductServiceTest test` or `ProductControllerIntegrationTest` · single method: `.\mvnw.cmd -Dtest=ProductServiceTest#testCreateProduct test`
- **Debug:** `F5` → `Debug Spring Boot App` (`.vscode/launch.json:8`, `mainClass: com.convergence.ecommerce.EcommerceApplication`); or `.\mvnw.cmd spring-boot:run "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"` + attach to 5005 — breakpoint hits only on next `curl` request
- **Build:** `.\mvnw.cmd clean compile|test|package|install` · `clean package "-DskipTests"` for JAR · `target/` is gitignored — `git status` shows dirty `target/classes` until `clean`

## Project Structure

- Single-module Maven: `pom.xml` (`java.version=17`, parent 4.0.8). Key dirs: `src/main/java/com/convergence/ecommerce/{controller,dto,model,repository,service,exception}/`, `src/main/resources/`.
- Entrypoint: `EcommerceApplication.java`. No codegen/migrations — `ddl-auto=update`.
- `.env` auto-loaded by `application.properties:2` (`spring.config.import=optional:file:.env[.properties]`). Do not assume shell `source .env` happened.

## Conventions & Gotchas

- **No Lombok** — all DTOs/entities use explicit getters/setters/constructors for workshop readability. Do not re-add.
- **curl-only** — Postman was removed; `README.md` documents `curl`/`curl.exe` only. Keep `curl.exe` on single line with single-quoted JSON in PowerShell: `curl.exe -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d '{"name": "..."}'`.
- **PowerShell quoting:** Every `-D` arg must be quoted (`"-Dspring-boot.run.profiles=production"`), else `Unknown lifecycle phase ".run.profiles=production"`.
- **Postgres credentials:** Copy `.env.example` to `.env`, set the real password, and keep `.env` uncommitted. The Dev Container reads the root `.env` through Compose; use `database` as the PostgreSQL hostname inside the container and `localhost` for a host PostgreSQL installation.
- **`psql` PATH (Windows):** Installer not on PATH → use `& "C:\Program Files\PostgreSQL\16\bin\psql.exe"` or add via `[Environment]::SetEnvironmentVariable("PATH", ..., Machine)` + restart terminal.
- **Service start needs Admin:** `Start-Service postgresql-x64-16` / `net start postgresql-x64-16` requires elevated shell; `Get-Service`/`sc query` does not. Linux: `systemctl status/start postgresql`, macOS: `brew services list/start`.
- **`.vscode/` is gitignored** (`.gitignore:5` = `.vscode/*` + `!launch.json`/`!extensions.json`) — `settings.json` is ignored, `launch.json`/`extensions.json` stay tracked. `target/` was once committed in `d21f129` → now untracked via `git rm --cached -r target`.

## Verification

- Fast: `.\mvnw.cmd clean test` → expect 4 tests (service + controller) BUILD SUCCESS.
- Live: run default profile → `curl http://localhost:8080/api/products` → `[]`; production profile → `psql -U postgres -c "\dt"` shows `products`.
