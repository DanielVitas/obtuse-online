package com.obtuse.game.gameobjects.world.obstacles.house.walls;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.bodies.boxes.Box;
import com.obtuse.game.gameobjects.world.WorldObject;

import static java.lang.Math.pow;

public class UpFiller extends WorldObject {

    public UpFiller(float x, float y) {
        path += "obstacles/house/walls/upFiller/";
        setWidth(1f);
        setHeight(1f);
        setPosition(x, y);
        addAnimation("default", 1f, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }

    @Override
    public float getZ() {
        return (float) -pow(10, 6);
    }
}
