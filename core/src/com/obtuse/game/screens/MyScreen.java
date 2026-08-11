package com.obtuse.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    public void resetStage(int index) {
        if (stages.size >= index + 1) {
            stages.insert(index, new Stage(new ScreenViewport()));
            cameras.insert(index, (OrthographicCamera) stages.get(index).getViewport().getCamera());
            stages.removeIndex(index + 1);
            cameras.removeIndex(index + 1);
            multiplexer.addProcessor(stages.get(index));
        }
    }

    public void resetStages() {
        for (int i = 0; i < stages.size; i++) {
            float width = camera(i).viewportWidth;
            float height = camera(i).viewportHeight;
            Vector3 position = camera(i).position;
            resetStage(i);
            camera(i).viewportWidth = width;
            camera(i).viewportHeight = height;
            camera(i).position.set(position);
        }
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
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (Stage stage : stages)
            try {
                stage.act();
            } catch (NullPointerException e) {e.printStackTrace();}
        for (Stage stage : stages)
            try {
                stage.getActors().sort(new Comparator<Actor>() {
                    @Override
                    public int compare(Actor o1, Actor o2) {
                        if (o1 instanceof DepthObject && o2 instanceof DepthObject)
                            return (int) -((((DepthObject) o1).getZ() - ((DepthObject) o2).getZ()) * pow(16,1));
                        else
                            return 0;
                    }
                });
                stage.draw();
            } catch (IndexOutOfBoundsException e) {e.printStackTrace();}
            catch (IllegalStateException e) {e.printStackTrace();}
            catch (NullPointerException e) {e.printStackTrace();}
            // On the web (TeaVM) a SpriteBatch draw of a null/undefined TextureRegion throws
            // a JS TypeError instead of being tolerated as it is on desktop/Android, and that
            // error is not a NullPointerException so the catches above miss it. Left unguarded
            // it aborts the whole frame and blacks out the entire screen (this is what made the
            // inventory render as a black screen). Isolate a bad draw to its own stage instead.
            catch (Throwable e) {e.printStackTrace();}
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
