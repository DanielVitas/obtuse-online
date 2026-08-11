package com.obtuse.game.maingame.world;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.obtuse.game.audio.MusicPlayer;
import com.obtuse.game.bodies.GameBody;
import com.obtuse.game.gameobjects.world.WorldObject;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.maingame.GameStage;
import com.obtuse.game.maingame.Level;
import com.obtuse.game.screens.MyScreen;

public abstract class WorldLevel extends Level {
    protected int area = 0;
    protected String[] musicNames;

    public WorldLevel(MyScreen screen) {
        super(screen);
        musicNames = installMusicNames();
    }

    protected abstract String[] installMusicNames();

    public void changeMusic() {
        if (!MusicPlayer.playing.equals(musicNames[area])) {
            MusicPlayer.play(musicNames[area]);
        }
    }

    public void getArea() {
        for (GameStage stage : stages)
            if (stage instanceof WorldStage)
                if (((WorldStage) stage).check(main.getX(), main.getY())) {
                    area = ((WorldStage) stage).area;
                    break;
                }
    }

    @Override
    public void run() {
        getArea();
        changeMusic();
    }

    /**
     * Snap the world camera onto the player. The per-frame follow runs in loop() AFTER the frame is
     * drawn, so on the first frame back from a fight/inventory the camera would otherwise show the
     * spot refreshLayout() recentered it to (a one-frame flash). Called from WorldScreen.show().
     */
    public void centerCamera() {
        if (main != null && main.body != null && main.body.body != null)
            camera(0).position.set(main.body.body.getWorldCenter().x, main.body.body.getWorldCenter().y, 0);
    }

    @Override
    public void clear() {
        super.clear();
        for (GameStage gameStage : stages)
            for (Actor actor : gameStage.stage.getActors()) {
                actor.remove();
            }
    }
}
