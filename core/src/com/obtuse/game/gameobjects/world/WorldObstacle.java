package com.obtuse.game.gameobjects.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.bodies.boxes.Box;

public abstract class WorldObstacle extends WorldObject {
    private static String defaultPath = "world/obstacles/";

    public WorldObstacle(String name, float defaultFD, float x, float y, float width, float height) {
        super();
        path += "obstacles/" + name + "/";
        setWidth(width);
        setHeight(height);
        setBody(new Box(x, y, BodyDef.BodyType.StaticBody, width, height));
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }
}
