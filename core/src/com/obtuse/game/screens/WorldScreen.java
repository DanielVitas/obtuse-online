package com.obtuse.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.obtuse.game.gameobjects.UI.dialog.WorldDialogBackground;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.maingame.world.WorldGame;

public class WorldScreen extends MyScreen {
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    public WorldScreen(String name) {
        super(name);
        addStage(); // dialog stage
        addStage(); // touch control stage, owned by no level
        for (int i = 0; i <= 0; i++)
            fixCamera(i);
        gameGame = new WorldGame(this);
    }

    @Override
    public void dialog(Dialog someDialog) {
    }

    @Override
    public void create() {

    }

    @Override
    protected void loop() {
        gameGame.run();
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        //debugRenderer.render(GameWorld.world, new Matrix4(camera(0).combined));
        GameWorld.step();
    }

    @Override
    public void show() {
        super.show();
        GameWorld.pause = false;
        // Put the camera on the player before the first frame is drawn (refreshLayout() in
        // super.show() recentered it), else there's a one-frame flash at the recentered spot.
        if (gameGame != null && gameGame.level instanceof com.obtuse.game.maingame.world.WorldLevel)
            ((com.obtuse.game.maingame.world.WorldLevel) gameGame.level).centerCamera();
    }

    @Override
    public void hide() {
        super.hide();
        GameWorld.pause = true;
    }
}
