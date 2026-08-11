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
        if (music != null) {
            music.stop();
            music.dispose();
        }
        playing = name;
        // On the web, a track that was not bundled cannot be loaded (readBytes would fail on
        // the missing file). Skip it rather than crash. null shippedTracks = every track is
        // available (desktop/Android), so this guard is a no-op there.
        if (shippedTracks != null && !shippedTracks.contains(name))
            return;
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
