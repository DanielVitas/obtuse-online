package com.obtuse.game.teavm;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
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
            // Assets live under android/assets in this project's layout.
            .addAssets(new AssetFileHandle("../android/assets"))

            .setOptimizationLevel(debug ? TeaVMOptimizationLevel.SIMPLE : TeaVMOptimizationLevel.ADVANCED)
            .setMainClass(TeaVMLauncher.class.getName())
            // Obfuscation is left OFF even for release: with it on, TeaVM 1.5.6's ADVANCED
            // output references an undefined top-level symbol (e.g. "CfX is not defined") and
            // the page never boots. Unobfuscated release still optimizes and runs; the only
            // cost is a larger, readable app.js (dwarfed by the 76 MB of assets anyway).
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
