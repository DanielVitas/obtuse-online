package com.obtuse.game.abilities.all;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.levels.FightLevel;
import com.obtuse.game.maingame.fight.triggers.GuardTrigger;

public class Guard extends Ability {
    public int damageGuarded = 1;

    public Guard() {
        super("guard",3);
        setName("Guard");
        addTarget(HERO);
        addTarget(ENEMY);
        addTarget(SUMMON);
        addSuggestedTarget(HERO);
        addSuggestedTarget(SUMMON);

        description = "Reduces damage taken by target by " + Integer.toString(damageGuarded) + " and damages the caster " +
                "instead.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        GuardTrigger guardTrigger;
        for (Trigger trigger : caster.holder.arena.triggers)
            if (trigger.getClass() == GuardTrigger.class)
                if (((GuardTrigger) trigger).caster == caster)
                    if (((GuardTrigger) trigger).target == target.fightObject) {
                        guardTrigger = (GuardTrigger) trigger;
                        guardTrigger.damageGuarded += damageGuarded;
                        if (caster.getBuff(EchoStatus.class) != null)
                            guardTrigger.damageGuarded += damageGuarded;
                        return;
                    }

        guardTrigger = new GuardTrigger(caster, target.fightObject, damageGuarded, level);
        Arena.triggers.add(guardTrigger);
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/guard/guard");
        super.animate(caster, target, level);
    }
}
