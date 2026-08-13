package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.events.Damage;

public class DivineOrb extends Equipment {

    public DivineOrb() {
        super("divineOrb", 1f);
        setName("Divine Orb");
        description = "Reverses the damage dealt (healing becomes damage and damage becomes healing).";
    }

    @Override
    public int previewOutgoingDamage(int d) {
        return -d;
    }

    @Override
    public void setup(final FightObject fightObject) {
        Arena.triggers.insert(0, new Trigger() {
            @Override
            public boolean check(Event event) {
                if (event instanceof Damage)
                    if (((Damage) event).dealer == fightObject) {
                        ((Damage) event).damage *= -1;
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
