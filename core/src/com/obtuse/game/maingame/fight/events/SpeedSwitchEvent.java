package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

public class SpeedSwitchEvent extends Event {
    public FightObject caster;
    public FightObject target;

    public SpeedSwitchEvent(FightObject caster, FightObject target) {
        super(0,1);
        this.caster = caster;
        this.target = target;
    }

    @Override
    public float run() {
        if (caster.holder.arena == target.holder.arena) {
            addSubEvent(new SpeedSetEvent(caster.speed, caster, target));
            addSubEvent(new SpeedSetEvent(target.speed, caster, caster));
            int ci = caster.holder.arena.fighterOrder.indexOf(caster,true);
            int ti = caster.holder.arena.fighterOrder.indexOf(target,true);
            caster.holder.arena.fighterOrder.insert(ci, target);
            caster.holder.arena.fighterOrder.removeIndex(ci + 1);
            caster.holder.arena.fighterOrder.insert(ti, caster);
            caster.holder.arena.fighterOrder.removeIndex(ti + 1);
            caster.holder.arena.refreshProfiles();
        }
        return 0;
    }
}
