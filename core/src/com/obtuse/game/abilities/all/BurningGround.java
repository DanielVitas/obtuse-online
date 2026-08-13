package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.BurningGroundEvent;
import com.obtuse.game.maingame.fight.events.StealEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class BurningGround extends Ability {
    public int damage = 1;

    @Override
    public int getBaseDamage() {
        return damage;
    }

    public BurningGround() {
        super("burningGround", 3);
        setName("Burning Ground");
        addTarget(HEROSLOT);
        addTarget(ENEMYSLOT);
        addTarget(SUMMONSLOT);
        addSuggestedTarget(Ability.ENEMY);

        description = "Sets targeted slot on fire. At the end of each turn, the character standing on it takes " +
                DMG + " damage.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new BurningGroundEvent(damage, caster, target));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/fireCast");
        super.animate(caster, target, level);
        SoundPlayer.play("fight/abilities/burningGround/burn");
    }
}
