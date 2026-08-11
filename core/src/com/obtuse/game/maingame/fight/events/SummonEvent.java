package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Summon;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class SummonEvent extends Event {
    public FightObject caster;
    public FightObject summon;
    public Holder summonHolder;
    public FightLevel level;

    public SummonEvent(FightObject caster, FightObject summon, Holder summonSlot, FightLevel level) {
        super(0,1);
        this.caster = caster;
        this.summon = summon;
        this.summonHolder = summonSlot;
        this.level = level;
    }

    @Override
    protected float run() {
        if (summonHolder.fightObject != null)
            summonHolder.arena.remove(summonHolder.fightObject);
        summon.reset();
        summon.setupEquipment();
        summonHolder.arena.add(summon, summonHolder);
        summonHolder.createAndAdd(level.stage(1));
        level.addHolder(summonHolder);
        if (summon instanceof Hero)
            level.game.allHeroes.add((Hero) summon);
        if (summon instanceof Enemy)
            level.game.allEnemies.add((Enemy) summon);
        return summon.summon();
    }
}
