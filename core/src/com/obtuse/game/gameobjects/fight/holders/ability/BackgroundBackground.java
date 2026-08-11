package com.obtuse.game.gameobjects.fight.holders.ability;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.gameobjects.BasicObject;

public class BackgroundBackground extends BasicObject {

    public BackgroundBackground(float x, float y, float width, float height) {
        path += "UI/abilities/backgrounds/background/";
        addAnimation("default", 1f, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
        setPosition(x, y);
        setSize(width, height);
    }

    public void create(float x, float y, float width, float height) {
        setWidth(width);
        setHeight(height);
        setPosition(x, y);
    }

    public void create(float x, float y) {
        create(x, y, getWidth(), getHeight());
    }
}
