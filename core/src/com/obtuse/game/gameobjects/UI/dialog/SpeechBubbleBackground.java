package com.obtuse.game.gameobjects.UI.dialog;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.obtuse.game.gameobjects.UI.Border;
import com.obtuse.game.gameobjects.UI.InfoBackground;

/**
 * A speech-bubble box: a light-grey rounded-ish panel with a silver frame and a small tail at the
 * bottom-centre pointing down at the speaker. Bounds include the tail; the panel is the top part.
 */
public class SpeechBubbleBackground extends Actor {

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = getX(), y = getY(), w = getWidth(), h = getHeight();
        float t = Math.max(2f, Math.min(w, h) * 0.02f);
        float tailH = h * 0.22f;
        float boxY = y + tailH, boxH = h - tailH;

        Border.fillRect(batch, x, boxY, w, boxH, InfoBackground.BG, parentAlpha);
        // Filled tail tapering down to a point just above the speaker.
        float cx = x + w / 2f, halfBase = w * 0.06f;
        int steps = 8;
        for (int i = 0; i < steps; i++) {
            float f = i / (float) steps;
            float hw = halfBase * (1 - f);
            Border.fillRect(batch, cx - hw, boxY - tailH * f - tailH / steps, hw * 2, tailH / steps + t, InfoBackground.BG, parentAlpha);
        }
        Border.drawRect(batch, x, boxY, w, boxH, t, Border.SILVER, parentAlpha);
    }
}
