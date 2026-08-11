package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.Swap;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class SwapOrb extends AbilityOrb {

    public SwapOrb() {
        super("fireBallOrb", 1f, new Swap());
    }
}
