package com.obtuse.game.gameobjects.fight.holders.fightObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.obtuse.game.gameobjects.BasicObject;

import static java.lang.Math.pow;

public abstract class Slot extends BasicObject {
    public Holder holder;

    // <1 draws the slot faded. Used for an empty summon slot that's only shown while the player is
    // targeting an ability that can be aimed at it (see FightLevel.tickSummonSlots).
    public float slotAlpha = 1f;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color c = batch.getColor();
        float r = c.r, g = c.g, b = c.b, a = c.a;
        if (slotAlpha < 1f)
            batch.setColor(r, g, b, a * slotAlpha);
        // Draw front-to-back (normal order): the base slot state is index 0, and the burning overlay
        // (appended last, 2x the slot) draws ON TOP so the flames are fully visible over the slot —
        // drawing the fire behind the base clipped its bottom half behind the slot sprite.
        for (int i = 0; i < currentlyDisplayed.size; i++)
            currentlyDisplayed.get(i).draw(batch, getX(), getY(), getWidth(), getHeight());
        if (slotAlpha < 1f)
            batch.setColor(r, g, b, a);
    }

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
