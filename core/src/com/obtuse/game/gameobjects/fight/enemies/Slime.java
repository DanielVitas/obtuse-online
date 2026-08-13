package com.obtuse.game.gameobjects.fight.enemies;

import com.obtuse.game.abilities.all.FireBall;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;

/** The smallest slime: 2 HP, one move (Fire Ball). What Big Slimes split into. Does not split. */
public class Slime extends Enemy {

    public Slime() {
        super("slime", 0.2f, 0.12f, 0.12f, 0.1f, 0.1f, 1f, 0.3f, 0.5f, 0.5f, 0.1f,
                "fight/objects/skeleton/hurt", "fight/objects/skeleton/death", "fight/objects/healing");
        setName("Slime");
        setSize(0.85f, 0.85f);
        originalHP = 2;

        FireBallOrb fireBall = new FireBallOrb();
        ((FireBall) fireBall.ability).damage = 1;
        abilityOrbs.add(fireBall);
    }
}
