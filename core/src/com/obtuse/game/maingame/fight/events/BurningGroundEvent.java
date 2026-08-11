package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class BurningGroundEvent extends Event {
    public int damage;
    public FightObject caster;
    public Holder target;

    public BurningGroundEvent(int damage, FightObject caster, Holder target) {
        super(0,1);
        this.damage = damage;
        this.caster = caster;
        this.target = target;
    }

    @Override
    protected float run() {
        target.burning += damage;
        return target.burn();
    }
}
