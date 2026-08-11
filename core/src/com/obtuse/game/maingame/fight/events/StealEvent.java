package com.obtuse.game.maingame.fight.events;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.ratio;
import static com.obtuse.game.Obtuse.w;

public class StealEvent extends Event {
    public FightObject caster;
    public FightObject target;

    public StealEvent(FightObject caster, FightObject target) {
        super(0,1);
        this.caster = caster;
        this.target = target;
    }

    @Override
    public float run() {
        if (target.lastUsed != null) {
            target.abilities.removeValue(target.lastUsed, true);
            int index = caster.abilities.indexOf(caster.lastUsed, true);
            caster.abilities.removeIndex(index);
            caster.abilities.insert(index, target.lastUsed);
            target.lastUsed = null;
            caster.lastUsed = null;
        }
        return 0;
    }
}
