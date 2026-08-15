package com.obtuse.game.gameobjects.fight.holders.fightObject.slots;

import com.obtuse.game.gameobjects.fight.holders.fightObject.Slot;

public class BasicSlot extends Slot {

    public BasicSlot() {
        super("basic", 1f, 1f,1f, 1f, 0.2f);
        setSize(1f,0.5f);
        animations.get("burning").width = 2 * getWidth();
        animations.get("burning").height = 2 * getHeight();
        animations.get("burning").additionalX = - getWidth() / 2;
        // The chain (locked-slot) overlay is 2x the slot too, centred, nudged down a touch so the ring
        // encircles the slot ellipse rather than floating above it.
        animations.get("locked").width = 2 * getWidth();
        animations.get("locked").height = 2 * getHeight();
        animations.get("locked").additionalX = - getWidth() / 2;
        animations.get("locked").additionalY = - getHeight() / 2;
    }
}
