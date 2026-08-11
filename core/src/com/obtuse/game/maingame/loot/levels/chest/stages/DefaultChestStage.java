package com.obtuse.game.maingame.loot.levels.chest.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.obtuse.game.Obtuse;
import com.obtuse.game.maingame.GameStage;

public class DefaultChestStage extends GameStage {

    public DefaultChestStage(Stage stage) {
        super(stage);
    }

    private void background() {
        Image pile02 = new Image(Obtuse.textureAtlas.findRegion("misc/pilesOfGold/pile02"));
        pile02.setPosition(-0.5f,0f);
        pile02.setSize(2, 2);
        add(pile02);

        Image pile03 = new Image(Obtuse.textureAtlas.findRegion("misc/pilesOfGold/pile03"));
        pile03.setPosition(1f,0);
        pile03.setSize(2, 2);
        add(pile03);


        Image pile01 = new Image(Obtuse.textureAtlas.findRegion("misc/pilesOfGold/pile01"));
        pile01.setPosition(0.3f,0);
        pile01.setSize(2f, 2);
        add(pile01);

        Image pile04 = new Image(Obtuse.textureAtlas.findRegion("misc/pilesOfGold/pile04"));
        pile04.setPosition(8,0);
        pile04.setSize(2, 2);
        add(pile04);
    }

    @Override
    protected void createLights() {

    }

    @Override
    protected void createEnvironment() {
        //background();
    }
}
