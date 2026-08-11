package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.DelayedHit;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class DelayedHitOrb extends AbilityOrb {

    public DelayedHitOrb() {
        super("fireBallOrb", 1f, new DelayedHit());
    }
}
