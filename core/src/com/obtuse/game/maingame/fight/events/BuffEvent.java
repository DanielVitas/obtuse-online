package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.Status;
import com.obtuse.game.gameobjects.fight.buffs.Stunned;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class BuffEvent extends Event {
    public FightObject caster;
    public FightObject target;
    public Status status;
    public FightLevel level;

    public BuffEvent(FightObject caster, FightObject target, Status status, FightLevel level) {
        super(0,1);
        this.caster = caster;
        this.target = target;
        this.status = status;
        this.level = level;
    }

    @Override
    protected float run() {
        if (status instanceof Stunned)
            block :{
                for (Status tempStatus : target.statuses)
                    if (tempStatus instanceof Stunned)
                        break block;
                target.statuses.add(status);
            }
        else
            target.statuses.add(status);
        return 0;
    }
}
