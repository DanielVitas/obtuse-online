package com.obtuse.game.abilities.all;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.events.BuffEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Echo extends Ability {

    public Echo() {
        super("echo",2);
        setName("Echo");
        addTarget(HERO);
        addTarget(ENEMY);
        addTarget(SUMMON);
        addSuggestedTarget(HERO);
        addSuggestedTarget(SUMMON);

        description = "The next ability the target uses happens twice.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new BuffEvent(caster, target.fightObject, new EchoStatus(), level));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/echo/echo");
        super.animate(caster, target, level);
    }
}
