package com.obtuse.game.screens;

import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.maingame.fight.levels.stages.InfoStage;
import com.obtuse.game.maingame.inventory.InventoryGame;
import com.obtuse.game.maingame.loot.LootGame;

public class InventoryScreen extends MyScreen {

    public InventoryScreen(String name) {
        super(name);
        addStage();
        addStage(); // touch control stage, owned by no level
        for (int i = 0; i <= 0; i++)
            fixCamera(i);
        gameGame = new InventoryGame(this);
    }

    @Override
    public void dialog(Dialog someDialog) {

    }

    public void generateInventory() {
        resetStages();
        ((InventoryGame) gameGame).setup();
    }

    @Override
    public void create() {

    }

    @Override
    protected void loop() {
        gameGame.run();
    }
}
