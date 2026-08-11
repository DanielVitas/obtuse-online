package com.obtuse.game.gameobjects.fight.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.all.Summon;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.AbilityOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;

public class Reaper extends Enemy {

    public Reaper() {
        super("reaper",1f, 0.1f,0.1f,0.5f,0.1f,1f,
                1f, 0.1f,0.1f,0.1f, "fight/objects/skeleton/hurt",
                "fight/objects/skeleton/death", "fight/objects/healing");
        setName("Reaper");
        setSize(2f, 2f);

        originalHP = 10;
        originalSpeed = 0;
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            AbilityOrb summonRedSkullOrb = new AbilityOrb("fireBallOrb", 1f, new Summon("Red Skull", 3) {
                @Override
                public FightObject summonedCreature() {
                    RedSkull redSkull = new RedSkull();
                    redSkull.originalHP = 4 - finalI;
                    return redSkull;
                }
            }) {
            };
            abilityOrbs.add(summonRedSkullOrb);
        }
    }

    @Override
    public float getClickHeight() {
        return super.getClickHeight() / 2;
    }
}
