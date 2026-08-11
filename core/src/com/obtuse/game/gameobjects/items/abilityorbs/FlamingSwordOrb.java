package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.all.SummonFlamingSword;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class FlamingSwordOrb extends AbilityOrb {

    public FlamingSwordOrb() {
        super("fireBallOrb", 1f, new SummonFlamingSword());
    }
}
