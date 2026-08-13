package com.obtuse.game.maingame.fight.events;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class BloodSacrificeEvent extends Event {
    public int damage;
    public int stunTreshold;
    public FightObject caster;
    public Holder target;
    public FightLevel level;

    public BloodSacrificeEvent(int damage, FightObject caster, FightLevel level) {
        super(0,1);
        this.damage = damage;
        this.caster = caster;
        this.level = level;
        for (Array<Holder> holderArray : new Array[]{caster.holder.arena.heroHolders, caster.holder.arena.enemyHolders,
                caster.holder.arena.summonHolders})
            for (Holder holder : holderArray)
                    if (holder.fightObject != null)
                        if (holder.fightObject != caster)
                            addSubEventNoTrigger(new Damage(damage, caster, holder.fightObject).cause("was sacrificed."));
    }

    @Override
    public float run() {
        int totalDamageDealt = 0;
        for (Event event : subEvents)
            if (event instanceof Damage)
                if (((Damage) event).taker.alive())
                    totalDamageDealt += ((Damage) event).damage;
        caster.preturn.add(new Damage(totalDamageDealt, caster, caster).cause("was sacrificed."));
        return 0;
    }
}
