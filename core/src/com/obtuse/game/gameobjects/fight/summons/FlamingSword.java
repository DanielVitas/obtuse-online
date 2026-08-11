package com.obtuse.game.gameobjects.fight.summons;

import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.Summon;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;

public class FlamingSword extends Summon {

    public FlamingSword() {
        super("flamingSword",0.1f,0.1f,0.1f,0.1f, 0.05f,
                1f,0.2f,1f,1f,1f, "",
                "", "");
        setName("Flaming Sword");
        setSize(1f, 1f);
        originalHP = 2;
        abilityOrbs.add(new FireBallOrb());
    }

    @Override
    public float summon() {
        SoundPlayer.play("fight/objects/flamingSword/summon");
        return super.summon();
    }
}
