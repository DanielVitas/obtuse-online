package com.obtuse.game.gameobjects.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.BasicObject;

public abstract class InfoBackground extends BasicObject {
    /** Light text (stats/description) on the charcoal box. */
    public static final Color TEXT = Border.ROYAL_TEXT;
    /** Red name/title, readable on the dark box. */
    public static final Color NAME = new Color(0.937f, 0.427f, 0.427f, 1f); // #ef6d6d

    public InfoBackground(String name, float defaultFD) {
        super();
        path += "UI/info/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }

    /**
     * Place this tooltip box next to the pointer (mouse on desktop, the held touch on mobile —
     * both come from Gdx.input.getX/getY), offset so it doesn't sit under the cursor, and clamped
     * to stay fully on screen. Height must already be set. Screen-space info stage.
     */
    public void positionAtPointer(Stage stage) {
        Vector2 v = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        stage.screenToStageCoordinates(v);
        float pad = Math.min(getWidth(), getHeight()) * 0.2f;
        float x = v.x + pad;                // to the right of the pointer
        float y = v.y - getHeight() - pad;  // box grows upward from y, so drop it below the pointer
        float margin = getHeight() * 0.06f;
        x = Math.max(margin, Math.min(x, Obtuse.width - getWidth() - margin));
        y = Math.max(margin, Math.min(y, Obtuse.height - getHeight() - margin));
        setPosition(x, y);
    }

    /**
     * Lay out and size the tooltip to fit its content, and place it at the pointer. The name (red)
     * sits top-left inside a tight silver frame; inlineStat (e.g. PP) goes on the name's line at the
     * right; belowStat (e.g. HP + SPD) goes on the line under the name; description fills the rest.
     * Any of inlineStat/belowStat/description may be null. Call BEFORE adding the labels to the stage.
     */
    public void buildTooltip(Stage stage, Label name, Label inlineStat, Label belowStat, Label description) {
        prep(name, NAME);
        prep(inlineStat, TEXT);
        prep(belowStat, TEXT);

        // Padding proportional to the TEXT height (not the surface width) so the tooltip keeps the
        // same look at any screen size or aspect ratio — the font scales with height, so the padding
        // must too, otherwise a wide/short screen gives huge padding around tiny text and vice versa.
        // Sit the text a clear margin INSIDE the gold frame: start from the frame thickness (matches
        // draw()) and add a gap, so the text's left edge never hugs the inner border.
        float frame = Math.max(2f, Obtuse.height * 0.006f);
        float pad = frame + Math.max(4f, height(name) * 0.5f);
        float gap = pad * 0.55f;
        float maxW = Obtuse.width * 0.9f;

        float titleLineW = width(name) + (inlineStat != null ? gap + width(inlineStat) : 0);
        float longest = Math.max(titleLineW, belowStat != null ? width(belowStat) : 0);
        float w = Math.min(maxW, Math.max(getWidth(), longest + 2 * pad));
        setWidth(w);
        float innerW = w - 2 * pad;

        // If a single line is still too wide (box capped at the screen), shrink that line's font.
        if (titleLineW > innerW && titleLineW > 0) {
            float s = innerW / titleLineW;
            scale(name, s);
            scale(inlineStat, s);
        }
        fitWidth(belowStat, innerW);

        float titleH = height(name);
        if (inlineStat != null) titleH = Math.max(titleH, height(inlineStat));
        float belowH = belowStat != null ? height(belowStat) : 0;

        float descH = 0;
        if (description != null) {
            description.setColor(TEXT);
            description.setFontScale(1f);
            description.setWrap(true);
            description.setAlignment(Align.topLeft);
            description.setWidth(innerW);
            descH = description.getPrefHeight();
            // If the whole box would be taller than the screen, shrink the description to fit.
            float maxH = Obtuse.height * 0.92f;
            float fixedH = 2 * pad + titleH + (belowStat != null ? gap + belowH : 0) + gap;
            if (fixedH + descH > maxH && descH > 0) {
                description.setFontScale(Math.max(0.5f, (maxH - fixedH) / descH));
                description.setWidth(innerW);
                descH = description.getPrefHeight();
            }
            description.setHeight(descH);
        }

        float h = pad + titleH
                + (belowStat != null ? gap + belowH : 0)
                + (description != null ? gap + descH : 0)
                + pad;
        setHeight(h);
        positionAtPointer(stage);

        float x = getX(), y = getY(), top = y + h;
        float nameY = top - pad - titleH;
        name.setPosition(x + pad, nameY + (titleH - height(name)) / 2f);

        if (inlineStat != null)
            inlineStat.setPosition(x + w - pad - width(inlineStat),
                    nameY + (titleH - height(inlineStat)) / 2f);

        float cursorY = nameY;
        if (belowStat != null) {
            cursorY = nameY - gap - belowH;
            belowStat.setPosition(x + pad, cursorY);
        }
        if (description != null)
            description.setPosition(x + pad, cursorY - gap - descH);
    }

    private static void prep(Label label, Color colour) {
        if (label == null) return;
        label.setColor(colour);
        label.setWrap(false);
        label.setFontScale(1f);
        label.pack();
    }

    private static float width(Label label) {
        return label == null ? 0 : label.getWidth();
    }

    private static float height(Label label) {
        return label == null ? 0 : label.getHeight();
    }

    private static void scale(Label label, float s) {
        if (label == null) return;
        label.setFontScale(label.getFontScaleX() * s);
        label.pack();
    }

    /** Shrink a single-line label's font so its width fits maxWidth. */
    private static void fitWidth(Label label, float maxWidth) {
        if (label == null) return;
        label.pack();
        if (label.getWidth() > maxWidth && label.getWidth() > 0) {
            label.setFontScale(maxWidth / label.getWidth());
            label.pack();
        }
    }

    public void create(float x, float y, float width, float height) {
        setWidth(width);
        setHeight(height);
        setPosition(x, y);
    }

    public void create(float x, float y) {
        create(x, y, getWidth(), getHeight());
    }

    public float play(String name) {
        float f = play(name, 0);
        play("default", 0, f - 0.02f);
        return f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Royal-gold box (charcoal panel, gold frame + inner ring, bright corner brackets). Do NOT
        // call super.draw() — we replace the sprite art. Thickness is a CONSTANT screen fraction so
        // the border reads the same on a small stat tooltip and a tall description one.
        float t = Math.max(2f, Obtuse.height * 0.006f);
        Border.drawGoldBox(batch, getX(), getY(), getWidth(), getHeight(), t, parentAlpha, 0);
    }
}
