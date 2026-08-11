package com.obtuse.game.abilities.all;

import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.enemies.DummyEnemy;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.events.SummonEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;
import com.obtuse.game.maingame.fight.triggers.ExplosionDeathTrigger;

public class Dummy extends Ability {
    public int damage = 1;

    public Dummy() {
        super("dummy", 3);
        setName("Dummy");
        addTarget(EMPTYENEMYSLOT);

        description = "Summons a Dummy to an empty enemy slot. Upon dying Dummy explodes, dealing " + Integer.toString(damage) +
        " damage to adjacent characters.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        DummyEnemy dummyEnemy = new DummyEnemy();
        caster.postturn.add(new SummonEvent(caster, dummyEnemy, target, level));
        Arena.triggers.add(new ExplosionDeathTrigger(dummyEnemy, damage, level));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        SoundPlayer.play("fight/abilities/summonCast");
        super.animate(caster, target, level);
    }
}
