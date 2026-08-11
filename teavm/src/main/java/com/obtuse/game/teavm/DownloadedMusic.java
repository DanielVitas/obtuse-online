package com.obtuse.game.teavm;

import com.badlogic.gdx.audio.Music;
import com.github.xpenatan.gdx.teavm.backends.web.webaudio.howler.Howl;
import org.teavm.jso.typedarrays.Int8Array;

/**
 * A libGDX {@link Music} backed by a Howl created from raw bytes we downloaded ourselves.
 *
 * <p>The gdx-teavm audio backend (HowlMusic) can only build a Howl from a FileHandle it reads
 * out of the preloaded in-memory file system — there's no way to hand it bytes, and its runtime
 * download path is broken. So for music fetched on demand we build the Howl directly here from
 * the downloaded {@link Int8Array} and implement Music over it. Mirrors HowlMusic's use of the
 * Howl API (play() returns a sound-instance id used by the setters).
 */
public class DownloadedMusic implements Music {
    private final Howl howl;
    private int id = -1;
    private boolean looping = false;
    private float volume = 1f;

    public DownloadedMusic(Int8Array data) {
        this.howl = Howl.create(data);
    }

    @Override
    public void play() {
        if (id >= 0 && howl.isPlaying(id)) return;
        id = howl.play();
        howl.setLoop(looping, id);
        howl.setVolume(volume, id);
    }

    @Override
    public void pause() {
        if (id >= 0) howl.pause(id);
    }

    @Override
    public void stop() {
        howl.stop();
        id = -1;
    }

    @Override
    public boolean isPlaying() {
        return id >= 0 && howl.isPlaying(id);
    }

    @Override
    public void setLooping(boolean isLooping) {
        looping = isLooping;
        if (id >= 0) howl.setLoop(isLooping, id);
    }

    @Override
    public boolean isLooping() {
        return looping;
    }

    @Override
    public void setVolume(float volume) {
        this.volume = volume;
        if (id >= 0) howl.setVolume(volume, id);
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public void setPan(float pan, float volume) {
        setVolume(volume);
    }

    @Override
    public void setPosition(float position) {
        if (id >= 0) howl.setSeek(position, id);
    }

    @Override
    public float getPosition() {
        return id >= 0 ? howl.getSeek(id) : 0f;
    }

    @Override
    public void dispose() {
        howl.stop();
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        // Not needed: game music always loops.
    }
}
