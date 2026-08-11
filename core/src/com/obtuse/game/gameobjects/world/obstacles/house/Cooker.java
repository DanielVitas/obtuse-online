package com.obtuse.game.gameobjects.world.obstacles.house;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.gameobjects.world.WorldObstacle;

public class Cooker extends WorldObstacle {

    public Cooker(float x, float y) {
        super("house/cooker", 1f, x, y, 1f, 1f);
        addAnimation("lit",0.2f, Animation.PlayMode.LOOP);
        play("lit",0);
    }
}
