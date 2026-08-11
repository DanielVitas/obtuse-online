package com.obtuse.game.gameobjects.world.obstacles.house;

import com.obtuse.game.gameobjects.world.WorldObstacle;

public class Clock extends WorldObstacle {

    public Clock(float x, float y) {
        super("house/clock", 1f, x, y, 1f, 2f);
    }
}
