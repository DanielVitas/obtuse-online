package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.all.HealthPotion;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class HealthPotionOrb extends AbilityOrb {

    public HealthPotionOrb() {
        super("healthPotion", 1f, new HealthPotion());
    }
}
