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
     * Web music backend. On the web no music is bundled into the initial preload (it would make
     * the first load huge, and this audio backend can't stream); instead each track is fetched
     * on demand and handed to Howl directly (see the :teavm module). When this is set, all music
     * playback is delegated to it. null on desktop/Android, which take the direct path below and
     * are unaffected.
     */
    public interface Backend {
        /** Fetch the track if needed (caching), stop whatever is playing, and loop this one. */
        void play(String filePath, float volume);
    }
    public static Backend backend = null;

    public MusicPlayer() {
        addFileNames();
    }

    public static void play(String name) {
        play(name,1f);
    }

    public static void play(String name, float volume) {
        final FileHandle fh = fileNames.get(name);
        if (fh == null)
            return;
        playing = name;
        if (backend != null) {
            // Web: the backend downloads on demand and keeps whatever is currently playing until
            // the new track is ready, so it never cuts to silence mid-transition.
            backend.play(fh.path(), volume * musicVolume);
            return;
        }
        if (music != null) {
            music.stop();
            music.dispose();
            music = null;
        }
        music = Gdx.audio.newMusic(fh);
        music.setVolume(volume * musicVolume);
        music.setLooping(true);
        music.play();
    }

    private static void addFileNames() {
        // Was a recursive folder walk; browsers have no folder to walk. See AudioAssets.
        for (String file : AudioAssets.MUSIC)
            fileNames.put(AudioAssets.key(file), Gdx.files.internal(path + "/" + file));
    }
}
