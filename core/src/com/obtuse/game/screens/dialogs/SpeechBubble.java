package com.obtuse.game.screens.dialogs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.obtuse.game.Fonts;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.UI.InfoBackground;
import com.obtuse.game.gameobjects.UI.dialog.SpeechBubbleBackground;
import com.obtuse.game.gameobjects.world.WorldObject;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.maingame.world.WorldGame;
import com.obtuse.game.screens.Dialog;
import com.obtuse.game.screens.MyScreen;
import com.obtuse.game.screens.WorldScreen;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.w;

/**
 * A speech bubble that pops up above a world character, shows the text, and disappears on its own
 * after a few seconds (duration-based Dialog — no key press needed). The world is paused while it
 * is up so the bubble stays put above the speaker.
 */
public abstract class SpeechBubble extends Dialog {
    private final WorldObject speaker;
    private SpeechBubbleBackground bubble;

    public SpeechBubble(WorldObject speaker, String text, float duration) {
        super("", text, duration);
        this.speaker = speaker;
    }

    @Override
    public void show() {
        GameWorld.pause = true;
        WorldScreen screen = (WorldScreen) MyScreen.game.getScreen();
        OrthographicCamera cam = screen.camera(0);

        // Speaker's position as a fraction of the visible world, so we can place the bubble on the
        // screen-space dialog stage right above their head.
        Vector2 wc = speaker.body.body.getWorldCenter();
        float fx = (wc.x - (cam.position.x - cam.viewportWidth / 2f)) / cam.viewportWidth;
        float fy = (wc.y - (cam.position.y - cam.viewportHeight / 2f)) / cam.viewportHeight;

        float bw = w(0.5f), bh = h(0.2f);
        float bx = fx * Obtuse.width - bw / 2f;      // centred above the speaker
        float by = fy * Obtuse.height + h(0.03f);    // tail tip just above the speaker
        bx = Math.max(w(0.01f), Math.min(bx, Obtuse.width - bw - w(0.01f)));
        by = Math.min(by, Obtuse.height - bh - h(0.01f));

        bubble = new SpeechBubbleBackground();
        bubble.setBounds(bx, by, bw, bh);
        screen.stage(1).addActor(bubble);

        float tailH = bh * 0.22f, pad = bw * 0.04f;
        Label textLabel = new Label(text, Fonts.get("worldDialog"));
        textLabel.setColor(InfoBackground.TEXT);
        textLabel.setWidth(bw - 2 * pad);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        textLabel.setHeight(bh - tailH - 2 * pad);
        textLabel.setPosition(bx + pad, by + tailH + pad);
        screen.stage(1).addActor(textLabel);
        labels.add(textLabel);
    }

    @Override
    public void hide() {
        for (Label label : labels)
            label.remove();
        if (bubble != null)
            bubble.remove();
        WorldGame.lastInteract = System.currentTimeMillis();
        GameWorld.pause = false;
    }
}
