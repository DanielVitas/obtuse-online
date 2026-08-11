package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.DamageSlot;
import com.obtuse.game.maingame.fight.events.StealEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class DelayedHit extends Ability {
    public int damage = 2;

    public DelayedHit() {
        super("delayedHit", 3);
        setName("Delayed Hit");
        addTarget(HEROSLOT);
        addTarget(ENEMYSLOT);
        addTarget(SUMMONSLOT);
        addSuggestedTarget(ENEMY);
        addAnimation("mark", 0.2f, Animation.PlayMode.NORMAL,1f,1f,0,0);


        description = "On casters next turn deals " + Integer.toString(damage) + " damage to any character standing " +
                "on targeted slot.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.preturn.add(new DamageSlot(damage, caster, target));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        super.animate(caster, target, level);
        SoundPlayer.play("fight/abilities/delayedHit/mark");
        Turn.sleep(playCharacter(target.slot, "mark"));
    }
}
