package com.obtuse.game.gameobjects.fight.buffs;

public class RegenStatus extends Status {
    public String amount = "";

    public RegenStatus() { // not a real status; derived from HealEvents each frame
        super("Regenerating");
    }

    @Override
    public String getName() {
        return super.getName() + ": " + amount;
    }
}
