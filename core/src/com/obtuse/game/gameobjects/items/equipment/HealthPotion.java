package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.events.HealEvent;

/**
 * Passive equipment: the wearer regenerates 1 life at the start of each turn. Only damage modifiers
 * equipped to its LEFT (earlier in the list) affect the heal — a modifier to its right does nothing.
 * So [Divine Orb, Health Potion] makes it "heal -1" (the orb, to the left, reverses it), while
 * [Health Potion, Divine Orb] still heals 1 (the orb, to the right, is ignored).
 */
public class HealthPotion extends Equipment {
    public int heal = 1;

    public HealthPotion() {
        super("healthPotion", 1f);
        setName("Health Potion");
        description = "Heals 1 life at the start of each turn.";
    }

    /** The heal as a (negative) damage value, with ONLY the equipment to its left applied. */
    private int modifiedDamage(FightObject wearer) {
        int idx = wearer.equipment.indexOf(this, true);
        if (idx < 0)
            idx = wearer.equipment.size;   // not equipped (grid): treat as appended — all current gear is to its left
        int d = -heal;                     // base: -1 damage == heal 1
        for (int i = 0; i < idx; i++)
            d = wearer.equipment.get(i).previewForHealthPotion(d);
        return d;
    }

    @Override
    public void setup(FightObject fightObject) {
        // Bake in the left-equipment modifiers now, and make the HealEvent dealer-less so the runtime
        // triggers (equipment to its RIGHT) never touch it again.
        fightObject.preturn.add(new HealEvent(modifiedDamage(fightObject), 999, null, fightObject));
    }

    /** Description with the heal recomputed for the wearer's left-side equipment (coloured markup). */
    public String describedFor(FightObject wearer) {
        int amount = -modifiedDamage(wearer);   // heal amount = -damage
        String num;
        if (amount > heal)
            num = "[#7ddc7d]" + amount + "[]";  // more healing = green
        else if (amount < heal)
            num = "[#ff6b6b]" + amount + "[]";  // less / negative = red
        else
            num = Integer.toString(amount);
        return "Heals " + num + " life at the start of each turn.";
    }
}
