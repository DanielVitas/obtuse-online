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
    // "Royal gold" box palette (the chosen UI style): dark charcoal panel, a gold frame with a
    // darker inner ring, and bright gold corner brackets.
    public static final Color ROYAL_CHARCOAL = new Color(0.098f, 0.102f, 0.133f, 1f); // #191a22
    public static final Color ROYAL_GOLD = new Color(0.79f, 0.635f, 0.294f, 1f);      // #c9a24b
    public static final Color ROYAL_DARKGOLD = new Color(0.427f, 0.333f, 0.122f, 1f); // #6d551f
    public static final Color ROYAL_BRIGHTGOLD = new Color(0.906f, 0.784f, 0.455f, 1f);// #e7c874
    /** Light text on the charcoal panel. */
    public static final Color ROYAL_TEXT = new Color(0.925f, 0.874f, 0.749f, 1f);      // #ecdfbf
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

    /**
     * Draw the "royal gold" box: charcoal fill, gold frame + darker inner ring, and bright gold
     * corner brackets (top-left + bottom-right). state 0 = default, 1 = hovered, 2 = selected
     * (brighter frame). Restores opaque white.
     */
    public static void drawGoldBox(Batch batch, float x, float y, float w, float h, float t,
                                   float parentAlpha, int state) {
        fillRect(batch, x, y, w, h, ROYAL_CHARCOAL, parentAlpha);
        Color frame = state >= 1 ? ROYAL_BRIGHTGOLD : ROYAL_GOLD;
        drawRect(batch, x, y, w, h, t, frame, parentAlpha);
        float g = t * 2f;
        if (w > 2 * g && h > 2 * g)
            drawRect(batch, x + g, y + g, w - 2 * g, h - 2 * g, Math.max(1f, t * 0.6f), ROYAL_DARKGOLD, parentAlpha);
        drawBrackets(batch, x, y, w, h, t, parentAlpha);
    }

    // Bright corner brackets (top-left + bottom-right) sitting ON the inner gold ring (inset by 2t),
    // not the outer edge.
    private static void drawBrackets(Batch batch, float x, float y, float w, float h, float t, float parentAlpha) {
        float g = t * 2f;
        float bx = x + g, by = y + g, bw = w - 2 * g, bh = h - 2 * g;
        float bl = Math.min(bw, bh) * 0.18f, bt = t * 1.5f;
        fillRect(batch, bx, by + bh - bl, bt, bl, ROYAL_BRIGHTGOLD, parentAlpha);       // TL vertical
        fillRect(batch, bx, by + bh - bt, bl, bt, ROYAL_BRIGHTGOLD, parentAlpha);       // TL horizontal
        fillRect(batch, bx + bw - bt, by, bt, bl, ROYAL_BRIGHTGOLD, parentAlpha);       // BR vertical
        fillRect(batch, bx + bw - bl, by, bl, bt, ROYAL_BRIGHTGOLD, parentAlpha);       // BR horizontal
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /**
     * The royal-gold FRAME only (gold border + darker inner ring + bright corner brackets), no
     * charcoal fill — for large panels on the world camera, where stretching the 1x1 white pixel
     * across a big fill bleeds into neighbouring atlas art. Thin frame lines tint cleanly.
     */
    public static void drawGoldFrame(Batch batch, float x, float y, float w, float h, float t, float parentAlpha) {
        drawRect(batch, x, y, w, h, t, ROYAL_GOLD, parentAlpha);
        float g = t * 2f;
        if (w > 2 * g && h > 2 * g)
            drawRect(batch, x + g, y + g, w - 2 * g, h - 2 * g, Math.max(1f, t * 0.6f), ROYAL_DARKGOLD, parentAlpha);
        drawBrackets(batch, x, y, w, h, t, parentAlpha);
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
