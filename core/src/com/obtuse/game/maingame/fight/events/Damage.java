package com.obtuse.game.maingame.fight.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.ratio;
import static com.obtuse.game.Obtuse.w;

public class Damage extends Event {
    public int damage;
    public FightObject dealer;
    public FightObject taker;

    public Damage(int damage, FightObject dealer, FightObject taker) {
        super(0,1);
        this.damage = damage;
        this.dealer = dealer;
        this.taker = taker;
    }

    private void damageText() {
        int dmg = damage;
        if (taker.holder.damageLabel.getY() - taker.holder.getY() / 10 * ratio * h(1) - h(0.1f) < h(0.02f))
            // Label.getText() returns a CharArray now; length is a method on it,
            // and CharArray.substring(int) still works, so the line below is unchanged.
            if (taker.holder.damageLabel.getText().length() > 1)
                dmg += Integer.parseInt(taker.holder.damageLabel.getText().substring(1));
        if (dmg >= 0) {
            taker.holder.damageLabel.setText("-" + Integer.toString(dmg));
            taker.holder.damageLabel.setColor(Color.RED);
        } else {
            taker.holder.damageLabel.setText("+" + Integer.toString(-dmg));
            taker.holder.damageLabel.setColor(Color.GREEN);
        }
        taker.holder.damageLabel.clearActions();
        taker.holder.damageLabel.setPosition(taker.holder.getX() / 10 * w(1) + w(0.05f),
                taker.holder.getY() / 10 * ratio * h(1) + h(0.1f));
        taker.holder.damageLabel.setSize(w(0.2f), h(0.01f));
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.show());
        sequenceAction.addAction(Actions.moveBy(0, h(0.05f), 0.4f));
        sequenceAction.addAction(Actions.hide());
        taker.holder.damageLabel.addAction(sequenceAction);
    }

    @Override
    public float run() {
        addSubEvent(new DamageDealt(damage, dealer, taker));
        if (taker.hp > taker.damageTaken) {
            damageText();
            if (taker.hp - taker.damageTaken <= damage)
                addSubEvent(new DeathEvent(dealer, taker));
        }
        return taker.damage(damage);
    }
}
