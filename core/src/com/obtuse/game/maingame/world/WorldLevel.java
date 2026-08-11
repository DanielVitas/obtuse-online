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

    @Override
    public void clear() {
        super.clear();
        for (GameStage gameStage : stages)
            for (Actor actor : gameStage.stage.getActors()) {
                actor.remove();
            }
    }
}
