package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Party;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.DeathEvent;

public class CrownOfThorns extends Equipment {
    public int hpPenalty = 3;

    public CrownOfThorns() {
        super("crownOfThorns",1f);
        setName("Crown of Thorns");
        description = "Wearer has " + Integer.toString(hpPenalty) + " less HP. Upon death, restores PP to the whole party.";
    }

    @Override
    public int previewMaxHp(int hp) {
        return hp - hpPenalty;
    }

    @Override
    public void setup(final FightObject fightObject) {
        Arena.triggers.insert(0, new Trigger() {
            @Override
            public boolean check(Event event) {
                if (event instanceof DeathEvent)
                    if (((DeathEvent) event).target == fightObject) {
                        for (Hero hero : Party.party)
                            for (AbilityInstance ability : hero.abilities)
                                ability.ppUsed = 0;
                        //return true;
                    }
                return false;
            }

            @Override
            public void run(Event event) {

            }
        });
        fightObject.hp -= hpPenalty;
        if (fightObject.hp <= 0)
            fightObject.death();
    }
}
