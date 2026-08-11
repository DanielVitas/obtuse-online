package com.obtuse.game.maingame.fight;

import com.badlogic.gdx.utils.Array;

public abstract class Event {
    public int turn;
    public int uses;
    public boolean isSubEvent = false;
    public Array<Event> subEvents = new Array<Event>();

    public Event() {

    }

    public Event(int turn, int uses) {
        this();
        this.turn = turn;
        this.uses = uses;
    }

    public void addSubEventNoTrigger(Event event) {
        event.isSubEvent = true;
        subEvents.add(event);
    }

    public void addSubEvent(Event event) {
        addSubEventNoTrigger(event);
        Arena.trigger(event);
    }

    public void addSubEventNoTrigger(Event event, int index) {
        event.isSubEvent = true;
        subEvents.insert(index, event);
    }

    public void addSubEvent(Event event, int index) {
        addSubEventNoTrigger(event, index);
        Arena.trigger(event);
    }

    protected abstract float run();

    public float preform() {
        float wait = run();
        Array<Event> toRemove = new Array<Event>();
        for (Event event : subEvents) {
            wait = Math.max(wait, event.preform());
            if (event.uses != -1)
                event.uses -= 1;
            if (event.uses == 0)
                toRemove.add(event);
        }
        subEvents.removeAll(toRemove, true);
        if (isSubEvent)
            return wait;
        Turn.sleep(wait);
        return 0;
    }
}
