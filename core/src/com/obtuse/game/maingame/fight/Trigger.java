package com.obtuse.game.maingame.fight;

import com.badlogic.gdx.utils.Array;

public abstract class Trigger {
    public boolean active = true;
    public boolean toRemove = false;

    public Trigger() {

    }

    public abstract boolean check(Event event);
    public abstract void run(Event event);

    public boolean checkAll(Event event) {
        if (check(event))
            return true;
        for (Event e : event.subEvents)
            if (checkAll(e))
                return true;
        return false;
    }

    protected void remove() {
        active = false;
        toRemove = true;
    }
}
