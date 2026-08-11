package com.obtuse.game.gameobjects.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.bodies.boxes.Box;

public abstract class WorldInteractive extends WorldObject {
    protected String name;

    public WorldInteractive(String name, float deafultFD, float x, float y, float width, float height) {
        super();
        path += "interactive/" + name + "/";
        this.name = name;
        setWidth(width);
        setHeight(height);
        setBody(new Box(x, y, BodyDef.BodyType.StaticBody, width, height));
        addAnimation("default", deafultFD, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }
}
