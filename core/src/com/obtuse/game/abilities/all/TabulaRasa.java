package com.obtuse.game.abilities.all;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class TabulaRasa extends Ability {
    public int damage = 0;

    @Override
    public int getBaseDamage() {
        return damage;
    }

    public TabulaRasa() {
        super("tabulaRasa", 6);
        setName("Tabula Rasa");
        addTarget(HERO);
        addTarget(ENEMY);
        addTarget(SUMMON);
        addSuggestedTarget(ENEMY);

        description = "Deals " + DMG + " damage to the targeted character.";
    }

    @Override
    public void cast(FightObject caster, Holder target, FightLevel level) {
        caster.postturn.add(new Damage(damage, caster, target.fightObject));
    }

    @Override
    public void animate(FightObject caster, Holder target, FightLevel level) {
        super.animate(caster, target, level);
    }
}
