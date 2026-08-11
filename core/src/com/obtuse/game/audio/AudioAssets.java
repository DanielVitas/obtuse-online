package com.obtuse.game.audio;

/**
 * The audio the game ships with, listed explicitly.
 *
 * SoundPlayer and MusicPlayer used to discover these by walking the assets folder with
 * FileHandle.list(). That works on the desktop and on Android, but a browser has no
 * directory to walk - assets are individually preloaded - so the scan finds nothing and
 * the game runs in silence. Listing them is the only portable option.
 *
 * IF YOU ADD OR RENAME AN AUDIO FILE, ADD IT HERE TOO, with its extension. The lookup
 * key is the path with the extension stripped, so "fight/abilities/bloodCast.wav" is
 * played as SoundPlayer.play("fight/abilities/bloodCast").
 */
public class AudioAssets {

    /** Relative to assets/audio/sounds. */
    public static final String[] SOUNDS = {
            "fight/abilities/bloodCast.wav",
            "fight/abilities/bloodSacrifice/blood.wav",
            "fight/abilities/burningGround/burn.wav",
            "fight/abilities/cast1.wav",
            "fight/abilities/cast2.wav",
            "fight/abilities/cast3.wav",
            "fight/abilities/cast5.mp3",
            "fight/abilities/cast6.wav",
            "fight/abilities/cast7.wav",
            "fight/abilities/delayedHit/mark.wav",
            "fight/abilities/duel/teleportation.wav",
            "fight/abilities/echo/echo.wav",
            "fight/abilities/fireCast.wav",
            "fight/abilities/fireball/explosion.wav",
            "fight/abilities/guard/guard.wav",
            "fight/abilities/shieldBash/bash.wav",
            "fight/abilities/steal/coin.wav",
            "fight/abilities/summonCast.wav",
            "fight/abilities/thunder/cast.wav",
            "fight/abilities/thunder/thunder.wav",
            "fight/abilities/thunder/thunderoriginal.wav",
            "fight/abilities/wooshCast.wav",
            "fight/objects/demon/summon.wav",
            "fight/objects/dummy/explosion.wav",
            "fight/objects/flamingSword/summon.wav",
            "fight/objects/healing.wav",
            "fight/objects/mageKnight/death.wav",
            "fight/objects/mageKnight/hurt.wav",
            "fight/objects/skeleton/death.wav",
            "fight/objects/skeleton/hurt.wav",
            "fight/objects/wizard/death.wav",
            "fight/objects/wizard/hurt.wav",
            "world/misc/woodenChestOpening.wav",
            "world/misc/woodenDoorOpening.wav"
    };

    /** Relative to assets/audio/music. */
    public static final String[] MUSIC = {
            "bachCelloSuiteNo1.mp3",
            "bachToccataAndFugue.mp3",
            "dvorakSymphonyNo9.mp3",
            "marschnerDerVampyr.mp3",
            "orffOFortuna.mp3",
            "saintSaensAquarium.mp3",
            "satieGnossienne.mp3",
            "verdiDiesIrae.mp3"
    };

    /** "fight/abilities/bloodCast.wav" -> "fight/abilities/bloodCast" */
    public static String key(String file) {
        int dot = file.lastIndexOf('.');
        return dot < 0 ? file : file.substring(0, dot);
    }
}
