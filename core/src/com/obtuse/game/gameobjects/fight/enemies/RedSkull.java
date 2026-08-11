package com.obtuse.game.gameobjects.fight.enemies;

import com.obtuse.game.abilities.all.Duel;
import com.obtuse.game.abilities.all.FireBall;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.items.abilityorbs.*;

public class RedSkull extends Enemy {

    public RedSkull() {
        super("redSkull",0.5f, 0.5f,0.2f,0.2f,0.1f,1f,
                0.2f, 0.5f,0.5f,0.1f,"fight/objects/skeleton/hurt",
                "fight/objects/skeleton/death","fight/objects/healing");
        setName("Red Skull");
        setSize(1f, 1f);
        originalHP = 7;
        FireBallOrb fb = new FireBallOrb();
        ((FireBall) fb.ability).damage = 2;
        abilityOrbs.add(fb, new BurningGroundOrb());
    }
}
