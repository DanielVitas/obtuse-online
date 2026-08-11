package com.obtuse.game.gameobjects.world.interactive;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.UI.touch.TouchIcons;
import com.obtuse.game.gameobjects.world.WorldInteractive;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.maingame.fight.Fight;
import com.obtuse.game.screens.FightScreen;
import com.obtuse.game.screens.LootScreen;
import com.obtuse.game.screens.MyScreen;

import static com.obtuse.game.Obtuse.changeScreen;

public abstract class WorldEnemy extends WorldInteractive {
    public boolean active = true;
    protected Fight fight = new Fight() {
        @Override
        public void end(boolean victory) {
            endFight(victory);
        }
    };

    public WorldEnemy(String name, float defaultFD, float x, float y, float width, float height) {
        super("enemies/" + name, defaultFD, x, y, width, height);
    }

    public void endFight(boolean victory) {
        if (victory) {
            active = false;
            this.remove();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    ((LootScreen) MyScreen.screens.get("LootScreen")).generateLoot("chest", fight.rewards);
                    changeScreen("LootScreen");
                }
            });
        } else {
            changeScreen("WorldScreen");
        }
    }

    @Override
    public boolean interact() {
        ((FightScreen) MyScreen.screens.get("FightScreen")).generateFight(fight);
        changeScreen("FightScreen");
        return true;
    }

    @Override
    public String actionIcon() {
        return active ? TouchIcons.FIGHT : null;
    }
}
