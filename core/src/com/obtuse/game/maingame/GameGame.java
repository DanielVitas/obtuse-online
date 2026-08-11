package com.obtuse.game.maingame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.obtuse.game.bindings.Binding;
import com.obtuse.game.bindings.BindingList;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.screens.MyScreen;

import java.lang.reflect.Method;

public abstract class GameGame {
    public Level level;
    protected MyScreen screen;
    protected BindingList bindingList;
    protected Vector3 touchPoint = new Vector3(0, 0, 0);

    /** Touch state, maintained by pollTouch(). */
    protected boolean touchHeld = false;
    protected boolean touchReleased = false;
    protected float touchScreenX = 0;
    protected float touchScreenY = 0;

    public GameGame(MyScreen screen) {
        this.screen = screen;
        bindingList = new BindingList();
        setup();
    }

    protected void addBinding(Object object, String filter, String functionName, int key) {
        bindingList.add(filter, new Binding(object, getMethod(object.getClass(), functionName), key));
    }

    protected void addBinding(Object object, String filter, String functionName, GameButton button) {
        bindingList.add(filter, new Binding(object, getMethod(object.getClass(), functionName), button));
    }

    protected Method getMethod(Class functionClass, String functionName) {
        try {
            return functionClass.getMethod(functionName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected OrthographicCamera camera(int index) {
        return screen.camera(index);
    }

    protected abstract void desktopInit();
    protected abstract void androidInit();

    protected void iOSInit() {
        androidInit();
    }

    /**
     * True when the web build is running on a device with a real mouse (a desktop browser),
     * set from JS at startup by the :teavm launcher. A desktop browser then uses the full
     * desktop input model — keyboard plus a mouse that shows an element's description on hover
     * (no click) and acts on click — while a touch device keeps the on-screen controls. false
     * on Android/desktop-app (they never consult it; those platforms take their own branches).
     */
    public static boolean webDesktop = false;

    /**
     * The browser is a hybrid. A desktop browser (mouse + keyboard) uses the same input as the
     * old desktop build — including hover-to-show-description — while a touch device uses the
     * on-screen controls. Kept as its own branch so the Android/Desktop/iOS paths are untouched.
     */
    protected void webInit() {
        if (webDesktop)
            desktopInit();
        else
            androidInit();
    }

    private void setup() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            desktopInit();
        }
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            androidInit();
        }
        if (Gdx.app.getType() == Application.ApplicationType.iOS) {
            iOSInit();
        }
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            webInit();
        }
    }

    protected void setLevel(Level level) {
        try {
            this.level.dispose();
        } catch (Exception ignored) {}
        this.level = level;
        level.create();
    }

    protected abstract void runAll();

    protected void runDesktop() {

    }

    protected void runAndroid() {

    }

    /** Called after the surface size changes, so on screen controls can be rebuilt. */
    public void layoutChanged() {

    }

    /**
     * The desktop input model is "the pointer is always somewhere": moving over a control
     * hovers it, pressing a button clicks it. A finger has no resting position, so on touch
     * devices holding the finger down stands in for hovering and lifting it stands in for
     * clicking. That keeps every info panel and highlight in the game working unchanged.
     * Call once per frame, before reading the fields below.
     */
    protected void pollTouch() {
        touchReleased = false;
        if (Gdx.input.isTouched(0)) {
            touchScreenX = Gdx.input.getX(0);
            touchScreenY = Gdx.input.getY(0);
            touchHeld = true;
        } else if (touchHeld) {
            touchHeld = false;
            touchReleased = true;
        } else if (Gdx.input.justTouched()) {
            // A tap that starts and ends inside a single frame never shows up in
            // isTouched(), so it would otherwise be swallowed. Count it as a release.
            touchScreenX = Gdx.input.getX(0);
            touchScreenY = Gdx.input.getY(0);
            touchReleased = true;
        }
    }

    /** True while a finger rests on the screen, and for the single frame after it lifts. */
    protected boolean touching() {
        return touchHeld || touchReleased;
    }

    /** Puts the current (or just released) touch into touchPoint, in the given camera's units. */
    protected void unprojectTouch(int cameraIndex) {
        camera(cameraIndex).unproject(touchPoint.set(touchScreenX, touchScreenY, 0));
    }

    protected void runIOS() {
        runAndroid();
    }

    /** See webInit(): desktop browsers run the desktop handler (mouse hover + click + keyboard),
     *  touch devices run the touch handler. */
    protected void runWeb() {
        if (webDesktop)
            runDesktop();
        else
            runAndroid();
    }

    public void run() {
        runAll();
        level.run();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            runDesktop();
        }
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            runAndroid();
        }
        if (Gdx.app.getType() == Application.ApplicationType.iOS) {
            runIOS();
        }
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            runWeb();
        }
    }

}
