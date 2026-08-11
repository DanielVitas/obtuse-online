package com.obtuse.game.gameobjects.UI.dialog;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.UI.Border;
import com.obtuse.game.gameobjects.world.WorldObject;

/**
 * A royal-gold speech bubble (charcoal panel, gold frame + corner brackets, with a tail pointing
 * down at the speaker). It TRACKS the speaker each frame — projecting their world position onto the
 * screen-space dialog stage — so the bubble stays above their head while the player walks around.
 */
public class SpeechBubbleBackground extends Actor {
    private WorldObject speaker;
    private OrthographicCamera cam;
    private Label text;

    public void track(WorldObject speaker, OrthographicCamera cam, Label text) {
        this.speaker = speaker;
        this.cam = cam;
        this.text = text;
        reposition();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        reposition();
    }

    private void reposition() {
        if (speaker == null || speaker.body == null || speaker.body.body == null)
            return;
        Vector2 wc = speaker.body.body.getWorldCenter();
        float fx = (wc.x - (cam.position.x - cam.viewportWidth / 2f)) / cam.viewportWidth;
        float fy = (wc.y - (cam.position.y - cam.viewportHeight / 2f)) / cam.viewportHeight;
        float bw = getWidth(), bh = getHeight();
        float bx = fx * Obtuse.width - bw / 2f;                 // centred above the speaker
        float by = fy * Obtuse.height + Obtuse.height * 0.03f;  // tail tip just above them
        bx = Math.max(Obtuse.width * 0.01f, Math.min(bx, Obtuse.width - bw - Obtuse.width * 0.01f));
        by = Math.min(by, Obtuse.height - bh - Obtuse.height * 0.01f);
        setPosition(bx, by);
        if (text != null) {
            float tailH = bh * 0.22f, pad = bw * 0.04f;
            text.setPosition(bx + pad, by + tailH + pad);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = getX(), y = getY(), w = getWidth(), h = getHeight();
        float t = Math.max(2f, Math.min(w, h) * 0.025f);
        float tailH = h * 0.22f, boxY = y + tailH, boxH = h - tailH;
        // Charcoal tail tapering down to a point above the speaker.
        float cx = x + w / 2f, halfBase = w * 0.055f;
        int steps = 8;
        for (int i = 0; i < steps; i++) {
            float f = i / (float) steps;
            float hw = halfBase * (1 - f);
            Border.fillRect(batch, cx - hw, boxY - tailH * f - tailH / steps, hw * 2, tailH / steps + t, Border.ROYAL_CHARCOAL, parentAlpha);
        }
        Border.drawGoldBox(batch, x, boxY, w, boxH, t, parentAlpha, 0);
    }
}
