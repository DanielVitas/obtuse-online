package com.obtuse.game.maingame.fight.events;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Event;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.ratio;
import static com.obtuse.game.Obtuse.w;

public class DamageSlot extends Event {
    public int damage;
    public FightObject dealer;
    public Holder taker;

    public DamageSlot(int damage, FightObject dealer, Holder taker) {
        super(0,1);
        this.damage = damage;
        this.dealer = dealer;
        this.taker = taker;
    }

    @Override
    public float run() {
        if (taker.fightObject != null) {
            Damage damageEvent = new Damage(damage, dealer, taker.fightObject);
            addSubEvent(damageEvent);
        }
        return 0;
    }
}
