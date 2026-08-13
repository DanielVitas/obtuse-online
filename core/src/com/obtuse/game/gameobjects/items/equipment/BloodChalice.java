package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.DamageDealt;

public class BloodChalice extends Equipment {

    public BloodChalice() {
        super("bloodChalice", 1f);
        setName("Blood Chalice");
        description = "Heals for the damage dealt.";
    }

    @Override
    public void setup(final FightObject fightObject) {
        Arena.triggers.insert(0, new Trigger() {
            @Override
            public boolean check(Event event) {
                if (event instanceof DamageDealt)
                    if (((DamageDealt) event).dealer == fightObject)
                        event.addSubEvent(new Damage(-((DamageDealt) event).damage,null, fightObject).cause("was drained."));
                return false;
            }

            @Override
            public void run(Event event) {

            }
        });
    }
}
