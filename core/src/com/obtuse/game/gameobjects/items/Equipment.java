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
}
