# Obtuse

A 2D libGDX game (2018), modernised and now playable in the browser, on Android, or both.

## Play in the browser

🎮 **https://USERNAME.github.io/REPO/** ← update this once GitHub Pages is live.

Every push to `main` rebuilds the web version and publishes it automatically via GitHub
Actions (`.github/workflows/deploy.yml`). The build needs only a JDK — `WEB_ONLY=1` leaves
the Android module out (see `settings.gradle`), so no Android SDK is required on CI.

## Build it yourself

**Web** (output in `teavm/build/dist/webapp/`):

```sh
./gradlew :teavm:buildRelease     # optimised bundle
./gradlew :teavm:run              # build + serve at http://localhost:8080
```

**Android** — see [BUILD.md](BUILD.md). Notes for working on the code are in
[CLAUDE.md](CLAUDE.md).

## Modules

- `core` — all the game logic, shared by every platform.
- `teavm` — the browser backend (transpiles `core` to JavaScript via gdx-teavm).
- `android` — the Android app.

`desktop/`, `ios/` and `ios-moe/` are 2018 leftovers, not part of the build.
