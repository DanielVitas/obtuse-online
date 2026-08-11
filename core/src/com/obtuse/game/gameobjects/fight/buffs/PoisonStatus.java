package com.obtuse.game.gameobjects.fight.buffs;

public class PoisonStatus extends Status {
    public String damage = "";

    public PoisonStatus() { // not a real status
        super("Poisoned");
    }

    @Override
    public String getName() {
        return super.getName() + ": " + damage;
    }
}
