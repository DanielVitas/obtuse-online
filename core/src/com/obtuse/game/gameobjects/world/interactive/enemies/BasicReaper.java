package com.obtuse.game.gameobjects.world.interactive.enemies;

import com.obtuse.game.gameobjects.fight.enemies.Reaper;
import com.obtuse.game.gameobjects.fight.enemies.Skeleton;
import com.obtuse.game.gameobjects.items.abilityorbs.DuelOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.GuardOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.SpeedSwitchOrb;
import com.obtuse.game.gameobjects.items.equipment.BloodChalice;
import com.obtuse.game.gameobjects.world.interactive.WorldEnemy;

public class BasicReaper extends WorldEnemy {

    public BasicReaper(float x, float y) {
        super("reaper", 1f, x, y, 2f, 2f);
        fight.id("reaper");
        fight.add(new Reaper());
        fight.musicName = "dvorakSymphonyNo9";
        fight.rewards.add(new BloodChalice());
    }
}
