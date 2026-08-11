package com.obtuse.game.maingame.world;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.obtuse.game.Obtuse;
import com.obtuse.game.bindings.BindingList;
import com.obtuse.game.gameobjects.UI.touch.TouchButton;
import com.obtuse.game.gameobjects.UI.touch.TouchIcons;
import com.obtuse.game.gameobjects.UI.touch.UseButton;
import com.obtuse.game.gameobjects.world.MainCharacter;
import com.obtuse.game.gameobjects.world.WorldCharacter;
import com.obtuse.game.gameobjects.world.WorldObject;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.maingame.GameGame;
import com.obtuse.game.maingame.world.levels.level1.Level1;
import com.obtuse.game.screens.InventoryScreen;
import com.obtuse.game.screens.MyScreen;

import static com.obtuse.game.Obtuse.changeScreen;
import static com.obtuse.game.Obtuse.changeScreenSafe;

public class WorldGame extends GameGame {
    public static long lastInteract = 0;
    private int[] moveKeys = {Input.Keys.S, Input.Keys.D, Input.Keys.A, Input.Keys.W};
    private TouchButton padDown, padRight, padLeft, padUp, bagButton;
    private UseButton useButton;

    public WorldGame(MyScreen screen) {
        super(screen);


        setLevel(new Level1(this.screen));
    }

    public void interact() {
        if (System.currentTimeMillis() - lastInteract > Obtuse.reactionTime) {
            for (WorldObject object : ((MainCharacter) level.main).sensoredObjects) {
                if (object.interact()) {
                    lastInteract = System.currentTimeMillis();
                    break;
                }
            }
        }
    }

    public void inventory() {
        ((InventoryScreen) MyScreen.screens.get("InventoryScreen")).generateInventory();
        changeScreenSafe("InventoryScreen");
    }

    private void desktopMove() {
        WorldCharacter m = (WorldCharacter) level.main;
        Vector2 v  = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(moveKeys[0]) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
            v.y -= 1;
        if (Gdx.input.isKeyPressed(moveKeys[1]) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            v.x += 1;
        if (Gdx.input.isKeyPressed(moveKeys[2]) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            v.x -= 1;
        if (Gdx.input.isKeyPressed(moveKeys[3]) || Gdx.input.isKeyPressed(Input.Keys.UP))
            v.y += 1;
        m.walk(v.nor());
        m.changeDirection(v);
    }

    @Override
    protected void desktopInit() {
        addBinding(this,"world", "interact", BindingList.keys.get("interact"));
        addBinding(this,"world", "inventory", BindingList.keys.get("inventory"));
    }

    /**
     * Builds the on screen pad on the screen's dedicated touch stage (index 2), which no
     * level owns, so it survives level changes. The arrow art is already in the atlas.
     */
    @Override
    protected void androidInit() {
        buildTouchControls();
    }

    @Override
    public void layoutChanged() {
        buildTouchControls();
    }

    private void buildTouchControls() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop || webDesktop)
            return;
        Stage ui = screen.stage(2);
        ui.clear();
        // One cell of the movement cross. The arrow art inside is drawn at its own
        // aspect ratio (see TouchButton) - the cell is the touch target, not the picture.
        // Size the controls off the SHORTER screen edge (min of width/height) instead of the
        // height: in landscape that's the height, so this is identical to before, but in
        // portrait the height is huge and height-based controls ballooned across the screen.
        float unit = Math.min(Obtuse.width, Obtuse.height);
        float cell = unit * 0.17f;
        float x = Obtuse.w(0.02f);
        float y = unit * 0.04f;
        padDown = new TouchButton(ui, "UI/arrows/down/default/main", x + cell, y, cell, cell);
        padLeft = new TouchButton(ui, "UI/arrows/left/default/main", x, y + cell, cell, cell);
        padRight = new TouchButton(ui, "UI/arrows/right/default/main", x + 2 * cell, y + cell, cell, cell);
        padUp = new TouchButton(ui, "UI/arrows/up/default/main", x + cell, y + 2 * cell, cell, cell);

        // Bag: top left, out of the way of both thumbs, with an icon instead of a word.
        float bag = unit * 0.17f;
        bagButton = new TouchButton(ui, TouchIcons.bag(),
                Obtuse.w(0.015f), Obtuse.h(1f) - bag - unit * 0.03f, bag, bag);

        // Use: a big silver disc under the right thumb, showing what it will do.
        float radius = unit * 0.19f;
        useButton = new UseButton(ui,
                Obtuse.w(1f) - radius - unit * 0.05f, radius + unit * 0.05f, radius);
    }

