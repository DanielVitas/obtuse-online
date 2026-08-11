# Obtuse — building the Android APK

Everything you need to turn `18 obtuse android/` into an installable app, from a Mac
with nothing set up. Pick **Route A** if you want the easy path, **Route B** if you'd
rather not install Android Studio.

The project folder is:

```
~/Documents/claude-folder/18 obtuse android
```

The name has spaces in it, so quote it in every shell command.

---

## 0. What the build needs

| | Why |
| --- | --- |
| **JDK 17** | Android Gradle Plugin 8.x refuses to run on anything older, and Gradle 8.7 won't use anything newer than 21. |
| **Android SDK**, platform 34 + build-tools 34 | Compiles resources and turns the classes into an APK. |
| **~3 GB free disk** | Gradle distribution, the plugin, the SDK, and an 80 MB APK. |
| **A network connection, once** | The first build downloads Gradle 8.7, AGP 8.4.2 and libGDX 1.9.8. After that it builds offline. |

Java 17 specifically — not 8, not 21. If you have several JDKs, section 6 shows how to
point Gradle at the right one.

---

## Route A — Android Studio

The least fiddly option: it brings its own JDK and manages the SDK for you.

1. **Install Android Studio** from <https://developer.android.com/studio>. Drag it to
   Applications and open it. The setup wizard installs an SDK, platform-tools and a
   build-tools release under `~/Library/Android/sdk` — accept the defaults.

2. **Open the project.** *File → Open*, navigate to `18 obtuse android`, and pick the
   folder itself (not a file inside it). Do **not** use *Import Project*; this is
   already a Gradle project.

3. **Let it sync.** A progress bar at the bottom reads "Gradle sync". First time this
   downloads Gradle 8.7 and the Android plugin — several minutes on a slow line. It also
   writes `local.properties` with your SDK path, which is why that file ships empty.

   If a banner offers to upgrade the Android Gradle Plugin, **decline it**. The versions
   here are pinned to work with libGDX 1.9.8 and the 2018 native libraries.

4. **Build.** *Build → Build Bundle(s) / APK(s) → Build APK(s)*. When it finishes, the
   notification has a **locate** link that opens the APK in Finder.

5. **Or run it directly.** Plug in a phone with USB debugging on (section 4), pick it in
   the device dropdown at the top, and press the green ▶. Android Studio builds,
   installs and launches in one step.

The APK ends up at:

```
android/build/outputs/apk/debug/android-debug.apk
```

---

## Route B — command line only

No Android Studio. Roughly 1.5 GB of downloads instead of 4 GB.

### B1. Install a JDK 17 and the SDK tools

