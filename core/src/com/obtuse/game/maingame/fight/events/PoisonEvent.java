package com.obtuse.game.maingame.fight.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.ratio;
import static com.obtuse.game.Obtuse.w;

public class PoisonEvent extends Event {
    public int damage;
    public int duration;
    public FightObject dealer;
    public FightObject taker;

    public PoisonEvent(int damage, int duration, FightObject dealer, FightObject taker) {
        super(0,duration);
        this.damage = damage;
        this.duration = duration;
        this.dealer = dealer;
        this.taker = taker;
    }

    @Override
    public float run() {
        addSubEvent(new Damage(damage, dealer, taker).cause("succumbed to poison."));
        return 0;
    }
}
