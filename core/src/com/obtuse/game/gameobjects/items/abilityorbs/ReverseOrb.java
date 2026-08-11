package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.Reverse;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class ReverseOrb extends AbilityOrb {

    public ReverseOrb() {
        super("fireBallOrb", 1f, new Reverse());
    }
}
