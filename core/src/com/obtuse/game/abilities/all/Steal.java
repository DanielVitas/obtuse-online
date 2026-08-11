package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.StealEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Steal extends Ability {

    public Steal() {
        super("steal", 2);
        setName("Steal");
        addTarget(HERO);
        addTarget(ENEMY);
        addTarget(SUMMON);
        addSuggestedTarget(ENEMY);
        addAnimation("coins", 0.1f, Animation.PlayMode.NORMAL,1f,1f,0,0);


        description = "Steals last ability target used.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new StealEvent(caster, target.fightObject));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/wooshCast");
        super.animate(caster, target, level);
        SoundPlayer.play("fight/abilities/steal/coin");
        Turn.sleep(play(target.fightObject, "coins"));
    }
}
