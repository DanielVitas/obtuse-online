package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.all.FireBall;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class FireBallOrb extends AbilityOrb {

    public FireBallOrb() {
        super("fireBallOrb", 1f, new FireBall());
    }
}
