package com.obtuse.game.teavm;

import com.badlogic.gdx.audio.Music;
import com.github.xpenatan.gdx.teavm.backends.web.assetloader.AssetDownloadImpl;
import com.github.xpenatan.gdx.teavm.backends.web.assetloader.AssetLoaderListener;
import com.github.xpenatan.gdx.teavm.backends.web.assetloader.AssetType;
import com.github.xpenatan.gdx.teavm.backends.web.assetloader.WebBlob;
import com.obtuse.game.audio.MusicPlayer;
import java.util.HashMap;
import java.util.Map;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Web implementation of {@link MusicPlayer.Backend}. No music is in the initial preload; each
 * track is downloaded on demand straight to bytes and played through {@link DownloadedMusic}
 * (Howl), which avoids gdx-teavm's broken runtime file-system download path. Downloaded tracks
 * are cached, so re-entering a fight is instant. While a not-yet-cached track downloads, the
 * previous track keeps playing and a loading overlay (see index.html) is shown.
 */
public class WebMusicBackend implements MusicPlayer.Backend {
    private final AssetDownloadImpl downloader = new AssetDownloadImpl(false);
    private final Map<String, Music> cache = new HashMap<String, Music>();
    private Music current;
    private String targetPath = "";

    @Override
    public void play(final String filePath, final float volume) {
        targetPath = filePath;
        Music cached = cache.get(filePath);
        if (cached != null) {
            switchTo(cached, volume);
            loading(false);
            return;
        }
        // Not downloaded yet. Keep the current track playing; only show the loading overlay if
        // something is already playing (i.e. a real switch — not the very first track at startup).
        loading(current != null);
        downloader.load(true, "assets/" + filePath, AssetType.Binary, new AssetLoaderListener<WebBlob>() {
            @Override
            public void onSuccess(String url, WebBlob blob) {
                Music m;
                try {
                    m = new DownloadedMusic(blob.getData());
                } catch (Throwable t) {
                    System.out.println("OBTUSE music decode failed: " + filePath + " " + t);
                    loading(false);
                    return;
                }
                cache.put(filePath, m);
                System.out.println("OBTUSE music ready: " + filePath);
                if (filePath.equals(targetPath)) {
                    switchTo(m, volume);
                    loading(false);
                }
            }
            @Override
            public void onFailure(String url) {
                System.out.println("OBTUSE music download FAILED: " + filePath);
                loading(false); // keep whatever is currently playing
            }
        });
    }

    private void switchTo(Music m, float volume) {
        if (current != null && current != m)
            current.stop();
        current = m;
        m.setVolume(volume);
        m.setLooping(true);
        m.play();
    }

    /** Show/hide the loading overlay declared in index.html. No-op if the element is absent. */
    private void loading(boolean show) {
        HTMLElement el = HTMLDocument.current().getElementById("music-loading");
        if (el != null)
            el.getStyle().setProperty("display", show ? "flex" : "none");
    }
}
