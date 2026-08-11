package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.SpeedSwitch;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class SpeedSwitchOrb extends AbilityOrb {

    public SpeedSwitchOrb() {
        super("fireBallOrb", 1f, new SpeedSwitch());
    }
}
