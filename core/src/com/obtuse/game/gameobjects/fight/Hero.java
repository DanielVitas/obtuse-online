package com.obtuse.game.gameobjects.fight;

import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public abstract class Hero extends FightCharacter {

    public Hero(String name, float defaultFD, float summonFD, float castFD, float hurtFD, float deathFD,
                float profileDefaultFD, float profileOnTurnFD, float stunnedFD, float unstunnedFD, float deathStunnedFD,
                String hurtSound, String deathSound, String healSound) {
        super("heroes/" + name, defaultFD, summonFD, castFD, hurtFD, deathFD, profileDefaultFD, profileOnTurnFD,
                stunnedFD, unstunnedFD, deathStunnedFD, hurtSound, deathSound, healSound);
    }

    @Override
    protected void turn(Arena arena, FightLevel level) {
        choice();
    }
}
