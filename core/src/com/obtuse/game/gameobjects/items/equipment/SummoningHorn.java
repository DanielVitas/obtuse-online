package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.SummonEvent;

public class SummoningHorn extends Equipment {

    public SummoningHorn() {
        super("summoningHorn", 1f);
        setName("Summoning Horn");
        description = "Summons have double HP.";
    }

    @Override
    public void setup(final FightObject fightObject) {
        Arena.triggers.insert(0, new Trigger() {
            @Override
            public boolean check(Event event) {
                if (event instanceof SummonEvent)
                    if (((SummonEvent) event).caster == fightObject) {
                        ((SummonEvent) event).summon.originalHP *= 2;
                        //return true;
                    }
                return false;
            }

            @Override
            public void run(Event event) {

            }
        });
    }
}
