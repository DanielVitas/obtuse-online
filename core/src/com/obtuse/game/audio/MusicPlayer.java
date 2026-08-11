package com.obtuse.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;

public class MusicPlayer {
    public static float musicVolume = 0.5f;
    private static String path = "audio/music";
    private static Map<String, FileHandle> fileNames = new HashMap<String, FileHandle>();
    private static Music music;
    public static String playing = "";
    /**
     * Track names that were actually shipped (preloaded) — used by the web build, where not
     * every music file is bundled to keep the first load small. null means "all tracks are
     * available" (desktop/Android), so those platforms are unaffected. play() silently skips
     * a request for a track that is not shipped instead of failing to load it.
     */
    public static java.util.Set<String> shippedTracks = null;

    public MusicPlayer() {
        addFileNames();
    }

    public static void play(String name) {
        play(name,1f);
    }

    public static void play(String name, float volume) {
        // On the web not every track is bundled (see MusicPlayer.shippedTracks). If this one
        // is not, leave whatever is currently playing alone — DON'T stop it — so the overworld
        // theme keeps playing through fights instead of cutting to silence. Stopping here also
        // left `music` referencing a disposed object; the next play() then disposed it a second
        // time, which froze the game on fight exit. null shippedTracks = all tracks available
        // (desktop/Android), so this is a no-op there.
        if (shippedTracks != null && !shippedTracks.contains(name))
            return;
        if (music != null) {
            music.stop();
            music.dispose();
            music = null;
        }
        playing = name;
        if (fileNames.containsKey(name)) {
            music = Gdx.audio.newMusic(fileNames.get(name));
            music.setVolume(volume * musicVolume);
            music.setLooping(true);
            music.play();
        }
    }

    private static void addFileNames() {
        // Was a recursive folder walk; browsers have no folder to walk. See AudioAssets.
        for (String file : AudioAssets.MUSIC)
            fileNames.put(AudioAssets.key(file), Gdx.files.internal(path + "/" + file));
    }
}
