# Presentation

- **Hosted slides:** <https://neilghosh.github.io/springboot-workshop/>
- **Editable source:** [`PRESENTATION.md`](./PRESENTATION.md)

## Edit and preview

1. Open `docs/PRESENTATION.md`; it uses [Marp](https://marp.app/) Markdown.
2. Edit normal Markdown content. A line containing `---` separates slides.
3. In VS Code, install the recommended **Marp for VS Code** extension and use
   **Marp: Open Preview to the Side**.

For a local browser preview without VS Code, run this from the repository root:

```bash
npx @marp-team/marp-cli docs/PRESENTATION.md --html --preview
```

The `--html` option renders the deck's HTML/CSS diagrams. The diagrams work
offline in the exported HTML; they do not need an external diagram service.

## Present the concepts

This is a standalone **Spring Boot concepts** introduction for people who know
basic Java but have not seen the workshop application. There is no fixed duration.
The greeting endpoint, clock injection, lifecycle callbacks, and configuration
values are small illustrative examples, not features to run against this repository.

| Focus | Visual explanation |
|---|---|
| Spring versus Boot | Object management versus application setup |
| IoC and beans | Who runs the setup; managed and unmanaged objects |
| Dependency injection | A clock bean passed into a constructor |
| Bean lifecycle | Create and connect, initialize, use, clean up |
| Default singleton scope | Several requests use the same bean |
| Requests and data | Calls through layers, validation, mapping, and transactions |
| Settings and tests | Profile overrides and controlled helpers |

Explain each picture before naming the technical term. Marp presenter notes
include prompts, code pointers, and qualifications that need not crowd the
slides. Distinguish a bean's lifetime from the request flow: a request normally
calls an already-created bean rather than constructing it again. Keep setup
and checkpoint navigation in the separate hands-on guide.

To refresh the checked-in browser version from the source:

```bash
npx @marp-team/marp-cli docs/PRESENTATION.md --html -o docs/PRESENTATION.html
```

## Publish

Commit and push `docs/PRESENTATION.md` to `main`. The
[GitHub Pages workflow](../.github/workflows/pages.yml) rebuilds and deploys the
hosted presentation automatically.

Do not edit generated `docs/index.html` manually because the deployment workflow
replaces it.

## Future annexures

See [`TODO.md`](./TODO.md) for optional advanced modules covering observability,
data integrity, caching, OpenAPI documentation, security, and migrations.
