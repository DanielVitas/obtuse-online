package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.Status;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class SwapEvent extends Event {
    public FightObject caster;
    public Holder target;
    public FightLevel level;

    public SwapEvent(FightObject caster, Holder target, FightLevel level) {
        super(0,1);
        this.caster = caster;
        this.target = target;
        this.level = level;
    }

    @Override
    protected float run() {
        Holder casterHolder = caster.holder;
        casterHolder.removeFightWithoutProfileObject();
        if (target.fightObject != null) {
            casterHolder.setFightObject(target.fightObject);
            target.removeFightWithoutProfileObject();
            casterHolder.createAndAdd(level.stage(1));
        }
        target.setFightObject(caster);
        target.createAndAdd(level.stage(1));
        return 0;
    }
}
