package com.obtuse.game.maingame.loot;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.Level;
import com.obtuse.game.screens.MyScreen;

public abstract class LootLevel extends Level {
    public Array<GameButton> buttons = new Array<GameButton>();

    public LootLevel(MyScreen screen) {
        super(screen);
    }

    public abstract void setup(Choice choice);
    public abstract void gatherInfo(Item item);
    public abstract void loseInfo();
}
