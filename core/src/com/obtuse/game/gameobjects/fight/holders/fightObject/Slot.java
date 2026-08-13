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
        // Draw the animations back-to-front: the burning overlay is appended last, so drawing in reverse
        // puts the fire BEHIND the base state (index 0) — keeping the target (red) / on-turn (dark) colour
        // visible on a slot that's on fire, instead of the flames covering it.
        for (int i = currentlyDisplayed.size - 1; i >= 0; i--)
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
