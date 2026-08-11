package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.BurningGround;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class BurningGroundOrb extends AbilityOrb {

    public BurningGroundOrb() {
        super("fireBallOrb", 1f, new BurningGround());
    }
}
