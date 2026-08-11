package com.obtuse.game.gameobjects.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.obtuse.game.Obtuse;

/**
 * A rectangle OUTLINE drawn with the atlas' 1x1 white pixel (tinted). Four thin quads (top,
 * bottom, left, right) so the box's interior stays untouched — used to frame ability boxes and
 * inventory slots. Thickness is in the same coordinate space as the stage it's added to (world
 * units on the inventory/world camera, pixels on a screen-space info stage).
 */
public class Border extends Actor {
    /** Warm gold/amber, matching the RPG UI. */
    public static final Color GOLD = new Color(1f, 0.80f, 0.30f, 1f);
    private static TextureRegion pixel;
    private final float thickness;

    public Border(float x, float y, float width, float height, float thickness) {
        this(x, y, width, height, thickness, GOLD);
    }

    public Border(float x, float y, float width, float height, float thickness, Color color) {
        setBounds(x, y, width, height);
        this.thickness = thickness;
        setColor(color);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawRect(batch, getX(), getY(), getWidth(), getHeight(), thickness, getColor(), parentAlpha);
    }

    /** Draw a rectangle outline (four thin quads) in the given batch. Restores opaque white. */
    public static void drawRect(Batch batch, float x, float y, float w, float h,
                                float thickness, Color color, float parentAlpha) {
        if (pixel == null)
            pixel = Obtuse.textureAtlas.findRegion("world/obstacles/whiteBox/default/main");
        if (pixel == null)
            return;
        float t = thickness;
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(pixel, x, y, w, t);         // bottom
        batch.draw(pixel, x, y + h - t, w, t); // top
        batch.draw(pixel, x, y, t, h);         // left
        batch.draw(pixel, x + w - t, y, t, h); // right
        // These sprites rely on the batch colour being opaque white afterwards (see render fix).
        batch.setColor(1f, 1f, 1f, 1f);
    }
}
