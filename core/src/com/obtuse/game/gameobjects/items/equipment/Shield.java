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

    // Shield is an INCOMING reducer, so it must NOT change the wearer's outgoing ability damage
    // (previewOutgoingDamage stays the identity default). It only modifies a Health Potion to its
    // right: a heal is negative damage, so "takes 1 less damage" makes the heal bigger.
    @Override
    public int previewForHealthPotion(int d) {
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
