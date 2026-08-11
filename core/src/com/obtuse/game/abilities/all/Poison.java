package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.PoisonEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Poison extends Ability {
    public int damage = 1;
    public int duration = 3;

    public Poison() {
        super("poison", 3);
        setName("Poison");
        addTarget(HERO);
        addTarget(ENEMY);
        addTarget(SUMMON);
        addSuggestedTarget(ENEMY);
        addAnimation("poison", 0.1f, Animation.PlayMode.NORMAL,1f,1f,0,0);

        description = "Deals " + Integer.toString(damage) + " damage at the beginning of targets turn for " +
                Integer.toString(duration) + " turns.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        target.fightObject.preturn.add(new PoisonEvent(damage, duration, caster, target.fightObject));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/cast2");
        super.animate(caster, target, level);
        Turn.sleep(play(target.fightObject, "poison"));
    }
}
