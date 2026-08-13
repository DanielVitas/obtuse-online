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
        if (speaker == null || cam == null)
            return;
        // Centre on the SPEAKER SPRITE's own bounds — getX()/getY() are its bottom-left corner and
        // getWidth()/getHeight() its size, so the horizontal centre is getX()+width/2 and the head is
        // the sprite's top. (The physics body centre used before is offset from the sprite, which is
        // exactly why the bubble sat to the left.)
        float centreWorldX = speaker.getX() + speaker.getWidth() / 2f;
        float headWorldY = speaker.getY() + speaker.getHeight();
        float fx = (centreWorldX - (cam.position.x - cam.viewportWidth / 2f)) / cam.viewportWidth;
        float fy = (headWorldY - (cam.position.y - cam.viewportHeight / 2f)) / cam.viewportHeight;
        float bw = getWidth(), bh = getHeight();
        float bx = fx * Obtuse.width - bw / 2f;                 // tail (bubble centre) points at the sprite's centre
        float by = fy * Obtuse.height + Obtuse.height * 0.06f;  // sitting a little above the head
        // No screen clamping: the bubble is anchored to the speaker's WORLD spot, so it scrolls off
        // with them when the player walks away rather than sticking to the edge and following the player.
        setPosition(bx, by);
        if (text != null) {
            float tailH = bh * 0.22f, pad = bw * 0.04f;
            text.setPosition(bx + pad, by + tailH + pad);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = getX(), y = getY(), w = getWidth(), h = getHeight();
        float t = Math.max(2f, Obtuse.height * 0.006f); // constant thickness, like the tooltips
        float tailH = h * 0.22f, boxY = y + tailH, boxH = h - tailH;
        float cx = x + w / 2f, halfBase = w * 0.055f;
        int steps = 12;
        // Charcoal fill of the tail (a triangle tapering down to a point above the speaker).
        for (int i = 0; i < steps; i++) {
            float f = i / (float) steps;
            float hw = halfBase * (1 - f);
            float segY = boxY - tailH * (i + 1) / steps;
            Border.fillRect(batch, cx - hw, segY, hw * 2, tailH / steps + t, Border.ROYAL_CHARCOAL, parentAlpha);
        }
        // Gold outline down both slanted edges of the tail so it is framed like the box.
        for (int i = 0; i < steps; i++) {
            float f = i / (float) steps;
            float hw = halfBase * (1 - f);
            float segY = boxY - tailH * (i + 1) / steps;
            float segH = tailH / steps + t;
            Border.fillRect(batch, cx - hw - t, segY, t, segH, Border.ROYAL_GOLD, parentAlpha); // left edge
            Border.fillRect(batch, cx + hw, segY, t, segH, Border.ROYAL_GOLD, parentAlpha);     // right edge
        }
        Border.drawGoldBox(batch, x, boxY, w, boxH, t, parentAlpha, 0);
    }
}
