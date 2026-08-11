package com.obtuse.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.DepthObject;
import com.obtuse.game.maingame.GameGame;
import com.obtuse.game.Fonts;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.levels.stages.InfoStage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.obtuse.game.Fonts.generateFonts;
import static com.obtuse.game.Obtuse.cameraWidth;
import static com.obtuse.game.Obtuse.ratio;
import static java.lang.Math.pow;

public abstract class MyScreen implements Screen {
    public static Obtuse game;
    protected String name;
    protected InputMultiplexer multiplexer;
    protected Array<Stage> stages = new Array<Stage>();
    private Array<OrthographicCamera> cameras = new Array<OrthographicCamera>();
    public static Map<String, Screen> screens = new HashMap<String, Screen>();
    public GameGame gameGame;
    public static Turn dialogInstance;
    /** How many of this screen's cameras were put into world units by fixCamera(). */
    private int worldCameras = 0;

    public MyScreen(String name) {
        if (name == null)
            name = this.getClass().getSimpleName();
        this.name = name;
        multiplexer = new InputMultiplexer();

        addStage();
        screens.put(name, this);
        create();
    }

    public abstract void dialog(Dialog someDialog);

    protected void fixCamera(int index) {
        if (index + 1 > worldCameras)
            worldCameras = index + 1;
        camera(index).viewportWidth = cameraWidth;
        camera(index).viewportHeight = camera(index).viewportWidth * Gdx.graphics.getHeight() /
                Gdx.graphics.getWidth();
        camera(index).position.set(camera(index).viewportWidth / 2, camera(index).viewportHeight / 2, 0);
    }

    public void addStage(Stage stage) {
        stages.add(stage);
        OrthographicCamera camera = (OrthographicCamera) stage.getViewport().getCamera();
        cameras.add(camera);
        multiplexer.addProcessor(stage);
    }

    // REUSE the existing Stage (and its SpriteBatch + camera) — just drop its actors — instead of
    // building a brand new one. The old version replaced the Stage on every fight/inventory/loot
    // entry and discarded the previous one WITHOUT disposing it, leaking a Stage+SpriteBatch each
    // time. On a real mobile GL context those rebuilt world-camera stages rendered nothing at all
    // (desktop/emulation tolerated it), which is why fight & inventory sprites were invisible on
    // the phone while the never-rebuilt overworld stage always rendered. Keeping the stage matches
    // the overworld and removes the leak. The stage stays registered on the input multiplexer.
    public void resetStage(int index) {
        if (stages.size >= index + 1)
            stages.get(index).clear();
    }

    public void resetStages() {
        for (int i = 0; i < stages.size; i++)
            resetStage(i);
        refreshLayout();
    }

    public void addStage() {
        Stage stage = new Stage(new ScreenViewport());
        addStage(stage);
    }

    public OrthographicCamera camera(int index) {
        return cameras.get(index);
    }

    public Stage stage(int index) {
        return stages.get(index);
    }

    public abstract void create();
    protected abstract void loop();

    @Override
    public void show() {
        // Re-measure against the current surface whenever this screen becomes active. libGDX also
        // calls resize() right after show(), but doing it here too is harmless and keeps a screen
        // that is shown without a following resize correctly laid out.
        refreshLayout();
        Gdx.input.setInputProcessor(multiplexer);
    }

    // One fresh SpriteBatch PER STAGE, created lazily inside render() — i.e. in this screen's live
    // GL frame. On real mobile GL the Stage's OWN batch (created during Obtuse.create()/scene
    // setup, outside any render frame) draws nothing, while a batch created inside render draws
    // fine through the same camera (proven by on-device probes). We therefore draw each stage's
    // root through its own render-frame batch instead of stage.draw(). Per-stage (not shared) so a
    // failure in one stage can never leave a shared batch mid-begin() and black out the others,
    // and end() is guaranteed in finally. Desktop/Android/emulation were unaffected either way.
    private SpriteBatch[] frameBatches;
    private static final Comparator<Actor> DEPTH = new Comparator<Actor>() {
        @Override
        public int compare(Actor o1, Actor o2) {
            if (o1 instanceof DepthObject && o2 instanceof DepthObject)
                return (int) -((((DepthObject) o1).getZ() - ((DepthObject) o2).getZ()) * pow(16, 1));
            else
                return 0;
        }
    };

    @Override
    public void render(float delta) {
        // Pick up a surface change (e.g. a phone rotation) even if the backend never fires resize()
        // — on the web, orientation changes don't reliably deliver a libGDX resize() callback, so
        // the whole game stayed at the old aspect until reload. Re-derive everything when it moves.
        if (Gdx.graphics.getWidth() != Obtuse.width || Gdx.graphics.getHeight() != Obtuse.height)
            refreshLayout();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (Stage stage : stages)
            try {
                stage.act();
            } catch (NullPointerException e) {e.printStackTrace();}
        if (frameBatches == null || frameBatches.length != stages.size)
            frameBatches = new SpriteBatch[stages.size];
        for (int i = 0; i < stages.size; i++) {
            SpriteBatch batch = frameBatches[i];
            if (batch == null) batch = frameBatches[i] = new SpriteBatch();
            try {
                stage(i).getActors().sort(DEPTH);
                OrthographicCamera cam = camera(i);
                cam.update();
                batch.setProjectionMatrix(cam.combined);
                batch.setColor(1f, 1f, 1f, 1f);
                batch.begin();
                stage(i).getRoot().draw(batch, 1);
            }
            // On the web (TeaVM) a SpriteBatch draw of a null/undefined TextureRegion throws a JS
            // TypeError (not an NPE); catch everything so a bad draw stays isolated to its stage.
            catch (Throwable e) {e.printStackTrace();}
            finally {
                try { if (batch.isDrawing()) batch.end(); } catch (Throwable e) {e.printStackTrace();}
            }
        }
        loop();
    }

    /**
     * Re-derives every size from the surface as it is right now. The 2018 code captured
     * the screen size once during create() and never looked again, so if Android hands
     * the app a portrait surface before the landscape lock settles, every position in
     * the game stays wrong for the rest of the session. Called on resize, and after
     * resetStages() so a screen rebuilt later is measured against the current surface.
     */
    public void refreshLayout() {
        Obtuse.width = Gdx.graphics.getWidth();
        Obtuse.height = Gdx.graphics.getHeight();
        Obtuse.ratio = (float) Obtuse.width / Obtuse.height;
        Fonts.refresh();
        InfoStage.refreshMetrics();
        for (Stage stage : stages)
            stage.getViewport().update(Obtuse.width, Obtuse.height, true);
        for (int i = 0; i < worldCameras; i++)
            fixCamera(i);
        Gdx.app.log("OBTUSE", name + " layout: surface " + Obtuse.width + "x" + Obtuse.height
                + " ratio " + Obtuse.ratio + ", world cameras " + worldCameras
                + (worldCameras > 0 ? " -> " + camera(0).viewportWidth + "x" + camera(0).viewportHeight : ""));
        if (gameGame != null)
            gameGame.layoutChanged();
    }

    @Override
    public void resize(int width, int height) {
        refreshLayout();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        for (Stage stage : stages)
            stage.dispose();
    }
}
