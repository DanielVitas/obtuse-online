package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.Thunderstrike;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class ThunderstrikeOrb extends AbilityOrb {

    public ThunderstrikeOrb() {
        super("fireBallOrb", 1f, new Thunderstrike());
    }
}
