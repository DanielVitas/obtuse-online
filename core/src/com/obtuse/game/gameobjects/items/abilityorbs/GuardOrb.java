package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.Guard;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class GuardOrb extends AbilityOrb {

    public GuardOrb() {
        super("fireBallOrb", 1f, new Guard());
    }
}
