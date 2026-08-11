package com.obtuse.game.gameobjects.fight.holders.ability.backgrounds;

import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.obtuse.game.gameobjects.fight.holders.ability.AbilityBackground;
import com.obtuse.game.maingame.fight.levels.stages.InfoStage;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.w;

public class BasicAbilityBackground extends AbilityBackground {

    public BasicAbilityBackground() {
        super("basic", 1f, 1f, 0.2f);
        setSize(InfoStage.defaultAbilityWidth, InfoStage.defaultAbilityHeight);
    }

    public BasicAbilityBackground(float width, float height) {
        super("basic", 1f, 1f, 0.2f);
        setSize(width, height);
    }
}
