package com.obtuse.game.maingame.fight.levels.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Summon;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.GameStage;
import com.obtuse.game.maingame.fight.Arena;

public class ArenaStage extends GameStage {
    private Arena arena;

    public ArenaStage(Stage stage) {
        super(stage);
    }

    public void switchArena(Arena arena) {
        stage.clear();
        this.arena = arena;
        //createAndAdd();
    }

    public void addHealthBar(Holder holder) {
        if (holder.healthBar != null) {
            holder.setHealthBarPosition();
            add(holder.healthBar);
        }
    }

    public void createAndAdd() {
        for (Array<Holder> holderArray : new Array[]{arena.heroHolders, arena.enemyHolders, arena.summonHolders})
            for (Holder holder : holderArray)
                holder.createAndAdd(stage);
    }

    @Override
    protected void createLights() {

    }

    @Override
    protected void createEnvironment() {

    }
}
