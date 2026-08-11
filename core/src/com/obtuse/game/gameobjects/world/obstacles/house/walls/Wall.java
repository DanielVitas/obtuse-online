package com.obtuse.game.gameobjects.world.obstacles.house.walls;

import com.obtuse.game.gameobjects.world.WorldObstacle;

public class Wall extends WorldObstacle {

    public Wall(float x, float y) {
        super("house/walls/classicWall", 1f, x, y, 2f, 2f);
    }
}
