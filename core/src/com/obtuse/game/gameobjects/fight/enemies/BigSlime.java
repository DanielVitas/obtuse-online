package com.obtuse.game.gameobjects.fight.enemies;

import com.obtuse.game.abilities.all.FireBall;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ShieldBashOrb;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.triggers.SlimeSplitTrigger;

/** 4 HP, Fire Ball (2 damage) + Shield Bash. Splits into two small Slimes on death. */
public class BigSlime extends Enemy {

    public BigSlime() {
        super("bigSlime", 0.2f, 0.12f, 0.12f, 0.1f, 0.1f, 1f, 0.3f, 0.5f, 0.5f, 0.1f,
                "fight/objects/skeleton/hurt", "fight/objects/skeleton/death", "fight/objects/healing");
        setName("Big Slime");
        setSize(1.2f, 1.2f);
        originalHP = 4;

        FireBallOrb fireBall = new FireBallOrb();
        ((FireBall) fireBall.ability).damage = 2;
        abilityOrbs.add(fireBall, new ShieldBashOrb());
    }

    @Override
    public void setupEquipment() {
        super.setupEquipment();
        Arena.triggers.add(new SlimeSplitTrigger(this, 2) {
            @Override
            protected com.obtuse.game.gameobjects.fight.Enemy child() {
                return new Slime();
            }
        });
    }
}
