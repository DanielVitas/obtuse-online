package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

/**
 * A heal-over-time (the mirror of {@link PoisonEvent}): each turn it deals negative damage to the
 * taker. The Health Potion already bakes in the effect of the equipment to its LEFT (see
 * HealthPotion.modifiedDamage), so this routes the Damage WITHOUT the trigger pass — that way no
 * runtime modifier (an outgoing one via the dealer, or an incoming one like Shield via the taker)
 * touches it again, and the heal matches the tooltip exactly regardless of equip order.
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
        addSubEventNoTrigger(new Damage(damage, dealer, taker));
        return 0;
    }
}
