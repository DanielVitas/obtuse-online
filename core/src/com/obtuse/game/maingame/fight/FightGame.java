package com.obtuse.game.maingame.fight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.obtuse.game.Obtuse;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.abilities.all.SkipTurn;
import com.obtuse.game.bindings.BindingList;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.buttons.SquareButton;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.holders.ability.AbilityHolder;
import com.obtuse.game.gameobjects.fight.holders.ability.BackgroundBackground;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.GameGame;
import com.obtuse.game.maingame.fight.levels.FightLevel;
import com.obtuse.game.maingame.fight.levels.stages.ArenaStage;
import com.obtuse.game.maingame.fight.levels.stages.InfoStage;
import com.obtuse.game.progress.ProgressKeeper;
import com.obtuse.game.screens.MyScreen;

import java.util.ConcurrentModificationException;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.w;

public class FightGame extends GameGame {
    private Turn turn;
    public Array<Arena> arenas = new Array<Arena>();
    public Array<Hero> allHeroes = new Array<Hero>();
    public Array<Enemy> allEnemies = new Array<Enemy>();
    public AbilityHolder selectedAbilityHolder;
    public Holder selectedHolder;
    private boolean wait = false;
    private Fight fight;
    private final Array<FightObject> summonOrder = new Array<FightObject>();
    private boolean needSummon = false;

    public FightGame(MyScreen screen) {
        super(screen);
    }

    public void setup(Fight fight){
        this.fight = fight;
        turn = new Turn() {
            @Override
            public void run() {

            }
        };
        arenas.clear();
        setLevel(new FightLevel(screen, this));
        level.create();
        //level.stage(2).addActor(new BackgroundBackground(w(0.4f), h(0.075f), w(0.2f), h(0.15f)));
        fight.setup(this);
        // Hide everyone (and their health bars). The actual sequential summon runs at the START of
        // the combat turn (runAll), which has the proper coroutine context so Turn.sleep() pauses.
        for (Array<Holder> holderArray : new Array[]{arenas.get(0).heroHolders, arenas.get(0).enemyHolders, arenas.get(0).summonHolders})
            for (final Holder holder : holderArray)
                if (holder.fightObject != null) {
                    holder.fightObject.setVisible(false);
                    if (holder.healthBar != null)
                        holder.healthBar.setVisible(false);
                }
        // Summon in the EXACT order the first turn will use: preorder() computes the arena's
        // fighterOrder now (shuffle + speed sort), and the first conduct() reuses it instead of
        // re-shuffling, so the entrance order matches who acts first.
        arenas.get(0).preorder();
        summonOrder.clear();
        summonOrder.addAll(arenas.get(0).fighterOrder);
        needSummon = true;
    }

    // Reveal each fighter one at a time in first-turn order: show it, start its health-bar fill, play
    // its entrance animation, and wait a beat before the next (so even fighters with no summon
    // animation clearly enter one by one — the health bar filling is then their entrance).
    private void summonInOrder() {
        for (FightObject fightObject : summonOrder) {
            fightObject.setVisible(true);
            if (fightObject.holder != null && fightObject.holder.healthBar != null)
                fightObject.holder.healthBar.setVisible(true);
            Turn.sleep(Math.max(fightObject.summon(), 0.8f));
        }
    }

    private void win() {
        //Turn.sleep();
        ProgressKeeper.add("win",fight.fightID);
        fight.end(true);
    }

    private void lose() {
        //Turn.sleep();
        ProgressKeeper.add("lose",fight.fightID);
        fight.end(false);
    }

    private void check() {
        lose : {
            for (Hero hero : allHeroes)
                if (hero.alive())
                    break lose;
            lose();
        }
        win : {
            for (Enemy enemy : allEnemies)
                if (enemy.alive())
                    break win;
            win();
        }
    }

    public void choice(FightObject caster) {
        ((FightLevel) level).choice(caster);
    }

    private void clicked(final AbilityHolder abilityHolder) {
        if (abilityHolder.ability.pp > abilityHolder.ability.ppUsed ||
                (abilityHolder.ability.ability instanceof SkipTurn)) {
            wait = true;
            new Turn() {
                @Override
                public void run() {
                    Turn.sleep(abilityHolder.abilityBackground.play("selected", 0));
                    ((FightLevel) level).selectedAbility = abilityHolder.ability;
                    ((InfoStage) level.stages.get(2)).clearAbilitySelectLabels();
                    wait = false;
                }
            };
        }
    }

    private void hovered(AbilityHolder abilityHolder) {
        if (selectedAbilityHolder != abilityHolder) {
            loseInfo();
            abilityHolder.abilityBackground.play("hovered", 0);
            selectedAbilityHolder = abilityHolder;
            ((FightLevel) level).gatherInfo(abilityHolder.ability);
        }
    }

    private void clicked(Holder holder) {

    }

    private void hovered(Holder holder) {
        if (selectedHolder != holder) {
            loseInfo();
            selectedHolder = holder;
            ((FightLevel) level).gatherInfo(holder);
            selectedHolder.slot.play("hovered",0);
            if (selectedHolder.fightObject != null)
                selectedHolder.fightObject.profile.hover();
        }
    }

