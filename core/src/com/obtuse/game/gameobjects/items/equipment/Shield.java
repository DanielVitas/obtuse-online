package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.events.Damage;

public class Shield extends Equipment {
    public int damage = 1;

    public Shield() {
        super("shield",1f);
        setName("Shield");
        description = "Takes " + Integer.toString(damage) + " less damage.";
    }

    // So a Shield to the LEFT of a Health Potion also modifies its heal (heal is negative damage, so
    // "takes 1 less damage" makes the heal bigger). Mirrors the trigger below (damage -= damage).
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
                    if (((Damage) event).taker == fightObject) {
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
