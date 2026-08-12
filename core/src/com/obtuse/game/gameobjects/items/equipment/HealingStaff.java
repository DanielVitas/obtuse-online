package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.events.Damage;

public class HealingStaff extends Equipment {
    public int damage = 2;

    public HealingStaff() {
        super("healingStaff",1f);
        setName("Healing Staff");
        description = "Deals " + Integer.toString(damage) + " less damage with all damaging abilities.";
    }

    @Override
    public int previewOutgoingDamage(int d) {
        return d - damage;
    }

    @Override
    public void setup(final FightObject fightObject) {
        Arena.triggers.insert(0, new Trigger() {
            @Override
            public boolean check(Event event) {
                if (event instanceof Damage)
                    if (((Damage) event).dealer == fightObject) {
                        ((Damage) event).damage -= damage;
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
