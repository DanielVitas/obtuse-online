package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.items.Equipment;

public class Seashell extends Equipment {

    public Seashell() {
        super("seashell", 1f);
        setName("Seashell");
        description = "Begins the battle with Echo.";
    }

    @Override
    public void setup(FightObject fightObject) {
        // "Begins the battle with Echo." Equipment setup runs during Fight.setup on the MAIN
        // thread, not the combat Turn thread. BuffEvent.preform() ends in Turn.sleep(), i.e.
        // Thread.sleep() — a harmless no-op on desktop/Android, but on the web (TeaVM) the
        // main thread cannot block, so it threw and the fight never started (black screen).
        // preform() only adds the status here anyway (no sub-events, no triggers), so apply
        // it directly. Matches BuffEvent.run() for a non-Stunned status.
        fightObject.statuses.add(new EchoStatus());
    }
}
