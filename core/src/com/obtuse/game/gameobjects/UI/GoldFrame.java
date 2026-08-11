package com.obtuse.game.gameobjects.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * A royal-gold FRAME (no fill) at the actor's bounds — used to outline a grouped area of inventory
 * slots. Lives on a screen-space stage (where the 1x1 pixel tints cleanly, unlike the world camera).
 */
public class GoldFrame extends Actor {

    public GoldFrame(float x, float y, float width, float height) {
        setBounds(x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float t = Math.max(2f, Math.min(getWidth(), getHeight()) * 0.02f);
        Border.drawGoldFrame(batch, getX(), getY(), getWidth(), getHeight(), t, parentAlpha);
    }
}
