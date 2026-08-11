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
            boolean empty = ppRemaining <= 0;
            // Out of PP: don't show a number at all (the box is greyed out instead).
            ppLabel.setText(empty ? "" : toRoman(ppRemaining));
            ppLabel.setColor(com.obtuse.game.gameobjects.UI.Border.ROYAL_BRIGHTGOLD);
            // Up in the top-left corner, above the centred move name.
            float bw = abilityBackground.getWidth(), bh = abilityBackground.getHeight();
            ppLabel.setSize(bw * 0.5f, bh * 0.3f);
            ppLabel.setAlignment(Align.left);
            ppLabel.setPosition(this.x + bw * 0.08f, this.y + bh - bh * 0.32f);
            abilityBackground.setDisabled(empty);
        }
    }

    /** 1..N as Roman numerals; 0 shows as "0" (the box is greyed out anyway). */
    private static String toRoman(int n) {
        if (n <= 0) return "0";
        int[] vals = {10, 9, 5, 4, 1};
        String[] syms = {"X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vals.length; i++)
            while (n >= vals[i]) { sb.append(syms[i]); n -= vals[i]; }
        return sb.toString();
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