With [Homebrew](https://brew.sh):

```sh
brew install --cask temurin@17
brew install --cask android-commandlinetools
```

Without Homebrew: grab Temurin 17 from <https://adoptium.net> and the "Command line
tools only" zip from the bottom of <https://developer.android.com/studio>, then unzip it
so that `sdkmanager` sits at `~/Library/Android/sdk/cmdline-tools/latest/bin/sdkmanager`.
That nesting matters — `sdkmanager` refuses to run from anywhere else.

### B2. Point the shell at both

Homebrew on Apple Silicon puts the SDK in `/opt/homebrew/share/android-commandlinetools`;
on Intel it's `/usr/local/share/android-commandlinetools`. Add to `~/.zshrc`:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export ANDROID_HOME="$(brew --prefix)/share/android-commandlinetools"
export PATH="$ANDROID_HOME/platform-tools:$PATH"
```

If you installed by hand instead, use `export ANDROID_HOME="$HOME/Library/Android/sdk"`.

Then `source ~/.zshrc` and check both:

```sh
java -version          # should say 17.x
sdkmanager --version
```

### B3. Install the SDK pieces and accept the licences

```sh
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
sdkmanager --licenses     # press y at each prompt
```

The licence step is not optional — the build fails with a wall of licence text if you
skip it.

### B4. Build

```sh
cd ~/Documents/claude-folder/"18 obtuse android"
./gradlew assembleDebug
```

First run takes 5–15 minutes and prints a lot. You want the last line to read
`BUILD SUCCESSFUL`. Later builds take seconds.

Check what came out:

```sh
ls -lh android/build/outputs/apk/debug/android-debug.apk
```

Around 80 MB is right — 77 MB of that is the audio and the texture atlas.

---

## 3. Useful Gradle commands

| Command | Does |
| --- | --- |
| `./gradlew assembleDebug` | Build the debug APK. |
| `./gradlew installDebug` | Build **and** push it to the connected phone. |
| `./gradlew clean` | Throw away build output; use if things get weird. |
| `./gradlew assembleDebug --offline` | Build with no network, once the caches are warm. |
| `./gradlew assembleDebug --stacktrace` | Full Java stack trace when something fails. |
| `./gradlew tasks` | List everything available. |

---

## 4. Getting it onto a phone

### Over USB

1. On the phone: *Settings → About phone*, tap **Build number** seven times. A toast
   says you're a developer.
2. *Settings → System → Developer options* → turn on **USB debugging**.
3. Plug it in. The phone shows an "Allow USB debugging?" dialog — accept it, ticking
   "always allow" so it stops asking.
4. Confirm the Mac sees it, then install:

```sh
adb devices                     # your phone should be listed as "device"
adb install -r android/build/outputs/apk/debug/android-debug.apk
```

`-r` replaces an existing copy, so you can reinstall over the top when you iterate.
`adb` comes from the `platform-tools` package; Android Studio users will find it at
`~/Library/Android/sdk/platform-tools/adb`.

### Without a cable

AirDrop, email or Google Drive the `.apk` to the phone and tap it in your downloads.
Android will ask permission to install from that app the first time — allow it. This is
fine for a debug build you signed yourself.

### On an emulator (no cable, best for iterating)

Your Mac is Apple Silicon, so the system image has to be **arm64-v8a** — x86 images
won't run. The APK ships arm64-v8a, so it installs fine.

First find your SDK and put its tools on the PATH:

```sh
ls -d ~/Library/Android/sdk "$(brew --prefix)/share/android-commandlinetools" 2>/dev/null
```

Take whichever path that prints and use it below:

```sh
export ANDROID_HOME="$HOME/Library/Android/sdk"      # or the brew path
export PATH="$ANDROID_HOME/emulator:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"
```

Install the emulator and an image, once (about 1.5 GB):

```sh
sdkmanager "emulator" "platform-tools" "system-images;android-34;google_apis;arm64-v8a"
sdkmanager --licenses
```

Create the virtual device, once. A Pixel 6 is 1080x2400 — the same 20:9 shape as a real
phone, so layout problems reproduce faithfully:

```sh
avdmanager create avd -n obtuse -k "system-images;android-34;google_apis;arm64-v8a" -d pixel_6
```

(`avdmanager list device | grep -i pixel` lists the profiles if `pixel_6` is rejected.)

Start it, and leave it running:

```sh
emulator -avd obtuse &
```

From then on the whole loop is one command — it builds and installs to whatever is
connected, emulator or phone:

```sh
cd ~/Documents/claude-folder/"18 obtuse android"
./gradlew installDebug
```

`adb devices` will show it as `emulator-5554`. Screenshots work the same as on a phone.
The emulator should rotate to landscape by itself when the game asks; if it doesn't, use
the rotate buttons on the toolbar beside the window. If it ever gets into a bad state,
`emulator -avd obtuse -wipe-data` gives you a factory-fresh one.

In Android Studio instead: *Tools → Device Manager → add a device*, pick Pixel 6 with an
API 34 arm64 image, start it, then press ▶.

Caveat: the emulator is faithful for layout and input, and rough for audio timing and
frame rate. Confirm anything performance-related on the real phone.

### Watching it run

```sh
adb logcat | grep -i "obtuse\|AndroidRuntime\|libGDX"
```

`AndroidRuntime` lines are crashes with the stack trace attached — that's what to send
me if it misbehaves.

---

## 5. A signed release APK (optional)

The debug APK is signed with a throwaway debug key. That's fine for your own phone but
can't be updated in place by a differently-signed build, and can't go on Play. For a
proper one:

1. Make a keystore, once, and keep it safe — losing it means never updating the app:

```sh
keytool -genkeypair -v -keystore ~/obtuse-release.jks \
        -keyalg RSA -keysize 2048 -validity 10000 -alias obtuse
```

2. Put the passwords somewhere outside the project. In `~/.zshrc`:

```sh
export OBTUSE_STORE_PASSWORD='…'
export OBTUSE_KEY_PASSWORD='…'
```

3. In `android/build.gradle`, inside the `android { }` block, before `buildTypes`:

```groovy
    signingConfigs {
        release {
            storeFile file(System.getProperty('user.home') + '/obtuse-release.jks')
            storePassword System.getenv('OBTUSE_STORE_PASSWORD')
            keyAlias 'obtuse'
            keyPassword System.getenv('OBTUSE_KEY_PASSWORD')
        }
    }
```

and add one line to the existing `release` build type:

```groovy
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
```

4. Build it:

```sh
./gradlew assembleRelease
ls -lh android/build/outputs/apk/release/android-release.apk
```

Leave `minifyEnabled false`. The game creates screens by reflection from strings
(`Obtuse.changeScreen("WorldScreen")`), and R8 would strip those classes as unused,
producing a build that dies the moment you leave the title screen.

---

## 6. When it goes wrong

**`zsh: permission denied: ./gradlew`**
`chmod +x gradlew`, then try again.

**`SDK location not found`**
`ANDROID_HOME` isn't set in the shell you're using, and `local.properties` is empty on
purpose. Either export it (B2) or add one line to `local.properties`:
`sdk.dir=/Users/daniel/Library/Android/sdk`

**`Android Gradle plugin requires Java 17` / `Unsupported class file major version`**
Gradle picked the wrong JDK. Check which one with `./gradlew -version` — look at the
`JVM:` line. Fix it for this project only by adding to `gradle.properties`:

```
org.gradle.java.home=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home
```

**`Failed to install the following SDK components … accept the licenses`**
Run `sdkmanager --licenses` (B3). In Android Studio, *Settings → Languages & Frameworks
→ Android SDK* has the same prompts.

**`Could not resolve com.badlogicgames.gdx:gdx:1.9.8`**
Network, proxy or VPN blocking Maven Central. Confirm with
`curl -I https://repo1.maven.org/maven2/`. (This is exactly the wall I hit — the cloud
sandbox I'm running in gets a 403 from those hosts, which is why I couldn't build the
APK for you.)

