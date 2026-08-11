package com.obtuse.game.gameobjects.world.obstacles.house;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.bodies.boxes.Box;
import com.obtuse.game.gameobjects.world.WorldObstacle;

public class Bookshelf extends WorldObstacle {

    public Bookshelf(float x, float y) {
        super("house/bookshelf", 1f, x, y, 1f, 1f);
        setBody(new Box(x, y, BodyDef.BodyType.StaticBody, getWidth(), getHeight() - 12f / 16));
    }
}
