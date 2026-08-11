package com.obtuse.game.abilities.all;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.events.BuffEvent;
import com.obtuse.game.maingame.fight.events.SwapEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Swap extends Ability {

    public Swap() {
        super("swap",3);
        setName("Swap");
        addTarget(HEROSLOT);
        addSuggestedTarget(HEROSLOT);

        description = "Teleports caster to the targeted hero slot and any character standing there to the casters.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/wooshCast");
        caster.postturn.add(new SwapEvent(caster, target, level));
    }
}
