package com.obtuse.game.abilities.all;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.summons.Ooze;

/**
 * The Huge Slime's summon move. Places a neutral Ooze on any empty slot; the Ooze then poisons
 * whatever stands next to it each turn. Extends the generic {@link Summon} ability.
 */
public class ToxicOoze extends Summon {

    public ToxicOoze() {
        super("Ooze", 2);
        setName("Toxic Ooze");
        // The base already targets EMPTYHEROSLOT + EMPTYSUMMONSLOT (suggesting EMPTYHEROSLOT so the
        // ooze lands next to the heroes). Allow the enemy side too, so "any empty slot" holds.
        addTarget(EMPTYENEMYSLOT);
        addSuggestedTarget(EMPTYSUMMONSLOT);
        description = "Summons an Ooze onto an empty slot. Each turn the Ooze poisons every unit beside it.";
    }

    @Override
    public FightObject summonedCreature() {
        return new Ooze();
    }
}
