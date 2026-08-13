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
        setName("Pass");
        description = "Lose 1 life, but act first the next turn.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        // 1 unmodifiable self-damage (dealer == null, so no equipment trigger can change it)...
        Damage dmg = new Damage(damage, null, caster).cause("gave their all.");
        dmg.preform();
        // ...and gain next-turn priority: the ordering puts passers first (see Arena.order()).
        caster.passed = true;
    }

    @Override
    public void run(FightObject caster, Holder target, FightLevel level) {
        cast(caster, target, level);
    }
}
