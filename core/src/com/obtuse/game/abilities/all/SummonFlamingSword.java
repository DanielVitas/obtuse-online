package com.obtuse.game.abilities.all;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.ability.AbilityHolder;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.SummonEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class SummonFlamingSword extends Ability {

    public SummonFlamingSword() {
        super("summonFlamingSword", 2);
        setName("Summon Flaming Sword");

        description = "Summons Flaming Sword.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        if (caster.holder.summonHolder != null)
            caster.postturn.add(new SummonEvent(caster, new com.obtuse.game.gameobjects.fight.summons.FlamingSword(),
                    caster.holder.summonHolder, level));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/summonCast");
        super.animate(caster, target, level);
    }
}
