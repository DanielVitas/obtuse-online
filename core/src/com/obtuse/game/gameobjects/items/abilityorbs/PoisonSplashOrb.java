package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.all.PoisonSplash;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class PoisonSplashOrb extends AbilityOrb {

    public PoisonSplashOrb() {
        super("fireBallOrb", 1f, new PoisonSplash());
    }
}
