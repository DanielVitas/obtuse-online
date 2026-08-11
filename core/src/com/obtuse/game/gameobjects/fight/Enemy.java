package com.obtuse.game.gameobjects.fight;

import com.obtuse.game.gameobjects.fight.AI.AI;
import com.obtuse.game.gameobjects.fight.AI.CompletelyRandomAI;
import com.obtuse.game.gameobjects.fight.AI.SuggestedRandomAI;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public abstract class Enemy extends FightCharacter {
    protected AI aI = new SuggestedRandomAI();

    public Enemy(String name, float defaultFD, float summonFD, float castFD, float hurtFD, float deathFD,
                 float profileDefaultFD, float profileOnTurnFD, float stunnedFD, float unstunnedFD, float deathStunnedFD,
                 String hurtSound, String deathSound, String healSound) {
        super("enemies/" + name, defaultFD, summonFD, castFD, hurtFD, deathFD,profileDefaultFD, profileOnTurnFD,
                stunnedFD, unstunnedFD, deathStunnedFD, hurtSound, deathSound, healSound);
    }

    @Override
    protected void turn(Arena arena, FightLevel level) {
        aI.turn(arena, this, level);
    }
}
