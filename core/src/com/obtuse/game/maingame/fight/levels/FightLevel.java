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
    // Set by FightGame.layoutChanged (render thread) on a resize; the move-select / targeting wait loops
    // (Turn thread) pick it up and rebuild their UI at the new size — so scene2d is only touched on the Turn thread.
    public volatile boolean relayoutFightUI = false;

    public FightLevel(MyScreen screen, FightGame game) {
        super(screen);
        this.game = game;
        add(new BackgroundStage(stage(0)));
        add(new ArenaStage(stage(1)));
        add(new InfoStage(stage(2)));
    }

    public Arena currentArena;   // the arena on screen now, so a resize can re-lay-it-out

    public void switchArena(Arena arena) {
        currentArena = arena;
        for (Holder holder : buttonMap.values()) {
            removeHolder(holder);
        }
        ((ArenaStage) stages.get(1)).switchArena(arena);
        buttonMap.clear();
        for (Array<Holder> holderArray : new Array[]{arena.heroHolders, arena.enemyHolders, arena.summonHolders})
            for (Holder holder : holderArray) {
                addHolder(holder);
            }
        updateDuelView(arena);
    }

    private com.badlogic.gdx.scenes.scene2d.Group pipGroup;
    // Smooth transition when the active arena changes: updateDuelView sets the TARGET camera; the live
    // camera eases toward it each frame in tickDuelView().
    private float curZoom = 1f, curPx, curPy, tgtZoom = 1f, tgtPx, tgtPy;
    private boolean duelViewInit = false;

    /**
     * Duel "distant arena" view. Zoom/shift the ACTIVE arena via its own world camera (so input, which
     * unprojects through the same camera, stays aligned), and render the OTHER arena small, faded and
     * in a corner (top-right when the main arena is active, top-left when the duel arena is). With no
     * duel running this targets the normal camera and clears the corner, so ordinary fights are
     * unchanged. Sets only the TARGET — tickDuelView() eases the camera to it. Called after every
     * switchArena and (via it) on resize.
     */
    public void updateDuelView(Arena activeArena) {
        float vw = com.obtuse.game.Obtuse.cameraWidth, vh = vw / com.obtuse.game.Obtuse.ratio;
        // The other live arena. INDEXED loop: game.arenas is iterated elsewhere by libGDX's pooled
        // iterator, and a nested for-each would invalidate it (see the duel turn-order fix).
        Arena other = null;
        for (int a = 0; a < game.arenas.size; a++) {
            Arena ar = game.arenas.get(a);
            if (ar != activeArena && ar.heroes.size + ar.enemies.size > 0)
                other = ar;
        }
        if (pipGroup == null) {
            pipGroup = new com.badlogic.gdx.scenes.scene2d.Group();
            pipGroup.setTransform(true);
        }
        pipGroup.clear();
        if (pipGroup.getParent() == null)
            stage(0).addActor(pipGroup);   // world background stage, so the PIP isn't zoomed by cam(1)

        boolean duelActive = activeArena instanceof com.obtuse.game.maingame.fight.arenas.DuelArena;
        if (other == null) {               // no duel: normal camera target
            tgtZoom = 1f; tgtPx = vw / 2; tgtPy = vh / 2;
        } else if (duelActive) {           // duel fills the screen, nudged right + lower
            tgtZoom = 1f; tgtPx = vw / 2 - 0.24f * vw; tgtPy = vh / 2 + 0.12f * vh;
        } else {                           // main at 0.8, nudged left
            tgtZoom = 1f / 0.8f; tgtPx = vw / 2 + 0.09f * vw * tgtZoom; tgtPy = vh / 2;
        }
        if (!duelViewInit) {               // first time: snap to the target (nothing to ease from yet)
            curZoom = tgtZoom; curPx = tgtPx; curPy = tgtPy; duelViewInit = true;
        }
        if (other != null) {               // the OTHER arena, small and faded in the corner
            for (Array<Holder> hs : new Array[]{other.heroHolders, other.enemyHolders, other.summonHolders})
                for (int i = 0; i < hs.size; i++)
                    hs.get(i).createAndAdd(pipGroup);
            pipGroup.setColor(1f, 1f, 1f, 0.5f);
            if (duelActive) {              // distant MAIN arena, top-left, 0.5
                pipGroup.setOrigin(0, vh);
                pipGroup.setScale(0.5f);
                pipGroup.setPosition(0.05f * vw, -0.05f * vh);
            } else {                       // distant DUEL arena, top-right, 0.4
                pipGroup.setOrigin(vw, vh);
                pipGroup.setScale(0.4f);
                pipGroup.setPosition(-0.1f, -0.05f * vh);
            }
        }
    }

    /** Ease the live arena camera (camera 1) toward the target from updateDuelView — the smooth
     *  transition when the turn moves between the main and duel arenas. Called every frame. */
    public void tickDuelView() {
        if (!duelViewInit) return;
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        float a = 1f - (float) Math.exp(-delta * 10f);   // ~0.3s ease, frame-rate independent
        curZoom += (tgtZoom - curZoom) * a;
        curPx += (tgtPx - curPx) * a;
        curPy += (tgtPy - curPy) * a;
        com.badlogic.gdx.graphics.OrthographicCamera cam = camera(1);
        cam.zoom = curZoom;
        cam.position.set(curPx, curPy, 0);
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
        relayoutFightUI = false;   // built fresh below at the current size; ignore any flag from an earlier turn
        buildAbilityBoxes(caster);

        while (selectedAbility == null) {
            if (relayoutFightUI) {   // a resize came in: tear the boxes down and rebuild at the new size
                relayoutFightUI = false;
                clearAbilityBoxes();
                buildAbilityBoxes(caster);
            }
            Turn.sleep();
        }
        clearAbilityBoxes();
    }

    /** Build the 4 move boxes + PP + the Pass button at the current size. */
    private void buildAbilityBoxes(FightObject caster) {
        InfoStage info = (InfoStage) stages.get(2);
        info.choice(caster);
        // The box + hit-area come from the box origin (abilityPosition), NOT the name label —
        // the name is now inset/top-aligned inside the box, so its bounds no longer match the box.
        for (int i = 0; i < info.abilitySelectLabels.size && i < caster.abilities.size; i++) {
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
        info.installAbilitySelectLabels();
    }

    /** Remove every actor built by buildAbilityBoxes so it can be rebuilt (resize) or dismissed. */
    private void clearAbilityBoxes() {
        for (AbilityHolder abilityHolder : abilityMap.values())
            abilityHolder.ppLabel.remove();
        abilityMap.clear();
        ((InfoStage) stages.get(2)).clearAbilitySelectLabels();
    }

    private void chooseTarget(FightObject caster) {
        selectedTarget = null;
        if (selectedAbility.ability.targets.size != 0) {
            targeting = true;
            back = false;
            relayoutFightUI = false;   // built fresh below at the current size
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
                if (relayoutFightUI) {   // a resize came in: rebuild the description panel + Cancel at the new size
                    relayoutFightUI = false;
                    info.showDescription(selectedAbility.ability.describedFor(caster));
                    info.showCancel();
                    cancelButton = new SquareButton(w(InfoStage.abilityPosition[8]), h(InfoStage.abilityPosition[9]),
                            w(0.14f), InfoStage.defaultAbilityHeight * 0.55f);
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

    /**
     * Big DD message shown on an enemy's turn: "[name] used [move] on [target]." when the move has a
     * target (an empty slot reads "empty slot"), otherwise just "[name] used [move].".
     */
    public void showEnemyAction(FightObject caster, AbilityInstance ability, Holder target) {
        String message = caster.getName() + " used " + ability.getName();
        if (target != null) {
            String targetName;
            if (target.fightObject == caster)
                targetName = "themself";
            else if (target.fightObject != null)
                targetName = target.fightObject.getName();
            else
                targetName = "empty slot";
            message += " on " + targetName;
        }
        message += ".";
        ((InfoStage) stages.get(2)).showDescription(message);
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
