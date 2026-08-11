package com.obtuse.game.gameobjects.fight.buffs;

public abstract class Status {
    String displayName;

    public Status(String name) {
        setName(name);
    }

    public void setName(String name) {
        displayName = name;
    }

    public String getName() {
        return displayName;
    }
}
