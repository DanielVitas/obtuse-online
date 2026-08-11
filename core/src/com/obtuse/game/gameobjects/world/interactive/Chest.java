package com.obtuse.game.gameobjects.world.interactive;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.gameobjects.UI.touch.TouchIcons;
import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.gameobjects.world.WorldInteractive;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.screens.LootScreen;
import com.obtuse.game.screens.MyScreen;

public abstract class Chest extends WorldInteractive {
    protected boolean opened = false;
    protected Choice choice;


    public Chest(String name, float defaultFD, float openingFD, float openedFD, float x, float y, float width, float height) {
        super("chests/" + name, defaultFD, x, y, width, height);
        choice = new Choice();
        addAnimation("opening", openingFD, Animation.PlayMode.NORMAL);
        addAnimation("opened", openedFD, Animation.PlayMode.LOOP);

    }

    private void open() {
        GameWorld.pause = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                play("opened",0);
                ((LootScreen) MyScreen.screens.get("LootScreen")).generateLoot("chest", choice);
                Obtuse.changeScreen("LootScreen");
            }
        }, play("opening", 0));
        opened = true;
    }

    public void add(Item item) {
        choice.add(item);
    }

    public void add(Item item1, Item item2) {
        choice.add(item1, item2);
    }

    public void add(Item item1, Item item2, Item item3) {
        choice.add(item1, item2, item3);
    }

    @Override
    public boolean interact() {
        if (!opened) {
            open();
            return true;
        }
        return false;
    }

    @Override
    public String actionIcon() {
        return opened ? null : TouchIcons.CHEST;
    }

}
