package com.obtuse.game.gameobjects.fight.holders.fightObject;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.gameobjects.BasicObject;

import static java.lang.Math.pow;

public abstract class Slot extends BasicObject {
    public Holder holder;

    public Slot(String name, float defaultFD, float hoveredFD, float targetedFD, float onTurnFD, float burningFD) {
        path += "fight/slots/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        addAnimation("hovered", hoveredFD, Animation.PlayMode.LOOP);
        addAnimation("targeted", targetedFD, Animation.PlayMode.LOOP);
        addAnimation("onTurn", onTurnFD, Animation.PlayMode.LOOP);
        addAnimation("burning", burningFD, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }

    public void create(float x, float y, float width, float height) {
        setWidth(width);
        setHeight(height);
        setPosition(x, y);
    }

    public float burn() {
        if (currentlyDisplayed.size < 2)
            currentlyDisplayed.add(animations.get("burning"));
        return play("burning",1);
    }

    public void create(float x, float y) {
        create(x, y, getWidth(), getHeight());
    }

    @Override
    public float getZ() {
        return (float) pow(10, 6);
    }
}
