package com.obtuse.game.teavm;

import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import com.obtuse.game.Obtuse;
import com.obtuse.game.audio.MusicPlayer;
import java.util.HashSet;
import java.util.Arrays;
import com.obtuse.game.maingame.fight.arenas.BossArena;
import com.obtuse.game.maingame.fight.arenas.DuelArena;
import com.obtuse.game.maingame.fight.arenas.MainArena;
import com.obtuse.game.maingame.fight.arenas.TripleBossArena;
import com.obtuse.game.screens.FightScreen;
import com.obtuse.game.screens.InventoryScreen;
import com.obtuse.game.screens.LootScreen;
import com.obtuse.game.screens.TitleScreen;
import com.obtuse.game.screens.WorldScreen;

/**
 * Browser entry point. Boots the game's ApplicationListener ({@link Obtuse}) onto the
 * page's &lt;canvas&gt;. Width/height 0 means "use all available space" — the game already
 * re-derives every viewport from the surface size in {@code MyScreen.refreshLayout()}, so
 * a fixed size would fight it.
 */
public class TeaVMLauncher {
    public static void main(String[] args) {
        keepReflectivelyLoadedClasses();
        // Only the overworld music is bundled on the web (see TeaVMBuilder's asset filter),
        // to keep the first load small. MusicPlayer skips any track not listed here instead
        // of trying to load a file that was not shipped.
        MusicPlayer.shippedTracks = new HashSet<String>(Arrays.asList("satieGnossienne", "verdiDiesIrae"));
        WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
        config.width = 0;
        config.height = 0;
        // Request a WebGL2 context. The texture atlas (textureAtlas.png) is 840x840 —
        // non-power-of-two — and is loaded with mipmap filtering. WebGL1 cannot mipmap an
        // NPOT texture, so it renders those sprites black (the beds/wardrobes were black
        // boxes). WebGL2 supports NPOT textures with mipmaps fully, so the atlas renders as
        // it does on desktop/Android — no asset changes needed. Desktop/Android GL always
        // tolerated this; only WebGL1 was strict.
        config.useGL30 = true;
        new WebApplication(new Obtuse(), config);
    }

    // Not a compile-time constant, so javac emits the body below and TeaVM's dead-code
    // elimination cannot drop it — but it never runs.
    private static boolean KEEP = false;

    /**
     * TeaVM only emits classes it can see reached in the call graph. Several classes here
     * are reached ONLY by name at runtime — {@code Obtuse.changeScreen(name)} does
     * {@code Class.forName(...)}, and {@code Fight.arenaClass} is instantiated via
     * {@code getConstructor(...).newInstance(...)}. Without a static reference TeaVM prunes
     * them and {@code Class.forName} throws ClassNotFoundException in the browser (it does
     * not on Android, where the classes are always present). Referencing the constructors in
     * a branch guarded by a non-constant {@code false} keeps each class AND its constructor
     * in the output; the reflection-class globs in {@link TeaVMBuilder} then supply the
     * name→class and constructor metadata that forName/newInstance read. This is the web
     * port's half of the reflection contract CLAUDE.md warns about — the reflection call
     * sites in core are left untouched.
     */
    private static void keepReflectivelyLoadedClasses() {
        if (!KEEP) return;
        // Screens (com.obtuse.game.screens.*), keyed by simple name in Obtuse.changeScreen.
        new WorldScreen(null);
        new TitleScreen(null);
        new FightScreen(null);
        new InventoryScreen(null);
        new LootScreen(null);
        // Arena classes (com.obtuse.game.maingame.fight.arenas.*), built via Fight.arenaClass.
        // Referencing the type keeps the class; the reflection globs keep the (FightGame,
        // Array, Array) constructor metadata.
        Class<?>[] arenas = { MainArena.class, BossArena.class, TripleBossArena.class, DuelArena.class };
        if (arenas.length < 0) System.out.println(arenas.length); // defeat unused-var pruning
    }
}
