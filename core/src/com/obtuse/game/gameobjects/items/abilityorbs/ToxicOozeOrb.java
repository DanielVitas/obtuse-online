package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.all.ToxicOoze;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class ToxicOozeOrb extends AbilityOrb {

    public ToxicOozeOrb() {
        super("fireBallOrb", 1f, new ToxicOoze());
    }
}
