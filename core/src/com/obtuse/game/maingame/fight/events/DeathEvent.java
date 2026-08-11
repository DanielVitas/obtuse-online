package com.obtuse.game.maingame.fight.events;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.ratio;
import static com.obtuse.game.Obtuse.w;

public class DeathEvent extends Event {
    public FightObject dealer;
    public FightObject target;

    public DeathEvent(FightObject dealer, FightObject target) {
        super(0,1);
        this.dealer = dealer;
        this.target = target;
    }

    @Override
    public float run() {
        return 0;
    }
}
