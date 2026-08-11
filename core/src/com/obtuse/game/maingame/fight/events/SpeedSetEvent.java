package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

public class SpeedSetEvent extends Event {
    int newSpeed;
    public FightObject caster;
    public FightObject target;

    public SpeedSetEvent(int newSpeed, FightObject caster, FightObject target) {
        super(0,1);
        this.newSpeed = newSpeed;
        this.caster = caster;
        this.target = target;
    }

    @Override
    public float run() {
        target.speed = newSpeed;
        return 0;
    }
}
