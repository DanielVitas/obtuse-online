package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.BloodSacrifice;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class BloodSacrificeOrb extends AbilityOrb {

    public BloodSacrificeOrb() {
        super("fireBallOrb", 1f, new BloodSacrifice());
    }
}
