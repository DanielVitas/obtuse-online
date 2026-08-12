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
        // Snap the camera onto the player BEFORE the draw so returning from a fight/inventory never
        // shows a frame of the world at the recentered/default camera spot. level.run() (in loop())
        // also snaps, but that happens AFTER the draw, so the first frame back would otherwise be off.
        centerCameraOnPlayer();
        super.render(delta);
        //debugRenderer.render(GameWorld.world, new Matrix4(camera(0).combined));
        GameWorld.step();
    }

    @Override
    public void refreshLayout() {
        super.refreshLayout();
        // fixCamera() (in super) recenters the camera; put it back on the player so a first-frame
        // refreshLayout() after returning to the world doesn't cause a one-frame flash.
        centerCameraOnPlayer();
    }

    @Override
    public void show() {
        super.show();
        GameWorld.pause = false;
        centerCameraOnPlayer();
    }

    private void centerCameraOnPlayer() {
        if (gameGame != null && gameGame.level instanceof com.obtuse.game.maingame.world.WorldLevel)
            ((com.obtuse.game.maingame.world.WorldLevel) gameGame.level).centerCamera();
    }

    @Override
    public void hide() {
        super.hide();
        GameWorld.pause = true;
    }
}
