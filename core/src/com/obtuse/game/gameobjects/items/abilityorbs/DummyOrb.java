package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.all.Dummy;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class DummyOrb extends AbilityOrb {

    public DummyOrb() {
        super("fireBallOrb", 1f, new Dummy());
    }
}
