package com.obtuse.game.gameobjects.world.staticObjects;

import com.obtuse.game.gameobjects.StaticObject;

import static java.lang.Math.pow;

public abstract class Background extends StaticObject {

    public Background() {
        super();
        path += "world/static/background/";
    }

    @Override
    public float getZ() {
        return (float) pow(10, 6);
    }
}
