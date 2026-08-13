package com.obtuse.game.maingame.fight.triggers;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.DeathEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class ExplosionDeathTrigger extends Trigger {
    public FightObject caster;
    public int damage;
    public FightLevel level;

    public ExplosionDeathTrigger(FightObject caster, int damage, FightLevel level) {
        this.caster = caster;
        this.damage = damage;
        this.level = level;
    }

    @Override
    public boolean check(Event event) {
        if (event.getClass() == DeathEvent.class)
            return ((DeathEvent) event).target == caster;
        return false;
    }

    @Override
    public void run(Event event) {
        for (Holder holder : caster.holder.adjacent)
            if (holder.fightObject != null)
                if (holder.fightObject.alive()) {
                    Damage damage = new Damage(this.damage, caster, holder.fightObject).cause("was caught in the blast.");
                    event.addSubEvent(damage,0);
                }
        remove();
    }
}
