package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.DuelEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Duel extends Ability {

    public Duel() {
        super("duel", 2);
        setName("Duel");
        addTarget(ENEMY);
        addSuggestedTarget(ENEMY);
        addAnimation("teleportation", 0.1f, Animation.PlayMode.NORMAL,1f,1f,0,0);

        description = "Teleports user and the target to a separate arena with only two slots. Now you must battle in " +
                "both primary and duel arena simultaneously. Upon either caster or target dying they both return.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new DuelEvent(caster, target.fightObject, level));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/cast3");
        super.animate(caster, target, level);
        play(caster, "teleportation");
        SoundPlayer.play("fight/abilities/duel/teleportation");
        Turn.sleep(play(target.fightObject, "teleportation"));
    }
}
