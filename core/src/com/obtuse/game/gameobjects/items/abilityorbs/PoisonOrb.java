package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.Poison;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class PoisonOrb extends AbilityOrb {

    public PoisonOrb() {
        super("fireBallOrb", 1f, new Poison());
    }
}
