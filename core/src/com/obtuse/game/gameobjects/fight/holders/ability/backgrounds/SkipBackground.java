package com.obtuse.game.gameobjects.fight.holders.ability.backgrounds;

import com.obtuse.game.gameobjects.fight.holders.ability.AbilityBackground;
import com.obtuse.game.maingame.fight.levels.stages.InfoStage;

public class SkipBackground extends AbilityBackground {

    public SkipBackground() {
        super("skip", 1f, 1f, 0.2f);
        setSize(InfoStage.defaultAbilityWidth, InfoStage.defaultAbilityHeight);
    }
}
