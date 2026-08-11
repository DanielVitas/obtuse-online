package com.obtuse.game.maingame.loot;

import com.badlogic.gdx.Gdx;
import com.obtuse.game.Obtuse;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.gameobjects.items.Inventory;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.GameGame;
import com.obtuse.game.maingame.loot.levels.chest.ChestLootLevel;
import com.obtuse.game.screens.MyScreen;

public class LootGame extends GameGame {
    private Choice choice;
    private Item selected;

    public LootGame(MyScreen screen) {
        super(screen);
    }

    public void setup(String type, Choice choice) {
        this.choice = choice;
        if (type.equals("chest")) {
            setLevel(new ChestLootLevel(screen));
        }
        level.create();
        ((LootLevel) level).setup(choice);
    }

    private void clicked(Item item) {
        Inventory.add(item);
        Obtuse.changeScreen("WorldScreen");
    }

    private void hovered(Item item) {
        if (selected != item) {
            selected = item;
            ((LootLevel) level).gatherInfo(item);
        }
    }

    private void loseInfo() {
        if (selected != null) {
            selected = null;
            ((LootLevel) level).loseInfo();
        }
    }


    @Override
    protected void desktopInit() {

    }

    @Override
    protected void androidInit() {

    }

    @Override
    protected void runAll() {

    }

    @Override
    protected void runAndroid() {
        pollTouch();
        if (!touching()) {
            loseInfo();
            return;
        }
        unprojectTouch(0);
        touch : {
            for (int i = 0; i < ((LootLevel) level).buttons.size; i++) {
                GameButton button = ((LootLevel) level).buttons.get(i);
                if (button.check(touchPoint.x, touchPoint.y)) {
                    if (touchReleased)
                        clicked(choice.items.get(i));
                    else
                        hovered(choice.items.get(i));
                    break touch;
                }
            }
            loseInfo();
        }
    }

    @Override
    public void runDesktop() {
        super.runDesktop();
        camera(0).unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        touch : {
            for (int i = 0; i < ((LootLevel) level).buttons.size; i++) {
                GameButton button = ((LootLevel) level).buttons.get(i);
                if (button.check(touchPoint.x, touchPoint.y)) {
                    if (Gdx.input.isTouched())
                        clicked(choice.items.get(i));
                    else
                        hovered(choice.items.get(i));
                    break touch;
                }
            }
            loseInfo();
        }
    }
}
