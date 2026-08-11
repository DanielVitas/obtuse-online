package com.obtuse.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

public class SoundPlayer {
    public static float soundVolume = 0.25f;
    private static String path = "audio/sounds";
    private static Map<String, FileHandle> fileNames = new HashMap<String, FileHandle>();
    private static Map<String, Sound> sounds = new HashMap<String, Sound>();

    public SoundPlayer() {
        addFileNames();
        addSounds();
    }

    public static void play(String name) {
        play(name,0f,1f);
    }

    public static void play(String name, float pan, float volume) {
        if (sounds.containsKey(name)) {
            long id = sounds.get(name).play(volume * soundVolume);
            sounds.get(name).setPan(id, pan, volume * soundVolume);
        }
    }

    private static void addFileNames() {
        // Was a recursive folder walk; browsers have no folder to walk. See AudioAssets.
        for (String file : AudioAssets.SOUNDS)
            fileNames.put(AudioAssets.key(file), Gdx.files.internal(path + "/" + file));
    }

    private static void addSounds() {
        for (String name : fileNames.keySet()) {
            try {
                sounds.put(name, Gdx.audio.newSound(fileNames.get(name)));
            } catch (RuntimeException e) {
                // One sound the platform dislikes should not take the whole game down.
                // Android's SoundPool in particular is unhappy with the larger WAVs.
                Gdx.app.error("OBTUSE", "could not load sound " + name, e);
            }
        }
    }
}
