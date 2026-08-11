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
        stage.dispose();
    }

    protected void add(Actor actor) {
        stage.addActor(actor);
    }

}
