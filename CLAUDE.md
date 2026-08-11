# Obtuse — notes for Claude Code

A 2D libGDX game written in 2018, being modernised. Read `BUILD.md` for how to build and
run; this file is what you need to know before changing code.

## Current state

- **Android: working.** Runs on a Poco F3 and a Pixel 6 API 34 emulator.
- **libGDX upgraded 1.9.8 → 1.14.2.** `core` compiles; runtime verification in progress.
- **Next: a browser build via gdx-teavm.** A spike (in `../spike`) proved Box2D, threads
  and blocking spin-waits all work in a browser under TeaVM.

## Build and run

```sh
./gradlew :core:compileJava        # fast check of game logic only, no APK
./gradlew :android:assembleDebug   # APK -> android/build/outputs/apk/debug/
./gradlew :android:installDebug    # build and push to the connected device/emulator
```

`settings.gradle` includes only `core` and `android`. The `desktop/`, `ios/` and
`ios-moe/` folders are 2018 leftovers, not in the build; do not try to fix them.

Debugging on device. **`adb` is not on the PATH**; it lives at
`/Users/daniel/Library/Android/sdk/platform-tools/adb`, so use the full path (or export
`ANDROID_HOME=$HOME/Library/Android/sdk` and add `$ANDROID_HOME/platform-tools` to PATH
yourself at the start of a session):

```sh
adb logcat -c && adb logcat | grep -iE "obtuse|AndroidRuntime|OBTUSE|SPIKE"
adb exec-out screencap -p > /tmp/screen.png    # then read the PNG
adb shell input tap X Y                        # drive the UI to reproduce something
```

The game logs layout diagnostics under the tag `OBTUSE`.

## Architecture

`Screen → GameGame → Level → GameStage`. Screens are singletons in `MyScreen.screens`,
constructed **by reflection from a string name** (`Obtuse.changeScreen("WorldScreen")`).
Key bindings and arena creation also use reflection. Do not "clean up" these into direct
constructor calls without checking the web port's reflection registration.

Top-down Box2D overworld → touching a `WorldEnemy` starts a turn-based battle → loot.

**Combat runs on its own thread.** `Turn extends Runnable`, with blocking `Thread.sleep`
and spin-waits like `while (selectedAbility == null) Turn.sleep();`. This is deliberate
and it survives on the web (TeaVM emulates threads with coroutines). Do not rewrite it
into a state machine without a strong reason.

## Invariants — things that have already bitten

- **Never hard-code world-space Y.** Layout constants written in 2018 assume the 640x520
  desktop window (~8.1 world units tall). A 20:9 phone shows ~4.5, so anything fixed in
  world Y falls off screen. Derive from `camera(0).viewportHeight`.
- **Never hard-code pixel font sizes.** `Fonts` scales everything by screen height.
- **Screen size is not fixed at startup.** `MyScreen.refreshLayout()` re-derives
  `Obtuse.width/height/ratio`, every viewport and every world camera. Call it, don't
  cache sizes.
- **Never stretch pixel art into a differently-shaped box.** The arrow art is 16x8 and
  8x16; `TouchButton` scales by a whole number and preserves aspect.
- **Audio is listed explicitly** in `audio/AudioAssets.java`. `FileHandle.list()` returns
  nothing in a browser. Add new audio files to that list or they silently never load.
- **Touch input model: hold = hover, release = click.** This reuses every existing
  `hovered()`/`clicked()` path from the mouse era. Keep it.
- libGDX is pinned in `gradle.properties`. Android `.so` natives are fetched from Maven by
  the `copyAndroidNatives` task — never check natives into `android/libs` again.

## Verifying changes

There is no test suite. `:core:compileJava` is the fast check. For anything visual,
build, install, screenshot with `adb exec-out screencap`, and look at it.
