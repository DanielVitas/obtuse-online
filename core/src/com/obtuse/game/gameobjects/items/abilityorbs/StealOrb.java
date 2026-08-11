package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.Steal;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class StealOrb extends AbilityOrb {

    public StealOrb() {
        super("fireBallOrb", 1f, new Steal());
    }
}
