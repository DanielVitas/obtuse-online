package com.obtuse.game.gameobjects.UI;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Timer;
import com.obtuse.game.gameobjects.BasicObject;

public abstract class SimpleArrow extends BasicObject {

    public SimpleArrow(String name, float defaultFD, float hoveredFD, float clickedFD) {
        super();
        path += "UI/arrows/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        addAnimation("hovered", hoveredFD, Animation.PlayMode.LOOP);
        addAnimation("clicked", clickedFD, Animation.PlayMode.NORMAL);
        currentlyDisplayed.add(animations.get("default"));
    }

    public float play(String name) {
        float f = play(name,0);
        play("default",0,f - 0.02f);
        return f;
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
