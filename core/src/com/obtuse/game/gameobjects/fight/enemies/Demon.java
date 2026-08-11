package com.obtuse.game.gameobjects.fight.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.all.FireBall;
import com.obtuse.game.abilities.all.Thunderstrike;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.items.abilityorbs.BloodSacrificeOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.FireBallOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ShieldBashOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ThunderstrikeOrb;

public class Demon extends Enemy {

    public Demon() {
        super("demon",0.1f, 0.1f,0.1f,0.1f,0.1f,1f,
                0.1f, 0.1f,0.1f,0.1f, "fight/objects/skeleton/hurt",
                "fight/objects/skeleton/death", "fight/objects/healing");
        setName("Demon");
        setSize(2f, 2f);

        addAnimation("defaultStunned",0.1f, Animation.PlayMode.LOOP);

        originalHP = 12;
        FireBallOrb fb = new FireBallOrb();
        ((FireBall) fb.ability).damage = 3;
        ThunderstrikeOrb ts = new ThunderstrikeOrb();
        ((Thunderstrike) ts.ability).damage = 2;
        abilityOrbs.add(fb, ts);
    }

    @Override
    public float defaultAnimation() {
        if (alive())
            if (isStunned)
                return play("defaultStunned",0);
            else
                return play("default",0);
        else
            return 0;
    }

    @Override
    public float summon() {
        SoundPlayer.play("fight/objects/demon/summon");
        return super.summon();
    }
}
