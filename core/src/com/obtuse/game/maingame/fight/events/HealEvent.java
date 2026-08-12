package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

/**
 * A heal-over-time (the mirror of {@link PoisonEvent}): each turn it deals negative damage to the
 * taker. Because it routes through a {@link Damage} whose dealer is the caster, it is modified by
 * the caster's equipment exactly like poison — a Divine Orb turns it back into damage, etc.
 */
public class HealEvent extends Event {
    public int damage;
    public int duration;
    public FightObject dealer;
    public FightObject taker;

    public HealEvent(int damage, int duration, FightObject dealer, FightObject taker) {
        super(0, duration);
        this.damage = damage;
        this.duration = duration;
        this.dealer = dealer;
        this.taker = taker;
    }

    @Override
    public float run() {
        addSubEvent(new Damage(damage, dealer, taker));
        return 0;
    }
}
