package com.obtuse.game.gameobjects.fight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public abstract class Summon extends FightObject {

    public Summon(String name, float defaultFD, float castFD, float hurtFD, float deathFD, float summonFD,
                  float profileDefaultFD, float profileOnTurnFD, float stunnedFD, float unstunnedFD, float deathStunnedFD,
                  String hurtSound, String deathSound, String healSound) {
        super(defaultFD, castFD, hurtFD, deathFD, summonFD, stunnedFD, unstunnedFD, deathStunnedFD, hurtSound, deathSound,
                healSound);
        path += "summons/" + name + "/";
        profile = new Profile("summons/" + name, profileDefaultFD, profileOnTurnFD);
        installAnimations();
        currentlyDisplayed.add(animations.get("default"));
    }

    @Override
    protected void turn(Arena arena, FightLevel level) {
        choice();
    }
}
