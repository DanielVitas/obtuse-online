package com.obtuse.game.gameobjects.world.interactive.enemies;

import com.obtuse.game.gameobjects.fight.enemies.RedSkull;
import com.obtuse.game.gameobjects.fight.enemies.Skeleton;
import com.obtuse.game.gameobjects.items.abilityorbs.StealOrb;
import com.obtuse.game.gameobjects.items.equipment.Shield;
import com.obtuse.game.gameobjects.world.interactive.WorldEnemy;

public class BasicRedSkull extends WorldEnemy {

    public BasicRedSkull(float x, float y) {
        super("redSkull", 0.5f, x, y, 1f, 1f);
        fight.id("redSkull");
        fight.add(new Skeleton());
        fight.add(new RedSkull());
        fight.add(new Skeleton());
        fight.musicName = "marschnerDerVampyr";
        fight.rewards.add(new StealOrb(), new Shield());
    }
}
