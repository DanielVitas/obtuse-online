package com.obtuse.game.maingame.fight.events;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

public class ReverseEvent extends Event {
    public FightObject caster;

    public ReverseEvent(FightObject caster) {
        super(0,1);
        this.caster = caster;
    }

    @Override
    public float run() {
        Array<Integer> speeds = new Array<Integer>();
        for (FightObject fightObject : caster.holder.arena.fighterOrder)
            speeds.add(fightObject.speed);
        speeds.reverse();
        for (int i = 0; i < caster.holder.arena.fighterOrder.size; i++)
            addSubEvent(new SpeedSetEvent(speeds.get(i), caster, caster.holder.arena.fighterOrder.get(i)));
        caster.holder.arena.fighterOrder.reverse();
        caster.holder.arena.refreshProfiles();
        return 0;
    }
}
