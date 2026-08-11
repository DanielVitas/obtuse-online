package com.obtuse.game.gameobjects.UI;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.gameobjects.BasicObject;

public abstract class InfoBackground extends BasicObject {

    public InfoBackground(String name, float defaultFD) {
        super();
        path += "UI/info/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
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
