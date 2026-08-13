package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.SwapEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Swap extends Ability {

    public Swap() {
        super("swap",3);
        setName("Swap");
        addTarget(HEROSLOT);
        addSuggestedTarget(HEROSLOT);
        addAnimation("gust", 0.1f, Animation.PlayMode.NORMAL, 1f, 1f, 0, 0);

        description = "Teleports the caster to the targeted hero slot, and any character standing there to the caster's slot.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/wooshCast");
        caster.postturn.add(new SwapEvent(caster, target, level));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        super.animate(caster, target, level);          // the caster's cast animation
        // A gust of wind sweeps over BOTH slots that are about to swap (mirrors how Duel plays its
        // teleport on the two duellists).
        play(caster.holder.slot, "gust");
        Turn.sleep(play(target.slot, "gust"));
    }
}
