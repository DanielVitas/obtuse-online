package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.Status;
import com.obtuse.game.gameobjects.fight.buffs.Stunned;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class StunEvent extends Event {
    public FightObject caster;
    public FightObject target;
    public FightLevel level;

    public StunEvent(FightObject caster, FightObject target, FightLevel level) {
        super(0,1);
        this.caster = caster;
        this.target = target;
        this.level = level;
    }

    @Override
    protected float run() {
        BuffEvent event = new BuffEvent(caster, target, new Stunned(), level);
        event.run();
        return target.stun();
    }
}
