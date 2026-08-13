package com.obtuse.game.gameobjects.world.interactive.enemies;

import com.obtuse.game.gameobjects.fight.enemies.HugeSlime;
import com.obtuse.game.gameobjects.items.abilityorbs.GuardOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.PoisonOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ShieldBashOrb;
import com.obtuse.game.gameobjects.world.interactive.WorldEnemy;

/** Overworld encounter: touching it starts a fight against a single Huge Slime (which splits). */
public class BasicHugeSlime extends WorldEnemy {

    public BasicHugeSlime(float x, float y) {
        super("hugeSlime", 0.3f, x, y, 2f, 1.6f);
        fight.id("hugeSlime");
        fight.add(new HugeSlime());
        fight.musicName = "bachToccataAndFugue";
        fight.rewards.add(new PoisonOrb(), new ShieldBashOrb(), new GuardOrb());
    }
}
