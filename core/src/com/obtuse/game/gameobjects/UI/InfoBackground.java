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
    /** Dark text, readable on the light body. */
    public static final Color TEXT = new Color(0.13f, 0.13f, 0.16f, 1f);

    public InfoBackground(String name, float defaultFD) {
        super();
        path += "UI/info/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }

    /** Height of the title strip (name + stat) at the top of the tooltip. */
    public float titleHeight() {
        return getHeight() * 0.22f;
    }

    /**
     * Lay out the standard tooltip: name in the title strip (left), a stat (HP/PP) to its right,
     * and a description filling the body below. Any of stat/description may be null. All dark text.
     */
    public void layoutTooltip(Label name, Label stat, Label description) {
        float x = getX(), y = getY(), w = getWidth(), h = getHeight(), th = titleHeight();
        float pad = w * 0.02f;
        name.setColor(TEXT);
        name.setWidth(w * 0.62f - pad);
        name.setHeight(th);
        name.setAlignment(Align.left);
        name.setPosition(x + pad, y + h - th);
        if (stat != null) {
            stat.setColor(TEXT);
            stat.setWidth(w * 0.34f);
            stat.setHeight(th);
            stat.setAlignment(Align.right);
            stat.setPosition(x + w - w * 0.34f - pad, y + h - th);
        }
        if (description != null) {
            description.setColor(TEXT);
            description.setWidth(w - 2 * pad);
            description.setWrap(true);
            description.setAlignment(Align.topLeft);
            description.setHeight(h - th - pad);
            description.setPosition(x + pad, y);
        }
    }

    /**
     * Place this tooltip box next to the pointer (mouse on desktop, the held touch on mobile —
     * both come from Gdx.input.getX/getY), offset so it doesn't sit under the cursor, and clamped
     * to stay fully on screen. Call this right after create()/setSize() and BEFORE positioning the
     * info labels, since they are laid out relative to this box's x/y. Screen-space info stage.
     */
    public void positionAtPointer(Stage stage) {
        Vector2 v = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        stage.screenToStageCoordinates(v);
        float pad = Math.min(getWidth(), getHeight()) * 0.2f;
        float x = v.x + pad;                  // to the right of the pointer
        float y = v.y - getHeight() - pad;    // box grows upward from y, so drop it below the pointer
        // Keep the whole box (which also draws its title just above the top edge) on screen.
        float margin = getHeight() * 0.12f;
        x = Math.max(margin, Math.min(x, Obtuse.width - getWidth() - margin));
        y = Math.max(margin, Math.min(y, Obtuse.height - getHeight() - margin));
        setPosition(x, y);
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Solid very-light-grey body (no sprite art), a silver frame around the title strip, and a
        // silver frame around the whole window. Do NOT call super.draw() — we replace the art.
        float x = getX(), y = getY(), w = getWidth(), h = getHeight(), th = titleHeight();
        float t = Math.min(w, h) * 0.02f;
        Border.fillRect(batch, x, y, w, h, BG, parentAlpha);
        Border.drawRect(batch, x, y + h - th, w, th, t, Border.SILVER, parentAlpha);
        Border.drawRect(batch, x, y, w, h, t, Border.SILVER, parentAlpha);
    }
}
