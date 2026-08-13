package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.BloodSacrificeEvent;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.ThunderstrikeEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class BloodSacrifice extends Ability {
    public int damage = 1;

    @Override
    public int getBaseDamage() {
        return damage;
    }

    public BloodSacrifice() {
        super("bloodSacrifice", 3);
        setName("Blood Sacrifice");
        addAnimation("blood", 0.05f, Animation.PlayMode.NORMAL,1f,1f,0,0);

        description = "Deals " + DMG + " damage to all characters in the arena except the caster." +
                " On the caster's next turn deals damage equal to the total damage dealt by this effect to the caster.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new BloodSacrificeEvent(damage, caster, level));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/bloodCast");
        super.animate(caster, target, level);
        float t = 0;
        for (Array<Holder> holderArray : new Array[]{caster.holder.arena.heroHolders, caster.holder.arena.enemyHolders,
                caster.holder.arena.summonHolders})
            for (Holder holder : holderArray)
                if (holder.fightObject != null)
                    if (holder.fightObject.alive())
                        if (holder.fightObject != caster)
                            t = play(holder.fightObject, "blood");
        SoundPlayer.play("fight/abilities/bloodSacrifice/blood");
        Turn.sleep(t);
    }
}
