package com.obtuse.game.gameobjects.world.interactive.enemies;

import com.obtuse.game.gameobjects.fight.enemies.CheshireCat;
import com.obtuse.game.gameobjects.fight.enemies.Demon;
import com.obtuse.game.gameobjects.items.abilityorbs.BloodSacrificeOrb;
import com.obtuse.game.gameobjects.world.interactive.WorldEnemy;
import com.obtuse.game.maingame.fight.arenas.TripleBossArena;

public class BasicDemonCat extends WorldEnemy {

    public BasicDemonCat(float x, float y) {
        super("cheshireCat", 0.2f, x, y, 2f, 2f);
        fight.id("cheshireCat");
        fight.arenaClass = TripleBossArena.class;
        fight.add(new CheshireCat());
        fight.add(new CheshireCat());
        fight.musicName = "verdiDiesIrae";
        fight.rewards.add(new BloodSacrificeOrb());
    }
}
