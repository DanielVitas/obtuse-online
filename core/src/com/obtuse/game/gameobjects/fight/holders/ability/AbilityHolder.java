package com.obtuse.game.gameobjects.fight.holders.ability;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Fonts;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.ability.backgrounds.BasicAbilityBackground;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Slot;
import com.obtuse.game.gameobjects.fight.holders.fightObject.slots.BasicSlot;
import com.obtuse.game.maingame.fight.levels.stages.InfoStage;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.w;

public class AbilityHolder {
    private static float[] ppEdge = {w(0), h(0)};
    public float x, y;
    public AbilityBackground abilityBackground;
    public AbilityInstance ability;
    public Label ppLabel = new Label("", Fonts.get("pp"));

    public AbilityHolder(float x, float y) {
        abilityBackground = new BasicAbilityBackground();
        this.x = x;
        this.y = y;
        abilityBackground.setPosition(this.x, this.y);
        ppLabel.setSize(w(0.03f), h(0.035f));
        ppLabel.setAlignment(Align.center);
    }

    public AbilityHolder(float x, float y, float width, float height) {
        abilityBackground = new BasicAbilityBackground(width, height);
        this.x = x;
        this.y = y;
        abilityBackground.setPosition(this.x, this.y);
    }

    public AbilityHolder(float x, float y, AbilityInstance ability) {
        this(x, y);
        setAbility(ability);
    }

    public AbilityHolder(float x, float y, AbilityInstance ability, InfoStage stage) {
        this(x, y, ability);
        stage.addAbilityBackground(abilityBackground);
    }

    public AbilityHolder(float x, float y, float width, float height, AbilityInstance ability, InfoStage stage) {
        this(x, y, width, height);
        setAbility(ability);
        stage.addAbilityBackground(abilityBackground);
    }

    public void refreshPPLabel(int index) {
        if (ppLabel != null) {
            int ppRemaining = ability.pp - ability.ppUsed;
            ppLabel.setText(Integer.toString(ppRemaining));
            if (ppRemaining == 0)
                ppLabel.setColor(Color.RED);
            else
                ppLabel.setColor(Color.GREEN);
            switch (index) {
                case 0:
                    ppLabel.setPosition(this.x + ppEdge[0],
                            this.y + abilityBackground.getHeight() - ppLabel.getHeight() - ppEdge[1]);
                    break;
                case 1:
                    ppLabel.setPosition(this.x + abilityBackground.getWidth() - ppLabel.getWidth() - ppEdge[0],
                            this.y + abilityBackground.getHeight() - ppLabel.getHeight() - ppEdge[1]);
                    break;
                case 2:
                    ppLabel.setPosition(this.x + ppEdge[0], this.y + ppEdge[1]);
                    break;
                case 3:
                    ppLabel.setPosition(this.x + abilityBackground.getWidth() - ppLabel.getWidth() - ppEdge[0],
                            this.y + ppEdge[1]);
                    break;
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return abilityBackground.getWidth();
    }

    public float getHeight() {
        return abilityBackground.getHeight();
    }

    public void setAbility(AbilityInstance ability) {
        ability.holder = this;
        this.ability = ability;
    }
}
