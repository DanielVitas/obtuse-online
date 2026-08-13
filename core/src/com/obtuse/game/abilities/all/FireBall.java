package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class FireBall extends Ability {
    public int damage = 1;

    @Override
    public int getBaseDamage() {
        return damage;
    }

    public FireBall() {
        super("fireBall", 3);
        setName("Fire Ball");
        addTarget(HERO);
        addTarget(ENEMY);
        addTarget(SUMMON);
        addSuggestedTarget(ENEMY);
        addAnimation("explosion", 0.05f, Animation.PlayMode.NORMAL,1f,1f,0,0);
        // A few fire pixels linger over the CASTER while their cast animation plays — reuses the slot
        // "burning" flames so no new art is needed.
        addAnimationFrom("ember", "fight/slots/basic/burning", 0.13f, Animation.PlayMode.LOOP, 0.9f, 0.9f, 0, 0);

        description = "Deals " + DMG + " damage to the targeted character.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new Damage(damage, caster, target.fightObject));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/fireCast");
        play(caster, "ember");             // embers linger over the caster during the cast (non-blocking overlay)
        super.animate(caster, target, level);
        SoundPlayer.play("fight/abilities/fireball/explosion");
        Turn.sleep(play(target.fightObject, "explosion"));
    }
}
