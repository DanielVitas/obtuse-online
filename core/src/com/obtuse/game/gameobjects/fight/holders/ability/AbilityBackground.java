package com.obtuse.game.gameobjects.fight.holders.ability;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.gameobjects.BasicObject;

public class AbilityBackground extends BasicObject {

    public AbilityBackground(String name, float defaultFD, float hoveredFD, float selectedFD) {
        path += "UI/abilities/backgrounds/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        addAnimation("hovered", hoveredFD, Animation.PlayMode.LOOP);
        addAnimation("selected", selectedFD, Animation.PlayMode.NORMAL);
        currentlyDisplayed.add(animations.get("default"));
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
