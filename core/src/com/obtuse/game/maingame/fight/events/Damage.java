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
    public String deathVerb;   // the cause-of-death phrase shown if this damage is fatal (null = generic)

    public Damage(int damage, FightObject dealer, FightObject taker) {
        super(0,1);
        this.damage = damage;
        this.dealer = dealer;
        this.taker = taker;
    }

    /** Tag what this damage IS, so a lethal blow can announce e.g. "[name] was incinerated!". */
    public Damage cause(String deathVerb) {
        this.deathVerb = deathVerb;
        return this;
    }

    private void damageText() {
        // Anchor the popup on the character by projecting its world spot through the live arena camera —
        // so it stays on the character during a duel, where that camera is zoomed and shifted.
        com.badlogic.gdx.math.Vector2 anchor = ((com.obtuse.game.maingame.fight.levels.FightLevel)
                taker.holder.arena.fightGame.level).worldToScreen(taker.holder.getX(), taker.holder.getY());
        float baseX = anchor.x + w(0.05f), baseY = anchor.y + h(0.1f);
        int dmg = damage;
        if (taker.holder.damageLabel.getY() - baseY < h(0.02f))
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
        taker.holder.damageLabel.setPosition(baseX, baseY);
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
                addSubEvent(new DeathEvent(dealer, taker, deathVerb));
        }
        return taker.damage(damage);
    }
}
