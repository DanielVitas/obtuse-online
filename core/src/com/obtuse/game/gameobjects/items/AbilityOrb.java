package com.obtuse.game.gameobjects.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.obtuse.game.Obtuse;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.animations.AnimationDrawable;

public abstract class AbilityOrb extends Item {
    public Ability ability;
    protected String name;

    public AbilityOrb(String name, float defaultFD, Ability ability) {
        super();
        path += "abilityOrbs/" + name + "/";
        this.ability = ability;
        description = ability.getDescription();
        setName(ability.getName());
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("default"));
    }
}
