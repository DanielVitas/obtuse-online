package com.obtuse.game.gameobjects.fight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.gameobjects.items.Item;

public abstract class FightCharacter extends FightObject {

    public FightCharacter(String name, float defaultFD, float summonFD, float castFD, float hurtFD, float deathFD,
                          float profileDefaultFD, float profileOnTurnFD, float stunnedFD, float unstunnedFD,
                          float deathStunnedFD, String hurtSound, String deathSound, String healSound) {
        super(defaultFD, summonFD, castFD, hurtFD, deathFD, stunnedFD, unstunnedFD, deathStunnedFD, hurtSound, deathSound, healSound);
        path += "characters/" + name + "/";
        profile = new Profile("characters/" + name, profileDefaultFD, profileOnTurnFD);
        installAnimations();
        currentlyDisplayed.add(animations.get("default"));
    }
}