    private void clickedTargeting(Holder holder) {
        if (((FightLevel) level).selectedAbility.validTarget(holder))
            ((FightLevel) level).selectedTarget = holder;
    }

    private void hoveredTargeting(Holder holder) {
        if (selectedHolder != holder) {
            loseInfo();
            selectedHolder = holder;
            ((FightLevel) level).gatherInfo(holder);
            if (((FightLevel) level).selectedAbility.validTarget(holder))
                selectedHolder.slot.play("targeted",0);
            else
                selectedHolder.slot.play("hovered",0);
        }
    }

    public void loseInfo() {
        if (!wait)
            if (selectedAbilityHolder != null || selectedHolder != null) {
                if (selectedHolder != null) {
                    selectedHolder.playSlotAnimation();
                    if (selectedHolder.fightObject != null)
                        selectedHolder.fightObject.profile.unhover();
                }
                if (selectedAbilityHolder != null)
                    selectedAbilityHolder.abilityBackground.play("default",0);
                selectedAbilityHolder = null;
                selectedHolder = null;
                ((FightLevel) level).loseInfo();
            }
    }

    public void back() {
        ((FightLevel) level).back = true;
    }

    @Override
    protected void desktopInit() {
        addBinding(this,"fight", "back", Input.Keys.ESCAPE);
    }

    @Override
    protected void androidInit() {

    }

    @Override
    protected void runAndroid() {
        pollTouch();
        if (wait)
            return;
        if (!touching()) {
            loseInfo();
            return;
        }
        try {
            if (!((FightLevel) level).targeting)
                touch:{
                    unprojectTouch(2);
                    for (GameButton button : ((FightLevel) level).abilityMap.keySet())
                        if (button.check(touchPoint.x, touchPoint.y)) {
                            if (touchReleased)
                                clicked(((FightLevel) level).abilityMap.get(button));
                            else
                                hovered(((FightLevel) level).abilityMap.get(button));
                            break touch;
                        }
                    unprojectTouch(0);
                    for (GameButton button : ((FightLevel) level).buttonMap.keySet())
                        if (button.check(touchPoint.x, touchPoint.y)) {
                            if (touchReleased)
                                clicked(((FightLevel) level).buttonMap.get(button));
                            else
                                hovered(((FightLevel) level).buttonMap.get(button));
                            break touch;
                        }
                    loseInfo();
                }
            else
                touch:{
                    unprojectTouch(0);
                    for (GameButton button : ((FightLevel) level).buttonMap.keySet())
                        if (button.check(touchPoint.x, touchPoint.y)) {
                            if (touchReleased)
                                clickedTargeting(((FightLevel) level).buttonMap.get(button));
                            else
                                hoveredTargeting(((FightLevel) level).buttonMap.get(button));
                            break touch;
                        }
                    // Lifting a finger away from every slot cancels targeting, the job
                    // ESCAPE does on the desktop.
                    if (touchReleased)
                        back();
                    loseInfo();
                }
        } catch (ConcurrentModificationException e) {e.printStackTrace();}
    }

    @Override
    protected void runAll() {
        if (!turn.isAlive())
            turn = new Turn() {
                @Override
                public void run() {
                    if (needSummon) {
                        summonInOrder();
                        needSummon = false;
                    }
                    Array<Arena> toRemove = new Array<Arena>();
                    for (Arena arena : arenas)
                        if (arena.finished)
                            toRemove.add(arena);
                    arenas.removeAll(toRemove, true);
                    for (Arena arena : arenas) {
                        ((FightLevel) level).switchArena(arena);
                        arena.conduct();
                        Turn.sleep();
                    }
                    check();
                    Game game = MyScreen.game;
                }
            };
    }

    @Override
    protected void runDesktop() {
        super.runDesktop();
        bindingList.keyBindings("fight");
        if (!wait)
            try {
                if (!((FightLevel) level).targeting)
                    touch:{
                        camera(2).unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                        for (GameButton button : ((FightLevel) level).abilityMap.keySet())
                            if (button.check(touchPoint.x, touchPoint.y)) {
                                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                                    clicked(((FightLevel) level).abilityMap.get(button));
                                else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
                                    System.out.println("hey");
                                else
                                    hovered(((FightLevel) level).abilityMap.get(button));
                                break touch;
                            }
                        camera(0).unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                        for (GameButton button : ((FightLevel) level).buttonMap.keySet())
                            if (button.check(touchPoint.x, touchPoint.y)) {
                                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                                    clicked(((FightLevel) level).buttonMap.get(button));
                                else
                                    hovered(((FightLevel) level).buttonMap.get(button));
                                break touch;
                            }
                        loseInfo();
                    }
                else
                    touch:{
                        camera(0).unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                        for (GameButton button : ((FightLevel) level).buttonMap.keySet())
                            if (button.check(touchPoint.x, touchPoint.y)) {
                                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                                    clickedTargeting(((FightLevel) level).buttonMap.get(button));
                                else
                                    hoveredTargeting(((FightLevel) level).buttonMap.get(button));
                                break touch;
                            }
                        loseInfo();
                    }
            } catch (ConcurrentModificationException e) {e.printStackTrace();}
    }
}
