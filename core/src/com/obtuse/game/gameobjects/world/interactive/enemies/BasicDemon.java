package com.obtuse.game.gameobjects.world.interactive.enemies;

import com.obtuse.game.gameobjects.fight.enemies.Demon;
import com.obtuse.game.gameobjects.fight.enemies.Skeleton;
import com.obtuse.game.gameobjects.items.abilityorbs.BloodSacrificeOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.EchoOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ReverseOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ThunderstrikeOrb;
import com.obtuse.game.gameobjects.world.interactive.WorldEnemy;
import com.obtuse.game.maingame.fight.arenas.BossArena;

public class BasicDemon extends WorldEnemy {

    public BasicDemon(float x, float y) {
        super("demon", 0.1f, x, y, 2f, 2f);
        fight.id("demon");
        fight.arenaClass = BossArena.class;
        fight.add(new Demon());
        fight.musicName = "orffOFortuna";
        fight.rewards.add(new BloodSacrificeOrb());
    }
}
