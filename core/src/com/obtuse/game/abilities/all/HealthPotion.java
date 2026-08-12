package com.obtuse.game.abilities.all;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.events.HealEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

/**
 * The mirror of Poison: applies a heal-over-time (negative damage) to the target. Since the heal
 * runs through a Damage event dealt BY the caster, the caster's equipment modifies it — a Divine
 * Orb turns the heal into damage, damaging gloves cancel it, a healing staff heals for more.
 */
public class HealthPotion extends Ability {
    public int damage = -1;      // negative damage = healing
    public int duration = 5;

    public HealthPotion() {
        super("healthPotion", 3);
        setName("Health Potion");
        addTarget(HERO);
        addTarget(SUMMON);
        addSuggestedTarget(HERO);
        description = "Heal 1 point of life at the start of each turn.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        target.fightObject.preturn.add(new HealEvent(damage, duration, caster, target.fightObject));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/objects/healing");
        super.animate(caster, target, level);
    }
}
