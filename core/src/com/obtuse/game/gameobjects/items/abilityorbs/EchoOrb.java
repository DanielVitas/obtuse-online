package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.Echo;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class EchoOrb extends AbilityOrb {

    public EchoOrb() {
        super("fireBallOrb", 1f, new Echo());
    }
}
