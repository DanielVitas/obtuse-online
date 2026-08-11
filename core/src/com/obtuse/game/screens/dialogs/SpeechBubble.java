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
        // Do NOT pause the world — the player can keep moving; the bubble tracks the speaker.
        WorldScreen screen = (WorldScreen) MyScreen.game.getScreen();
        OrthographicCamera cam = screen.camera(0);

        float bw = w(0.5f), bh = h(0.2f);
        float tailH = bh * 0.22f, pad = bw * 0.04f;

        Label textLabel = new Label(text, Fonts.get("worldDialog"));
        textLabel.setColor(InfoBackground.TEXT);
        textLabel.setWidth(bw - 2 * pad);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);
        textLabel.setHeight(bh - tailH - 2 * pad);
        labels.add(textLabel);

        bubble = new SpeechBubbleBackground();
        bubble.setSize(bw, bh);
        bubble.track(speaker, cam, textLabel);   // positions the bubble + text above the speaker
        screen.stage(1).addActor(bubble);
        screen.stage(1).addActor(textLabel);
    }

    @Override
    public void hide() {
        for (Label label : labels)
            label.remove();
        if (bubble != null)
            bubble.remove();
        WorldGame.lastInteract = System.currentTimeMillis();
    }
}
