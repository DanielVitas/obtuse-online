package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.SpeedSetEvent;
import com.obtuse.game.maingame.fight.events.SpeedSwitchEvent;
import com.obtuse.game.maingame.fight.events.StealEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class SpeedSwitch extends Ability {

    public SpeedSwitch() {
        super("speedSwitch", 2);
        setName("Speed Switch");
        addTarget(HERO);
        addTarget(ENEMY);
        addTarget(SUMMON);
        addSuggestedTarget(ENEMY);
        addAnimation("up", 0.1f, Animation.PlayMode.NORMAL,1f,1f,0,0);
        addAnimation("down", 0.1f, Animation.PlayMode.NORMAL,1f,1f,0,0);

        description = "Switches speed between caster and target.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new SpeedSwitchEvent(caster, target.fightObject));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/cast6");
        super.animate(caster, target, level);
        if (caster.speed <= target.fightObject.speed) {
            play(caster, "up");
            Turn.sleep(play(target.fightObject, "down"));
        } else {
            play(caster, "down");
            Turn.sleep(play(target.fightObject, "up"));
        }
    }
}
