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
        // The Huge Slime should drop the Ooze on its OWN side, never on a hero/summon slot. Enemies
        // target with inverse=true, so the ally-side slot (EMPTYHEROSLOT) resolves to empty ENEMY
        // slots for the caster (same way Skeleton's suggested ENEMY inverts to attack heroes). Clear
        // the base Summon's broader empty-hero/summon targets and keep only the own-side slot.
        targets.clear();
        suggestedTargets.clear();
        addTarget(EMPTYHEROSLOT);
        addSuggestedTarget(EMPTYHEROSLOT);
        description = "Summons an Ooze onto an empty enemy slot. Each turn the Ooze poisons every unit beside it.";
    }

    @Override
    public FightObject summonedCreature() {
        return new Ooze();
    }
}
