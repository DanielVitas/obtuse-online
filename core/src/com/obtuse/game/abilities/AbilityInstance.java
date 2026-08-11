package com.obtuse.game.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.obtuse.game.Fonts;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.ability.AbilityHolder;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class AbilityInstance {
    public Ability ability;
    public AbilityHolder holder;
    public int pp;
    public int ppUsed = 0;

    public AbilityInstance(Ability ability) {
        this.ability = ability;
        this.pp = ability.pp;
    }

    public void run(FightObject caster, Holder target, FightLevel level) {
        ppUsed += 1;
        caster.lastUsed = this;
        ability.run(caster, target, level);
    }

    public boolean validTarget(Holder holder) {
        return ability.validTarget(holder);
    }

    public boolean validInverseTarget(Holder holder) {
        return ability.validInverseTarget(holder);
    }

    public String getName() {
        return ability.getName();
    }

    public String getDescription() {
        return ability.description;
    }
}
