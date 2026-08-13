package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.PoisonEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

/**
 * The Ooze's only move. A self-centred splash (no chosen target, like Blood Sacrifice) that poisons
 * every unit standing on an adjacent slot — any side — for a couple of turns.
 */
public class PoisonSplash extends Ability {
    public int damage = 1;
    public int duration = 2;

    @Override
    public int getBaseDamage() {
        return damage;
    }

    public PoisonSplash() {
        super("poison", 3);   // reuse the poison effect's art (no dedicated poisonSplash region)
        setName("Poison Splash");
        addAnimation("poison", 0.1f, Animation.PlayMode.NORMAL, 1f, 1f, 0, 0);
        // No targets: the AI always casts it and the splash finds its own victims from the caster's slot.
        description = "Poisons every adjacent unit, dealing " + DMG + " damage at the start of its turn for "
                + Integer.toString(duration) + " turns.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        if (caster.holder == null)
            return;
        for (Holder holder : caster.holder.adjacent)
            if (holder.fightObject != null && holder.fightObject.alive() && holder.fightObject != caster)
                holder.fightObject.preturn.add(new PoisonEvent(damage, duration, caster, holder.fightObject));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/cast2");
        super.animate(caster, target, level);   // plays the caster's own cast animation
        float t = 0;
        if (caster.holder != null)
            for (Holder holder : caster.holder.adjacent)
                if (holder.fightObject != null && holder.fightObject.alive() && holder.fightObject != caster)
                    t = play(holder.fightObject, "poison");
        Turn.sleep(t);
    }
}
