package com.obtuse.game.gameobjects.world.obstacles.house;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.bodies.boxes.Box;
import com.obtuse.game.gameobjects.world.WorldObstacle;

public class Bed extends WorldObstacle {

    public Bed(float x, float y) {
        super("house/bed", 1f, x, y, 1f, 24f / 16);
        setBody(new Box(x, y, BodyDef.BodyType.StaticBody, getWidth(), getHeight() - 4f / 16));
    }
}
