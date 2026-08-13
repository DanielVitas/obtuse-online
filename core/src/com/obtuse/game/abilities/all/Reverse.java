package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.gameobjects.items.AbilityOrb;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.ReverseEvent;
import com.obtuse.game.maingame.fight.events.SpeedSwitchEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Reverse extends Ability {

    public Reverse() {
        super("speedReverse", 2);
        setName("Reverse");

        description = "Switches speed between all characters so that the order is reversed.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new ReverseEvent(caster));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/cast6");
        super.animate(caster, target, level);
    }
}
