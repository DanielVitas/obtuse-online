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
        // The Huge Slime should drop the Ooze on the PLAYER's side — an empty hero or summon slot —
        // so it poisons the heroes/their summons. Enemies target with inverse=true, so the
        // opponent-side slot (EMPTYENEMYSLOT) inverts to empty HERO + empty SUMMON slots for the
        // caster (same way Skeleton's suggested ENEMY inverts to attack heroes). Clear the base
        // Summon's own-side default and declare the opponent side.
        targets.clear();
        suggestedTargets.clear();
        addTarget(EMPTYENEMYSLOT);
        addSuggestedTarget(EMPTYENEMYSLOT);
        description = "Summons an Ooze onto an empty hero or summon slot. Each turn the Ooze poisons every unit beside it.";
    }

    @Override
    public FightObject summonedCreature() {
        return new Ooze();
    }
}
