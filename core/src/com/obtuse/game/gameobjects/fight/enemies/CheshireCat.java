package com.obtuse.game.gameobjects.fight.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.all.FireBall;
import com.obtuse.game.abilities.all.Summon;
import com.obtuse.game.abilities.all.Thunderstrike;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.AbilityOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ThunderstrikeOrb;

public class CheshireCat extends Enemy {

    public CheshireCat() {
        super("cheshireCat",0.2f, 0.2f,0.2f,0.2f,0.2f,1f,
                1f, 0.2f,0.2f,0.2f, "fight/objects/skeleton/hurt",
                "fight/objects/skeleton/death", "fight/objects/healing");
        setName("Smiling Demon");
        setSize(2f, 2f);

        originalHP = 4;
        originalSpeed = 2;
        AbilityOrb summonOrb = new AbilityOrb("fireBallOrb", 1f, new Summon(getName(),2) {
            @Override
            public FightObject summonedCreature() {
                return new CheshireCat();
            }
        }) {};
        FireBallOrb fb = new FireBallOrb();
        abilityOrbs.add(summonOrb, fb);
    }
}
