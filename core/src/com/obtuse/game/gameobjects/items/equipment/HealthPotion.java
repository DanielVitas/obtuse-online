package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.events.HealEvent;

/**
 * Passive equipment: the wearer regenerates 1 life at the start of each turn. The heal is dealt to
 * the wearer BY the wearer, so their own equipment modifies it (a Divine Orb turns it into damage,
 * damaging gloves cancel it, a healing staff heals for more) — the mirror of a self-poison.
 */
public class HealthPotion extends Equipment {
    public int heal = 1;

    public HealthPotion() {
        super("healthPotion", 1f);
        setName("Health Potion");
        description = "Heals 1 life at the start of each turn.";
    }

    @Override
    public void setup(FightObject fightObject) {
        // Negative damage = healing; a long duration so it lasts the whole fight.
        fightObject.preturn.add(new HealEvent(-heal, 999, fightObject, fightObject));
    }
}
