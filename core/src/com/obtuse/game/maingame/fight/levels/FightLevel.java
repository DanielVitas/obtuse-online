package com.obtuse.game.maingame.fight.levels;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Fonts;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.abilities.all.SkipTurn;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.buttons.SquareButton;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.ability.AbilityHolder;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.Level;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.FightGame;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.levels.stages.BackgroundStage;
import com.obtuse.game.maingame.fight.levels.stages.ArenaStage;
import com.obtuse.game.maingame.fight.levels.stages.InfoStage;
import com.obtuse.game.screens.MyScreen;

import java.util.HashMap;
import java.util.Map;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.w;

public class FightLevel extends Level {
    public FightGame game;
    public Map<GameButton, Holder> buttonMap = new HashMap<GameButton, Holder>();
    public Map<GameButton, AbilityHolder> abilityMap = new HashMap<GameButton, AbilityHolder>();
    public AbilityInstance selectedAbility;
    public Holder selectedTarget;
    public boolean targeting = false;
    public boolean back = false;
    public FightObject choosingCaster;   // whose move menu is open (for damage-modifier previews)
    public SquareButton cancelButton;    // shown in the Pass slot while choosing a target

    public FightLevel(MyScreen screen, FightGame game) {
        super(screen);
        this.game = game;
        add(new BackgroundStage(stage(0)));
        add(new ArenaStage(stage(1)));
        add(new InfoStage(stage(2)));
    }

    public void switchArena(Arena arena) {
        for (Holder holder : buttonMap.values()) {
            removeHolder(holder);
        }
        ((ArenaStage) stages.get(1)).switchArena(arena);
        buttonMap.clear();
        for (Array<Holder> holderArray : new Array[]{arena.heroHolders, arena.enemyHolders, arena.summonHolders})
            for (Holder holder : holderArray) {
                addHolder(holder);
            }
    }

    public void removeHolderInfo(Holder holder) {
        if (holder.hpLabel != null)
            holder.hpLabel.remove();
        if (holder.healthBar != null)
            holder.healthBar.remove();
    }

    public void removeHolder(Holder holder) {
        removeHolderInfo(holder);
        if (holder.fightObject != null)
            holder.fightObject.remove();
    }

    public void addHolder(Holder holder) {
        holder.createAndAdd(stage(1));
        addButton(holder);
        ((InfoStage) stages.get(2)).addHPLabel(holder);
    }

    public void refreshButtons(Arena arena) {
        buttonMap.clear();
        for (Array<Holder> holderArray : new Array[]{arena.heroHolders, arena.enemyHolders, arena.summonHolders}) {
            for (Holder holder : holderArray)
                addButton(holder);
        }
    }

    public void addButton(Holder holder) {
        buttonMap.put(new SquareButton(holder.getX(), holder.getY(), holder.getWidth(), holder.getHeight()), holder);
    }

    public void choice(FightObject caster) {
        chooseAbilityAndTarget(caster);
        abilityMap.clear();
        selectedAbility.run(caster, selectedTarget, this);
    }

    private void chooseAbilityAndTarget(FightObject caster) {
        selectedAbility = null;
        while (selectedAbility == null) {
            chooseAbility(caster);
            chooseTarget(caster);
        }
    }

    private void chooseAbility(FightObject caster) {
        choosingCaster = caster;
        InfoStage info = (InfoStage) stages.get(2);
        info.choice(caster);
        // The box + hit-area come from the box origin (abilityPosition), NOT the name label —
        // the name is now inset/top-aligned inside the box, so its bounds no longer match the box.
        for (int i = 0; i < info.abilitySelectLabels.size; i++) {
            float bx = w(InfoStage.abilityPosition[2 * i]), by = h(InfoStage.abilityPosition[2 * i + 1]);
            AbilityInstance ability = caster.abilities.get(i);
            AbilityHolder abilityHolder = new AbilityHolder(bx, by,
                    InfoStage.defaultAbilityWidth, InfoStage.defaultAbilityHeight, ability, info);
            abilityHolder.refreshPPLabel(i);
            stage(2).addActor(abilityHolder.ppLabel);
            if (ability.pp - ability.ppUsed <= 0) // grey the name when the move is out of PP
                info.abilitySelectLabels.get(i).setColor(0.5f, 0.5f, 0.55f, 1f);
            abilityMap.put(new SquareButton(bx, by, InfoStage.defaultAbilityWidth, InfoStage.defaultAbilityHeight),
                    abilityHolder);
        }
        createSkip();
        game.loseInfo();
        ((InfoStage) stages.get(2)).installAbilitySelectLabels();

        while (selectedAbility == null)
            Turn.sleep();
        for (AbilityHolder abilityHolder : abilityMap.values())
            abilityHolder.ppLabel.remove();
    }

    private void chooseTarget(FightObject caster) {
        selectedTarget = null;
        if (selectedAbility.ability.targets.size != 0) {
            targeting = true;
            back = false;
            // Where the moves were: show the move's description; where Pass was: a Cancel button.
            InfoStage info = (InfoStage) stages.get(2);
            info.showDescription(selectedAbility.ability.describedFor(caster));
            info.showCancel();
            cancelButton = new SquareButton(w(InfoStage.abilityPosition[8]), h(InfoStage.abilityPosition[9]),
                    w(0.14f), InfoStage.defaultAbilityHeight * 0.55f);
            while (selectedTarget == null) {
                if (back) {
                    back = false;
                    selectedAbility = null;
                    break;
                }
                Turn.sleep();
            }
            targeting = false;
            cancelButton = null;
            info.clearCancel();
            info.clearDescription();
        }
    }

    private void createSkip() {
        // Much smaller than the ability boxes — a compact button in the bottom-left corner.
        Label label = new Label("Pass", Fonts.get("fightInfoTable"));
        label.setWidth(w(0.14f));
        label.setHeight(InfoStage.defaultAbilityHeight * 0.55f);
        label.setAlignment(Align.center);
        label.setPosition(w(InfoStage.abilityPosition[8]), h(InfoStage.abilityPosition[9]));
        ((InfoStage) stages.get(2)).addAbilitySelectLabel(label);
        AbilityHolder abilityHolder = new AbilityHolder(label.getX(), label.getY(), label.getWidth(), label.getHeight(),
                new AbilityInstance(new SkipTurn()), (InfoStage) stages.get(2));
        abilityMap.put(new SquareButton(label.getX(), label.getY(), label.getWidth(), label.getHeight()),
                abilityHolder);
    }

    public void gatherInfo(Holder holder) {
        ((InfoStage) stages.get(2)).setup(holder);
    }

    public void gatherInfo(AbilityInstance ability) {
        ((InfoStage) stages.get(2)).setup(ability, choosingCaster);
    }

    /** Big DD message shown on an enemy's turn: "[name] used [move]." (no description). */
    public void showEnemyAction(FightObject caster, AbilityInstance ability) {
        ((InfoStage) stages.get(2)).showDescription(caster.getName() + " used " + ability.getName() + ".");
    }

    public void hideDescription() {
        ((InfoStage) stages.get(2)).clearDescription();
    }

    public void loseInfo() {
        ((InfoStage) stages.get(2)).clearInfoLabels();
    }

    @Override
    public void run() {

    }

    @Override
    public void basic() {
    }
}