**`No matching abi found` / `armeabi is not supported`**
Something restored the old `android/libs/armeabi` folder. Delete it; modern AGP rejects
that ABI and it's already excluded in `defaultConfig.ndk.abiFilters`.

**App installs but shows a black screen, then dies**
Get the real reason from `adb logcat`. The likeliest causes are in section 9.

**`INSTALL_FAILED_UPDATE_INCOMPATIBLE`**
A copy signed with a different key is already installed. `adb uninstall com.obtuse.game`
first.

**Everything is inexplicably broken**
`./gradlew clean`, then `rm -rf ~/.gradle/caches` and rebuild.

---

## 7. What the conversion changed in the build

| Before (2018) | Now |
| --- | --- |
| Gradle 4.6 | Gradle 8.7 |
| Android Gradle Plugin 3.1.0 | 8.4.2 |
| `jcenter()` — shut down in 2022 | `mavenCentral()` + `google()` |
| compileSdk 28, minSdk 9, targetSdk 28 | compileSdk 34, minSdk 21, targetSdk 34 |
| Java 1.6 | Java 8 source level, JDK 17 to build |
| `package=` in the manifest | `namespace` in `android/build.gradle` |
| launcher activity with no `exported` | `android:exported="true"` (required since Android 12) |
| 5 modules incl. RoboVM + Multi-OS Engine | `core` + `android` only |
| `sdk.dir=C:/Users/Daniel/…` | left for Android Studio to fill in |

