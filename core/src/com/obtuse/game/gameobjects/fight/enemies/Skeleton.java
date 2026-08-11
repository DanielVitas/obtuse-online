package com.obtuse.game.gameobjects.fight.enemies;

import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.AI.CompletelyRandomAI;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.items.abilityorbs.DuelOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.SwapOrb;

public class Skeleton extends Enemy {

    public Skeleton() {
        super("skeleton",0.5f, 0.5f,0.2f,0.2f,0.1f,1f,
                0.2f, 0.5f,0.5f,0.1f, "fight/objects/skeleton/hurt",
                "fight/objects/skeleton/death","fight/objects/healing");
        aI = new CompletelyRandomAI();
        setName("Skeleton");
        setSize(1f, 1f);
        originalHP = 3;
        abilityOrbs.add(new FireBallOrb(), new SwapOrb());
    }
}
