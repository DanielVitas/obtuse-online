package com.obtuse.game.screens.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.obtuse.game.Fonts;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.UI.DialogBackground;
import com.obtuse.game.gameobjects.UI.dialog.WorldDialogBackground;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.maingame.world.WorldGame;
import com.obtuse.game.screens.Dialog;
import com.obtuse.game.screens.MyScreen;
import com.obtuse.game.screens.WorldScreen;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.w;

public abstract class WorldDialog extends Dialog {

    public WorldDialog(String title, String text) {
        super(title, text);
    }

    public WorldDialog(String title, String text, float duration) {
        super(title, text, duration);
    }

    @Override
    public void show() {
        GameWorld.pause = true;
        background = new WorldDialogBackground();
        ((WorldDialogBackground) background).create(w(0.2f), h(0.7f), w(0.6f), h(0.3f));
        ((WorldScreen) MyScreen.game.getScreen()).stage(1).addActor(background);

        Label titleLabel = new Label(title, Fonts.get("worldDialogTitle"));
        titleLabel.setWidth(background.getWidth());
        titleLabel.setWrap(true);
        titleLabel.setAlignment(Align.topLeft);
        titleLabel.setPosition(background.getX(), background.getY() + background.getHeight() - titleLabel.getHeight());
        ((WorldScreen) MyScreen.game.getScreen()).stage(1).addActor(titleLabel);
        labels.add(titleLabel);

        Label textLabel = new Label(text, Fonts.get("worldDialog"));
        textLabel.setWidth(background.getWidth());
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.topLeft);
        textLabel.setPosition(background.getX(), background.getY() + background.getHeight() - textLabel.getHeight() - titleLabel.getHeight());
        ((WorldScreen) MyScreen.game.getScreen()).stage(1).addActor(textLabel);
        labels.add(textLabel);
    }

    @Override
    public void hide() {
        for (Label label : labels)
            label.remove();
        background.remove();
        WorldGame.lastInteract = System.currentTimeMillis();
        GameWorld.pause = false;
    }
}
