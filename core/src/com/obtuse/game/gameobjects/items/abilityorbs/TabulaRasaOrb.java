package com.obtuse.game.gameobjects.items.abilityorbs;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.all.TabulaRasa;
import com.obtuse.game.gameobjects.items.AbilityOrb;

public class TabulaRasaOrb extends AbilityOrb {

    public TabulaRasaOrb() {
        super("fireBallOrb", 1f, new TabulaRasa());
    }
}
