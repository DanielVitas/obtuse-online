package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.Duel;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class DuelOrb extends AbilityOrb {

    public DuelOrb() {
        super("fireBallOrb", 1f, new Duel());
    }
}
