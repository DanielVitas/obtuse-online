package com.obtuse.game.screens;

import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.maingame.fight.Fight;
import com.obtuse.game.maingame.fight.FightGame;
import com.obtuse.game.maingame.loot.LootGame;

public class FightScreen extends MyScreen {

    public FightScreen(String name) {
        super(name);
        addStage(); // fight stage
        addStage(); // info stage
        for (int i = 0; i <= 1; i++)
            fixCamera(i);
        gameGame = new FightGame(this);
    }

    @Override
    public void dialog(Dialog someDialog) {

    }

    public void generateFight(Fight fight) {
        resetStages();
        ((FightGame) gameGame).setup(fight);
    }

    @Override
    public void create() {

    }

    @Override
    protected void loop() {
        gameGame.run();
    }
}