libGDX stays pinned at **1.9.8** deliberately: `android/libs` holds the matching `.so`
natives checked in back in 2018, and mixing those with another libGDX version crashes at
startup. `gdx-bullet`, `gdx-controllers`, `ashley` and `gdx-ai` were declared in 2018 but
no class imports them, so they're gone — that also removed 31 MB of bullet natives. The
`armeabi` ABI folder went too, because modern AGP rejects it.

`desktop/`, `ios/` and `ios-moe/` are still on disk but no longer in `settings.gradle` —
RoboVM and Multi-OS Engine can't be resolved any more. Add them back if you ever want them.

---

## 8. Controls

The desktop build assumed a mouse that is always hovering somewhere, plus a keyboard.
On a phone:

- **Overworld** — the arrow pad in the bottom left walks (multi-touch, so diagonals
  work). **Use** interacts with whatever you're facing (the old SPACE), **Bag** opens the
  inventory (the old I).
- **Fights** — hold a finger on an ability or a character to inspect it; the info panel
  updates exactly as hovering did. Lift to commit. While choosing a target, lifting away
  from every slot cancels, which is what ESCAPE did.
- **Inventory** — hold to inspect, lift to equip or unequip. **Back** returns to the world.
- **Loot** — hold to read an item, lift to take it.

That press-to-hover / release-to-click mapping is why no existing screen needed
rewriting: every highlight, info panel and animation still fires on the same calls.

Nine files changed, all additive — no existing method signature or gameplay value was
touched. `maingame/GameGame.java` (touch state), the four `*Game` classes (their
`runAndroid()` bodies), `screens/WorldScreen.java` and `screens/InventoryScreen.java`
(one extra stage each for the controls), `gameobjects/UI/touch/TouchButton.java` (new),
and `Obtuse.java` (`setWindowedMode` now only runs on desktop).

---

## 9. Untested — watch for these on the first run

The APK has never been run on a device. All 209 core source files compile cleanly, but
that's a different claim. If it misbehaves, these are the prime suspects:

1. **Threading.** Fights run on their own `Turn` thread that mutates scene2d actors
   directly. The desktop build tolerates it — the render loop quietly swallows the
   exceptions — and Android's stricter GL threading may not.
2. **Sound loading.** `SoundPlayer` loads all 34 sounds into `SoundPool` at startup,
   including one 3.9 MB WAV. SoundPool is happiest under about 1 MB per sound, so some
   effects may silently fail.
3. **Layout.** A modern phone is far wider than the 640×520 the fight UI was laid out
   for, so the profile row and the ability list will look stretched even in landscape.
4. `Turn.stop()` calls `Thread.stop()`, which throws on Android 8+. Nothing calls it
   today, so it's only a trap for later.

---

## 10. Adding audio later

Sound and music files are listed explicitly in
`core/src/com/obtuse/game/audio/AudioAssets.java`. They used to be discovered by walking
the assets folder, but a browser has no folder to walk, so the list is now the source of
truth. **Add or rename an audio file and you must add it there too**, with its extension.
The lookup key is the path minus the extension, so `fight/abilities/bloodCast.wav` plays
as `SoundPlayer.play("fight/abilities/bloodCast")`.

## 11. Housekeeping

`_to_delete/` holds files the conversion moved aside (the `armeabi` natives, the bullet
`.so`s) plus transfer scratch. Nothing in the app refers to it — delete the folder.

Worth doing early: `git init && git add . && git commit`. There's no version history for
this project anywhere, and you're about to start changing it.
