package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.events.ThunderstrikeEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Thunderstrike extends Ability {
    public int damage = 1;

    @Override
    public int getBaseDamage() {
        return damage;
    }
    public int stunTreshold = 1;

    public Thunderstrike() {
        super("thunderstrike", 3);
        setName("Thunder");
        addTarget(HEROSLOT);
        addTarget(ENEMYSLOT);
        addTarget(SUMMONSLOT);
        addSuggestedTarget(ENEMY);
        addAnimation("lightning", 0.05f, Animation.PlayMode.NORMAL,1f,1f,0,0);

        description = "Deals " + DMG + " damage to any characters standing on the slot and adjacent " +
                "slots. If total damage dealt is lower or equal to " + Integer.toString(stunTreshold) + " all affected " +
                "characters are stunned.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new ThunderstrikeEvent(damage, stunTreshold, caster, target, level));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/thunder/cast");
        super.animate(caster, target, level);
        SoundPlayer.play("fight/abilities/thunder/thunder");
        for (Holder holder : target.adjacent)
            playCharacter(holder.slot, "lightning");
        Turn.sleep(playCharacter(target.slot, "lightning"));
    }
}
