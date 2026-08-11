package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class ThunderstrikeEvent extends Event {
    public int damage;
    public int stunTreshold;
    public FightObject caster;
    public Holder target;
    public FightLevel level;

    public ThunderstrikeEvent(int damage, int stunTreshold, FightObject caster, Holder target, FightLevel level) {
        super(0,1);
        this.damage = damage;
        this.stunTreshold = stunTreshold;
        this.caster = caster;
        this.target = target;
        this.level = level;
        if (target.fightObject != null)
            addSubEventNoTrigger(new Damage(damage, caster, target.fightObject));
        for (Holder holder : target.adjacent)
            if (holder.fightObject != null)
                addSubEventNoTrigger(new Damage(damage, caster, holder.fightObject));
    }

    @Override
    public float run() {
        int totalDamageDealt = 0;
        for (Event event : subEvents)
            if (event instanceof Damage)
                if (((Damage) event).taker.alive())
                    totalDamageDealt += ((Damage) event).damage;
        if (totalDamageDealt <= stunTreshold) {
            if (target.fightObject != null)
                addSubEvent(new StunEvent(caster, target.fightObject, level));
            for (Holder holder : target.adjacent)
                if (holder.fightObject != null)
                    addSubEvent(new StunEvent(caster, holder.fightObject, level));
        }
        return 0;
    }
}
