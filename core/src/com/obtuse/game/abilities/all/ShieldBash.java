package com.obtuse.game.abilities.all;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.buffs.Stunned;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.events.BuffEvent;
import com.obtuse.game.maingame.fight.events.StunEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;
import com.obtuse.game.maingame.fight.triggers.GuardTrigger;

public class ShieldBash extends Ability {

    public ShieldBash() {
        super("shieldBash",3);
        setName("Shield Bash");
        addTarget(HERO);
        addTarget(ENEMY);
        addTarget(SUMMON);
        addSuggestedTarget(ENEMY);

        description = "Stuns the target.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/shieldBash/bash");
        caster.postturn.add(new StunEvent(caster, target.fightObject, level));
    }
}
