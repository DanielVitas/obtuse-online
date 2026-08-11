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
