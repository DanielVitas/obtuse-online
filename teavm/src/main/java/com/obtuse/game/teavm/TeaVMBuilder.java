package com.obtuse.game.teavm;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFilter;
import com.github.xpenatan.gdx.teavm.backends.shared.config.compiler.TeaCompiler;
import com.github.xpenatan.gdx.teavm.backends.shared.config.plugin.TeaReflectionSupplier;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;
import java.io.File;
import org.teavm.tooling.TeaVMSourceFilePolicy;
import org.teavm.tooling.sources.DirectorySourceFileProvider;
import org.teavm.vm.TeaVMOptimizationLevel;

/**
 * Transpiles the game to JavaScript with TeaVM. Run by the Gradle tasks in build.gradle;
 * pass "debug" for source maps / no obfuscation, "run" to serve on Jetty afterwards.
 *
 * <p>Reflection matters here. TeaVM strips anything it can't see reached, but this game
 * looks classes up by string name at runtime in three places, so each must be declared:
 * <ul>
 *   <li>{@code Obtuse.changeScreen(name)} → {@code com.obtuse.game.screens.*}
 *   <li>{@code Fight.arenaClass.newInstance(...)} → {@code com.obtuse.game.maingame.fight.arenas.*}
 *   <li>{@code GameGame.getMethod(name).invoke(...)} for key/button bindings → the *Game
 *       classes and anything else that registers a binding, hence the broad game package.
 * </ul>
 * See CLAUDE.md: "Do not clean up reflection into direct constructor calls without
 * checking the web port's reflection registration."
 */
public class TeaVMBuilder {
    /** The assets to ship, minus the atlas source art and editor files (see addAssets call). */
    private static AssetFileHandle webAssets() {
        AssetFileHandle assets = new AssetFileHandle("../android/assets");
        assets.filter = new AssetFilter() {
            @Override
            public boolean accept(String path) {
                String p = path.replace('\\', '/');
                // Editor/source files — never loaded by the game on any platform.
                if (p.endsWith(".piskel") || p.endsWith(".zip") || p.endsWith(".tps")) return false;
                // Raw art under images/ is packed into atlas/ and loaded from there — except
                // images/world/ (floor, grass, walls are loaded by path). Drop the rest.
                if (p.contains("images/") && !p.contains("images/world/")) return false;
                // Music is COPIED (so it can be fetched at runtime) but kept out of the STARTUP
                // preload — build.gradle strips every audio/music entry from preload.txt, and
                // WebMusicBackend downloads each track on demand. That keeps the first load small
                // without dropping the soundtrack. (Sound EFFECTS stay in the preload; they're
                // small and are read from the FS by the normal SoundPlayer path.)
                return true;
            }
        };
        return assets;
    }

    public static void main(String[] args) {
        boolean debug = false;
        boolean startJetty = false;
        for (String arg : args) {
            if ("debug".equals(arg)) debug = true;
            else if ("run".equals(arg)) startJetty = true;
        }

        // Register reflection targets DIRECTLY with the TeaVM ReflectionSupplier, before the
        // compile. The TeaCompiler.addReflectionClass path only feeds DefaultReflectionListener,
        // whose classpath *glob* scan did not register these classes in this setup (the same
        // gap that left the ../spike's reflection broken). The supplier's own membership test
        // is a plain substring match, so one package prefix covers the whole game. Doing it
        // here — before build() — means the metadata exists during dependency analysis, so
        // TeaVM keeps the reflected constructors/methods (Obtuse.changeScreen's forName, the
        // Fight.arenaClass newInstance, and GameGame's getMethod bindings) instead of pruning
        // them. Classes reached ONLY by name are additionally pinned in TeaVMLauncher.
        TeaReflectionSupplier.addReflectionClass("com.obtuse.game");

        new TeaCompiler(
            new WebBackend()
                .setHtmlWidth(1280)
                .setHtmlHeight(720)
                .setHtmlTitle("Obtuse")
                .setStartJettyAfterBuild(startJetty)
                .setJettyPort(8080)
        )
            // Assets live under android/assets in this project's layout. Ship only what the
            // game loads at runtime: gdx-teavm otherwise PRELOADS the whole tree (~858 files)
            // before the first frame — including ~798 raw source PNGs and ~126 .piskel/.zip
            // editor files that exist only to build atlas/textureAtlas. Over a CDN that many
            // requests stalls the load on a black screen. The game renders everything from
            // atlas/ and reads only a few images/world/ textures (floor, grass, walls) by
            // path, so drop the rest of images/ and the editor files. (Cuts the preload to
            // ~80 files; see grep in git history confirming no images/ outside world/ is
            // loaded directly and that all fight/UI/item art is packed in the atlas.)
            .addAssets(webAssets())

            // SIMPLE optimization for BOTH debug and release. TeaVM 1.5.6's ADVANCED level
            // miscompiles this game: it emits references to methods it then dead-code-strips
            // (e.g. "cbgssa_TemporalAction__init_1 is not defined", "CfX is not defined"), so
            // the page never boots — and it's not reproducible across machines (my local
            // ADVANCED build ran; the identical CI build did not). SIMPLE is what the debug
            // build has always used and it is reliable everywhere. Slightly larger app.js.
            .setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE)
            .setMainClass(TeaVMLauncher.class.getName())
            // Obfuscation off too: TeaVM 1.5.6's obfuscator has the same undefined-symbol bug.
            .setObfuscated(false)
            .setDebugInformationGenerated(debug)
            .setSourceMapsFileGenerated(debug)
            .setSourceFilePolicy(TeaVMSourceFilePolicy.COPY)
            .addSourceFileProvider(new DirectorySourceFileProvider(new File("../core/src/")))
            // Reflection is registered directly with TeaReflectionSupplier at the top of
            // main(), not through the TeaCompiler.addReflectionClass glob path (see there).
            .build(new File("build/dist"));
    }
}
