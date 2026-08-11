package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.StealEvent;
import com.obtuse.game.maingame.fight.events.SummonEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public abstract class Summon extends Ability {

    public Summon(String name, int pp) {
        super("summon", pp);
        setName("Summon " + name);
        addTarget(EMPTYHEROSLOT);
        addTarget(EMPTYSUMMONSLOT);
        addSuggestedTarget(EMPTYHEROSLOT);

        description = "Summons " + name + " to targeted empty slot.";
    }

    public abstract FightObject summonedCreature();

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new SummonEvent(caster, summonedCreature(), target, level));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        super.animate(caster, target, level);
    }
}
