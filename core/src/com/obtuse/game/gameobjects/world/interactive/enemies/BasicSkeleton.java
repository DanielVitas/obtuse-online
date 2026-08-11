package com.obtuse.game.gameobjects.world.interactive.enemies;

import com.obtuse.game.gameobjects.fight.enemies.Demon;
import com.obtuse.game.gameobjects.fight.enemies.Skeleton;
import com.obtuse.game.gameobjects.items.abilityorbs.*;
import com.obtuse.game.gameobjects.world.interactive.WorldEnemy;

public class BasicSkeleton extends WorldEnemy {

    public BasicSkeleton(float x, float y) {
        super("skeleton", 0.5f, x, y, 1f, 1f);
        fight.id("skeletons");
        fight.add(new Skeleton());
        fight.add(new Skeleton());
        fight.musicName = "bachToccataAndFugue";
        fight.rewards.add(new DuelOrb(), new SpeedSwitchOrb(), new GuardOrb());
    }
}
