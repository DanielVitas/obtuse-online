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
    /** Very light grey tooltip body. */
    public static final Color BG = new Color(0.90f, 0.90f, 0.92f, 1f);
    /** Dark text (stats/description), readable on the light body. */
    public static final Color TEXT = new Color(0.13f, 0.13f, 0.16f, 1f);
    /** Red name/title. */
    public static final Color NAME = new Color(0.80f, 0.12f, 0.12f, 1f);

    // Tight silver frame around just the name, computed by buildTooltip().
    private boolean hasNameRect = false;
    private float nameRectX, nameRectY, nameRectW, nameRectH;

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
        name.setColor(NAME);
        name.setWrap(false);
        name.pack();
        float titleH = name.getHeight();
        if (inlineStat != null) {
            inlineStat.setColor(TEXT);
            inlineStat.setWrap(false);
            inlineStat.pack();
            titleH = Math.max(titleH, inlineStat.getHeight());
        }
        float belowH = 0;
        if (belowStat != null) {
            belowStat.setColor(TEXT);
            belowStat.setWrap(false);
            belowStat.pack();
            belowH = belowStat.getHeight();
        }

        // Grow the box just enough for the title line (name + inline stat) if needed.
        float padGuess = getWidth() * 0.035f;
        float titleNeeded = padGuess * 2 + name.getWidth()
                + (inlineStat != null ? padGuess * 2 + inlineStat.getWidth() : 0);
        float w = Math.max(getWidth(), titleNeeded);
        setWidth(w);
        float pad = w * 0.035f;
        float innerW = w - 2 * pad;
        float gap = pad * 0.5f;

        float descH = 0;
        if (description != null) {
            description.setColor(TEXT);
            description.setWidth(innerW);
            description.setWrap(true);
            description.setAlignment(Align.topLeft);
            descH = description.getPrefHeight();
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
        name.setPosition(x + pad, nameY + (titleH - name.getHeight()) / 2f);
        // Tight silver frame hugging the name (a little breathing room left/right/top/bottom).
        float nb = pad * 0.35f;
        hasNameRect = true;
        nameRectX = x + pad - nb;
        nameRectY = nameY - nb;
        nameRectW = name.getWidth() + 2 * nb;
        nameRectH = titleH + 2 * nb;

        if (inlineStat != null)
            inlineStat.setPosition(x + w - pad - inlineStat.getWidth(),
                    nameY + (titleH - inlineStat.getHeight()) / 2f);

        float cursorY = nameY;
        if (belowStat != null) {
            cursorY = nameY - gap - belowH;
            belowStat.setPosition(x + pad, cursorY);
        }
        if (description != null)
            description.setPosition(x + pad, cursorY - gap - descH);
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
        // Solid very-light-grey body (no sprite art), a silver frame around the whole window, and a
        // tight silver frame around the name. Do NOT call super.draw() — we replace the art.
        float x = getX(), y = getY(), w = getWidth(), h = getHeight();
        float t = Math.min(w, h) * 0.02f;
        Border.fillRect(batch, x, y, w, h, BG, parentAlpha);
        Border.drawRect(batch, x, y, w, h, t, Border.SILVER, parentAlpha);
        if (hasNameRect)
            Border.drawRect(batch, nameRectX, nameRectY, nameRectW, nameRectH, t, Border.SILVER, parentAlpha);
    }
}
