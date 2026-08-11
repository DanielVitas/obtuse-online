package com.obtuse.game.maingame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameStage {
    public Stage stage;

    public GameStage(Stage stage) {
        this.stage = stage;
    }

    protected abstract void createLights();
    protected abstract void createEnvironment();

    public void create() {
        createLights();
        createEnvironment();
    }

    public void clear() {
        stage.clear();
    }

    public void dispose() {
        // Do NOT dispose the underlying Stage here. It is owned and REUSED by the Screen
        // (Level.stage(i) just returns screen.stage(i)), and MyScreen.dispose() disposes it for
        // real. Level.dispose() runs on every setLevel() — i.e. every fight/inventory entry — so
        // disposing the Stage destroyed the reused SpriteBatch, and from the 2nd fight onward the
        // arena/inventory sprites drew through a DISPOSED batch: nothing on real mobile GL, though
        // desktop and the emulation tolerated it. Just drop this level's actors instead.
        stage.clear();
    }

    protected void add(Actor actor) {
        stage.addActor(actor);
    }

}
