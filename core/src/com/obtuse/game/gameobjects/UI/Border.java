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
 * units on the inventory/world camera, pixels on a screen-space info stage). Optionally dashed.
 */
public class Border extends Actor {
    /** Muted, grayish gold for the battle move boxes. */
    public static final Color GOLD = new Color(0.68f, 0.62f, 0.46f, 1f);
    /** Silver-grey for tooltips and inventory slots. */
    public static final Color SILVER = new Color(0.70f, 0.73f, 0.78f, 1f);
    private static TextureRegion pixel;

    private final float thickness;
    private final boolean dashed;

    public Border(float x, float y, float width, float height, float thickness) {
        this(x, y, width, height, thickness, GOLD, false);
    }

    public Border(float x, float y, float width, float height, float thickness, Color color, boolean dashed) {
        setBounds(x, y, width, height);
        this.thickness = thickness;
        this.dashed = dashed;
        setColor(color);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (dashed) {
            // Dash/gap scaled to the box so it reads as dashes at any size.
            float dash = Math.max(getWidth(), getHeight()) * 0.06f;
            drawDashedRect(batch, getX(), getY(), getWidth(), getHeight(), thickness, getColor(), dash, dash, parentAlpha);
        } else {
            drawRect(batch, getX(), getY(), getWidth(), getHeight(), thickness, getColor(), parentAlpha);
        }
    }

    private static TextureRegion pixel() {
        if (pixel == null)
            pixel = Obtuse.textureAtlas.findRegion("world/obstacles/whiteBox/default/main");
        return pixel;
    }

    /** Draw a rectangle outline (four thin quads). Restores opaque white. */
    public static void drawRect(Batch batch, float x, float y, float w, float h,
                                float thickness, Color color, float parentAlpha) {
        TextureRegion p = pixel();
        if (p == null) return;
        float t = thickness;
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(p, x, y, w, t);         // bottom
        batch.draw(p, x, y + h - t, w, t); // top
        batch.draw(p, x, y, t, h);         // left
        batch.draw(p, x + w - t, y, t, h); // right
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /** Fill a solid rectangle. Restores opaque white. */
    public static void fillRect(Batch batch, float x, float y, float w, float h, Color color, float parentAlpha) {
        TextureRegion p = pixel();
        if (p == null) return;
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(p, x, y, w, h);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /** Draw a dashed rectangle outline. Restores opaque white. */
    public static void drawDashedRect(Batch batch, float x, float y, float w, float h, float thickness,
                                      Color color, float dash, float gap, float parentAlpha) {
        TextureRegion p = pixel();
        if (p == null) return;
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        float t = thickness, step = dash + gap;
        // bottom + top (dashes run along x)
        for (float dx = 0; dx < w; dx += step) {
            float len = Math.min(dash, w - dx);
            batch.draw(p, x + dx, y, len, t);
            batch.draw(p, x + dx, y + h - t, len, t);
        }
        // left + right (dashes run along y)
        for (float dy = 0; dy < h; dy += step) {
            float len = Math.min(dash, h - dy);
            batch.draw(p, x, y + dy, t, len);
            batch.draw(p, x + w - t, y + dy, t, len);
        }
        batch.setColor(1f, 1f, 1f, 1f);
    }
}
