package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.ShieldBash;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class ShieldBashOrb extends AbilityOrb {

    public ShieldBashOrb() {
        super("fireBallOrb", 1f, new ShieldBash());
    }
}
