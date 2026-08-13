package com.obtuse.game.gameobjects.fight.enemies;

import com.obtuse.game.abilities.all.FireBall;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.items.abilityorbs.BloodSacrificeOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ToxicOozeOrb;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.triggers.SlimeSplitTrigger;

/** 8 HP, Fire Ball (3 damage) + Blood Sacrifice + Toxic Ooze. Splits into two Big Slimes on death. */
public class HugeSlime extends Enemy {

    public HugeSlime() {
        super("hugeSlime", 0.25f, 0.14f, 0.12f, 0.12f, 0.14f, 1f, 0.3f, 0.5f, 0.5f, 0.1f,
                "fight/objects/skeleton/hurt", "fight/objects/skeleton/death", "fight/objects/healing");
        setName("Huge Slime");
        setSize(2.1f, 1.64f);   // 32x25 native frame, aspect preserved
        originalHP = 8;

        FireBallOrb fireBall = new FireBallOrb();
        ((FireBall) fireBall.ability).damage = 3;
        abilityOrbs.add(fireBall, new BloodSacrificeOrb(), new ToxicOozeOrb());
    }

    @Override
    public void setupEquipment() {
        super.setupEquipment();
        Arena.triggers.add(new SlimeSplitTrigger(this, 2) {
            @Override
            protected com.obtuse.game.gameobjects.fight.Enemy child() {
                return new BigSlime();
            }
        });
    }
}
