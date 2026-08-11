package com.obtuse.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.maingame.loot.LootGame;

public class LootScreen extends MyScreen {

    public LootScreen(String name) {
        super(name);
        addStage();
        for (int i = 0; i <= 0; i++)
            fixCamera(i);
        gameGame = new LootGame(this);
    }

    @Override
    public void dialog(Dialog someDialog) {

    }

    public void generateLoot(String type, Choice choice) {
        resetStages();
        ((LootGame) gameGame).setup(type, choice);
    }

    @Override
    public void create() {

    }

    @Override
    protected void loop() {
        gameGame.run();
    }
}
