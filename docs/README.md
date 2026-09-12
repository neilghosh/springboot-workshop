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
npx @marp-team/marp-cli docs/PRESENTATION.md --preview
```

## Publish

Commit and push `docs/PRESENTATION.md` to `main`. The
[GitHub Pages workflow](../.github/workflows/pages.yml) rebuilds and deploys the
hosted presentation automatically.

Do not edit generated `docs/index.html` manually because the deployment workflow
replaces it.
