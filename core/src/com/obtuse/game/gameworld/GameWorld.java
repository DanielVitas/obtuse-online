package com.obtuse.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;

public class GameWorld {
    public static boolean pause = false;
    public static World world;

    public GameWorld() {
        world = new World(new Vector2(0, 0), true);
        setContactListener(new WorldContactListener());
    }

    public static void setContactListener(ContactListener contactListener) {
        world.setContactListener(contactListener);
    }

    public static Body createBody(BodyDef bodyDef) {
        return world.createBody(bodyDef);
    }

    public static void step() {
        if (!pause)
            world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }
}
