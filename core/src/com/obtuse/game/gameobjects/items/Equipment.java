package com.obtuse.game.gameobjects.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.gameobjects.fight.FightObject;

public abstract class Equipment extends Item {

    public Equipment(String name, float defaultFD) {
        super();
        path += "equipment/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }

    public abstract void setup(FightObject fightObject);

    /**
     * Preview how this equipment changes OUTGOING damage the wearer deals (for tooltips), mirroring
     * the runtime Damage trigger. Default: no change. Only outgoing modifiers override this.
     */
    public int previewOutgoingDamage(int damage) {
        return damage;
    }
}
