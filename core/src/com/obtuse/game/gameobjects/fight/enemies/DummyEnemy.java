package com.obtuse.game.gameobjects.fight.enemies;

import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;

public class DummyEnemy extends Enemy {

    public DummyEnemy() {
        super("dummy",0.5f, 0.5f,0.2f,0.2f,0.1f,1f,
                0.2f,1f,1f,0.1f, "",
                "fight/objects/dummy/explosion", "");
        setName("Dummy");
        setSize(1f, 1f);
        originalHP = 1;
    }
}
