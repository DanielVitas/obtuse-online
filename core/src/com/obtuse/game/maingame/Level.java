package com.obtuse.game.maingame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.bodies.GameBody;
import com.obtuse.game.gameobjects.world.WorldObject;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.screens.MyScreen;

public abstract class Level {
    public Array<GameStage> stages = new Array<GameStage>();
    private MyScreen screen;
    public WorldObject main;

    public Level(MyScreen screen) {
        this.screen = screen;
    }

    public void clear() {
        for (GameStage gameStage : stages)
            gameStage.stage.clear();
    }

    public abstract void run();
    public abstract void basic();

    public void add(GameStage stage) {
        stages.add(stage);
    }

    public void create(){
        clear();
        basic();
        for (GameStage stage : stages) {
            stage.create();
        }
    }

    public void dispose() {
        for (GameStage stage : stages)
            stage.dispose();
    }

    public Stage stage(int index) {
        return screen.stage(index);
    }

    protected OrthographicCamera camera(int index) {
        return screen.camera(index);
    }
}
