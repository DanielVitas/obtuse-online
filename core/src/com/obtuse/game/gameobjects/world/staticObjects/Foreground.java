package com.obtuse.game.gameobjects.world.staticObjects;

import com.obtuse.game.gameobjects.StaticObject;

import static java.lang.Math.pow;

public abstract class Foreground extends StaticObject {

    public Foreground() {
        super();
        path += "world/static/foreground/";
    }

    @Override
    public float getZ() {
        return (float) -pow(10, 6);
    }
}
