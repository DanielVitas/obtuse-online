package com.obtuse.game.gameobjects.fight.holders.fightObject.slots;

import com.obtuse.game.gameobjects.fight.holders.fightObject.Slot;

public class BasicSlot extends Slot {

    public BasicSlot() {
        super("basic", 1f, 1f,1f, 1f, 0.2f);
        setSize(1f,0.5f);
        animations.get("burning").width = 2 * getWidth();
        animations.get("burning").height = 2 * getHeight();
        animations.get("burning").additionalX = - getWidth() / 2;
    }
}
