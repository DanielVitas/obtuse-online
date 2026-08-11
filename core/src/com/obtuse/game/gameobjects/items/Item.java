package com.obtuse.game.gameobjects.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.effects.Effect;
import com.obtuse.game.gameobjects.BasicObject;

import java.util.HashMap;
import java.util.Map;

public abstract class Item extends BasicObject {
    private String displayName;
    protected String description;

    public Item() {
        path += "items/";
        setSize(0.5f, 0.5f);
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return displayName;
    }

    public void setName(String name) {
        displayName = name;
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