    /** What pressing Use would actually trigger right now, or null if nothing is in reach. */
    private String reachableAction() {
        for (WorldObject object : ((MainCharacter) level.main).sensoredObjects) {
            String icon = object.actionIcon();
            if (icon != null)
                return icon;
        }
        return null;
    }

    @Override
    protected void runAndroid() {
        if (GameWorld.pause)
            return;
        WorldCharacter m = (WorldCharacter) level.main;
        Vector2 v = new Vector2(0, 0);
        boolean use = false;
        boolean bag = false;
        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            camera(2).unproject(touchPoint.set(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            if (padDown.check(touchPoint.x, touchPoint.y)) v.y -= 1;
            if (padRight.check(touchPoint.x, touchPoint.y)) v.x += 1;
            if (padLeft.check(touchPoint.x, touchPoint.y)) v.x -= 1;
            if (padUp.check(touchPoint.x, touchPoint.y)) v.y += 1;
            if (useButton.check(touchPoint.x, touchPoint.y)) use = true;
            if (bagButton.check(touchPoint.x, touchPoint.y)) bag = true;
        }
        m.walk(v.nor());
        m.changeDirection(v);
        // read the surroundings before the list is cleared for this frame
        useButton.setIcon(reachableAction());
        if (use)
            interact();
        if (bag)
            inventory();
        ((MainCharacter) level.main).sensoredObjects.clear();
    }

    /**
     * Web input. The browser has both a mouse and a keyboard, so this drives the character
     * from either: the on-screen pad/Use/Bag respond to the pointer (the same check() calls
     * as runAndroid), and the arrow keys mirror the pad while Space mirrors Use. Both feed
     * the one movement vector, so a held arrow key and a pressed on-screen arrow combine
     * rather than fight. Android and Desktop keep their own handlers untouched.
     */
    @Override
    protected void runWeb() {
        if (webDesktop) {
            // Desktop browser: keyboard to move, mouse to interact (and hover for info).
            runDesktop();
            return;
        }
        if (GameWorld.pause)
            return;
        WorldCharacter m = (WorldCharacter) level.main;
        Vector2 v = new Vector2(0, 0);
        boolean use = false;
        boolean bag = false;
        // Pointer on the on-screen controls (mouse press = touch).
        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;
            camera(2).unproject(touchPoint.set(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            if (padDown.check(touchPoint.x, touchPoint.y)) v.y -= 1;
            if (padRight.check(touchPoint.x, touchPoint.y)) v.x += 1;
            if (padLeft.check(touchPoint.x, touchPoint.y)) v.x -= 1;
            if (padUp.check(touchPoint.x, touchPoint.y)) v.y += 1;
            if (useButton.check(touchPoint.x, touchPoint.y)) use = true;
            if (bagButton.check(touchPoint.x, touchPoint.y)) bag = true;
        }
        // Keyboard: arrow keys mirror the pad, Space mirrors the Use button.
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) v.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) v.x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) v.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) v.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) use = true;
        m.walk(v.nor());
        m.changeDirection(v);
        // read the surroundings before the list is cleared for this frame
        useButton.setIcon(reachableAction());
        if (use)
            interact();
        if (bag)
            inventory();
        ((MainCharacter) level.main).sensoredObjects.clear();
    }

    @Override
    protected void runAll() {
        //System.out.println(((MainCharacter) level.main).sensoredObjects);
    }

    @Override
    protected void runDesktop() {
        if (!GameWorld.pause) {
            super.runDesktop();
            bindingList.keyBindings("world");
            desktopMove();
            ((MainCharacter) level.main).sensoredObjects.clear();
        }
    }
}
