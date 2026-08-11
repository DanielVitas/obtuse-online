package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

public class DamageDealt extends Event {
    public int damage;
    public FightObject dealer;
    public FightObject target;

    public DamageDealt(int damage, FightObject dealer, FightObject target) {
        super(0,1);
        this.damage = damage;
        this.dealer = dealer;
        this.target = target;
    }

    @Override
    public float run() {
        return 0;
    }
}
