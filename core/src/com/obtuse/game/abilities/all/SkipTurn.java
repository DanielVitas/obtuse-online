package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class SkipTurn extends Ability {
    public int damage = 1;

    public SkipTurn() {
        super("skipTurn", 0);
        setName("Skip Turn");
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        Damage dmg = new Damage(damage, caster, caster);
        dmg.preform();
    }

    @Override
    public void run(FightObject caster, Holder target, FightLevel level) {
        cast(caster, target, level);
    }
}
