package com.obtuse.game.maingame.inventory;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Timer;
import com.obtuse.game.Obtuse;
import com.obtuse.game.bindings.BindingList;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.gameobjects.UI.SimpleArrow;
import com.obtuse.game.gameobjects.UI.touch.TouchButton;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Party;
import com.obtuse.game.gameobjects.items.Inventory;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.GameGame;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.inventory.levels.InventoryLevel;
import com.obtuse.game.maingame.inventory.levels.stages.InventoryStage;
import com.obtuse.game.maingame.loot.LootLevel;
import com.obtuse.game.maingame.loot.levels.chest.ChestLootLevel;
import com.obtuse.game.screens.MyScreen;

import static com.obtuse.game.Obtuse.changeScreen;
import static com.obtuse.game.Obtuse.changeScreenSafe;
import static com.obtuse.game.Obtuse.reactionTime;

public class InventoryGame extends GameGame {
    private Item selected;
    private SimpleArrow selectedArrow;
    private long previousActionTime = 0;
    private static boolean wait = false;
    private TouchButton backButton;

    public InventoryGame(MyScreen screen) {
        super(screen);
    }

    public void setup() {
        for (Hero hero : Party.party)
            hero.reset();
        setLevel(new InventoryLevel(screen));
        level.create();
        ((InventoryLevel) level).changeHero(0);
        // generateInventory() rebuilds every stage, so the touch control has to be recreated
        // each time the screen is opened rather than once in androidInit().
        buildTouchControls();
    }

    @Override
    public void layoutChanged() {
        // Re-place everything (grid, orb/equipment rows, hero + name) for the new surface — e.g.
        // after a phone rotation — so the whole screen re-fits the current orientation. changeHero(0)
        // keeps the same hero but rebuilds its sprite/name/positions (setup() alone leaves the hero
        // and its name at their old spot). Then rebuild the touch control for the new size.
        if (level != null && ((InventoryLevel) level).hero != null)
            ((InventoryLevel) level).changeHero(0);
        buildTouchControls();
    }

    private void buildTouchControls() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            return;
        screen.stage(2).clear();
        // Height off the shorter edge so the button isn't absurdly tall in portrait (see the
        // same reasoning in WorldGame.buildTouchControls); landscape is unchanged (min == height).
        float unit = Math.min(Obtuse.width, Obtuse.height);
        backButton = new TouchButton(screen.stage(2), "Back", "fightInfoTable",
                Obtuse.w(0.02f), Obtuse.h(1f) - unit * 0.13f, Obtuse.w(0.16f), unit * 0.10f);
    }

    public static void wait(float duration) {
        wait = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                wait = false;
            }
        }, duration);
    }

    private void unequip(int index) {
        if (allow()) {
            ((InventoryLevel) level).unequip(index);
            ((InventoryLevel) level).setup();
        }
    }

    private void equip(Item item) {
        if (allow()) {
            ((InventoryLevel) level).equip(item);
            ((InventoryLevel) level).setup();
        }
    }

    private void hovered(Item item) {
        if (selected != item) {
            loseInfo();
            selected = item;
            ((InventoryLevel) level).gatherInfo(item);
        }
    }

    private void clicked(SimpleArrow arrow, int index) {
        if (allow()) {
            wait(arrow.play("clicked"));
            if (index == 0)
                ((InventoryLevel) level).changeHero(-1);
            if (index == 1)
                ((InventoryLevel) level).changeHero(1);
        }
    }

    private void hovered(SimpleArrow arrow) {
        if (selectedArrow != arrow) {
            selectedArrow = arrow;
            selectedArrow.play("hovered",0);
        }
    }

    private void loseInfo() {
        if (selected != null || selectedArrow != null) {
            if (selectedArrow != null)
                selectedArrow.play("default",0);
            selected = null;
            selectedArrow = null;
            ((InventoryLevel) level).loseInfo();
        }
        if (selected != null) {
            selected = null;
            ((InventoryLevel) level).loseInfo();
        }
    }

    private boolean allow() {
        if (System.currentTimeMillis() - previousActionTime > reactionTime) {
            previousActionTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void world() {
        changeScreenSafe("WorldScreen");
    }

    @Override
    protected void desktopInit() {
        addBinding(this,"inventory", "world", BindingList.keys.get("inventory"));
        addBinding(this,"inventory", "world", Input.Keys.ESCAPE);
    }

    @Override
    protected void androidInit() {

    }

    @Override
    protected void runAll() {

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
        if (backButton != null) {
            unprojectTouch(2);
            if (backButton.check(touchPoint.x, touchPoint.y)) {
                if (touchReleased)
                    world();
                return;
            }
        }
        unprojectTouch(0);
        touch:
        {
            for (int i = 0; i < ((InventoryLevel) level).heroButtons.size; i++) {
                GameButton button = ((InventoryLevel) level).heroButtons.get(i);
                if (button.check(touchPoint.x, touchPoint.y)) {
                    if (touchReleased)
                        unequip(i);
                    else
                        hovered(((InventoryLevel) level).getHeroItem(i));
                    break touch;
                }
            }
            for (int i = 0; i < ((InventoryLevel) level).inventoryButtons.size; i++) {
                GameButton button = ((InventoryLevel) level).inventoryButtons.get(i);
                if (button.check(touchPoint.x, touchPoint.y)) {
                    if (touchReleased)
                        equip(Inventory.items.get(i));
                    else
                        hovered(Inventory.items.get(i));
                    break touch;
                }
            }
            for (int i = 0; i < ((InventoryLevel) level).arrowButtons.size; i++) {
                GameButton button = ((InventoryLevel) level).arrowButtons.get(i);
                if (button.check(touchPoint.x, touchPoint.y)) {
                    if (touchReleased)
                        clicked(((InventoryStage) ((InventoryLevel) level).stages.get(1)).arrows.get(i), i);
                    else
                        hovered(((InventoryStage) ((InventoryLevel) level).stages.get(1)).arrows.get(i));
                    break touch;
                }
            }
            loseInfo();
        }
    }

    @Override
    protected void runDesktop() {
        bindingList.keyBindings("inventory");
        if (!wait) {
            camera(0).unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            touch:
            {
                for (int i = 0; i < ((InventoryLevel) level).heroButtons.size; i++) {
                    GameButton button = ((InventoryLevel) level).heroButtons.get(i);
                    if (button.check(touchPoint.x, touchPoint.y)) {
                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                            unequip(i);
                        else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
                            unequip(i);
                        else
                            hovered(((InventoryLevel) level).getHeroItem(i));
                        break touch;
                    }
                }
                for (int i = 0; i < ((InventoryLevel) level).inventoryButtons.size; i++) {
                    GameButton button = ((InventoryLevel) level).inventoryButtons.get(i);
                    if (button.check(touchPoint.x, touchPoint.y)) {
                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                            equip(Inventory.items.get(i));
                        else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
                            equip(Inventory.items.get(i));
                        else
                            hovered(Inventory.items.get(i));
                        break touch;
                    }
                }
                for (int i = 0; i < ((InventoryLevel) level).arrowButtons.size; i++) {
                    GameButton button = ((InventoryLevel) level).arrowButtons.get(i);
                    if (button.check(touchPoint.x, touchPoint.y)) {
                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                            clicked(((InventoryStage) ((InventoryLevel) level).stages.get(1)).arrows.get(i), i);
                        else
                            hovered(((InventoryStage) ((InventoryLevel) level).stages.get(1)).arrows.get(i));
                        break touch;
                    }
                }
                loseInfo();
            }
        }
    }
}
