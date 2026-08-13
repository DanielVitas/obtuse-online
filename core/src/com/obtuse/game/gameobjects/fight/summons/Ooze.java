package com.obtuse.game.gameobjects.fight.summons;

import com.obtuse.game.gameobjects.fight.AI.AI;
import com.obtuse.game.gameobjects.fight.AI.SuggestedRandomAI;
import com.obtuse.game.gameobjects.fight.Summon;
import com.obtuse.game.gameobjects.items.abilityorbs.PoisonSplashOrb;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

/**
 * A neutral hazard summoned by the Huge Slime's Toxic Ooze move. It counts for neither win nor lose
 * (the battle can end while it lives), but it takes turns and auto-casts Poison Splash on whatever
 * stands beside it. 1 HP — easily popped, and it expires on its own once its Poison Splash PP runs out.
 */
public class Ooze extends Summon {
    // Act automatically each turn instead of prompting the player (Summon.turn() calls choice(),
    // which hands control to the player — right for a hero's summon, wrong for an enemy's hazard).
    private final AI aI = new SuggestedRandomAI();

    public Ooze() {
        super("ooze", 0.25f, 0.12f, 0.12f, 0.1f, 0.15f,
                1f, 0.3f, 0.5f, 0.5f, 0.1f,
                "fight/objects/skeleton/hurt", "fight/objects/skeleton/death", "fight/objects/healing");
        setName("Ooze");
        setSize(0.65f, 0.65f);
        originalHP = 1;
        abilityOrbs.add(new PoisonSplashOrb());
    }

    @Override
    protected void turn(Arena arena, FightLevel level) {
        aI.turn(arena, this, level);
    }
}
