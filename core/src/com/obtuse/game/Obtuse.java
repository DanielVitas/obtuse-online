package com.obtuse.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.obtuse.game.audio.MusicPlayer;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.Party;
import com.obtuse.game.gameobjects.fight.heroes.MageKnight;
import com.obtuse.game.gameobjects.fight.heroes.Wizard;
import com.obtuse.game.gameobjects.fight.summons.FlamingSword;
import com.obtuse.game.gameworld.GameWorld;
import com.obtuse.game.screens.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Obtuse extends Game {
	public static float cameraWidth = 10f;
	public static float ratio;
	public static int width;
	public static int height;
	private static String screenPath = "com.obtuse.game.screens.";
	public static TextureAtlas textureAtlas;
	public static float pixels = 1;
	private static long lastChecked = 0;
	public static long reactionTime = 200;
	
	@Override
	public void create () {
		MyScreen.game = this;
		//Gdx.graphics.setResizable(false);
		if (Gdx.app.getType() == Application.ApplicationType.Desktop)
			Gdx.graphics.setWindowedMode(640, 520);
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		ratio = (float) width / height;
		textureAtlas = new TextureAtlas(Gdx.files.internal("atlas/textureAtlas.atlas"));
		new Fonts();
		new GameWorld();
		new LootScreen(null);
		new FightScreen(null);
		new InventoryScreen(null);
		new SoundPlayer();
		new MusicPlayer();

		Party.party.add(new Wizard());
		Party.party.add(new MageKnight());

		changeScreen("WorldScreen");
	}

	public static void dialog(Dialog someDialog) {
		((MyScreen) MyScreen.game.screen).dialog(someDialog);
	}

	public static float w(float part) {
		return width * part;
	}

	public static float h(float part) {
		return height * part;
	}

	public static float s(float part) {
		return ((float) Math.sqrt(w(1) * h(1))) * part;
	}

	public static void changeScreen(String screenName) {
		if (!MyScreen.screens.containsKey(screenName)) try {
			Class[] parameterTypes = {String.class};
			Class myClass = Class.forName(screenPath + screenName);
			Constructor constructor = myClass.getConstructor(parameterTypes);

			Object[] parameters = {null};
			constructor.newInstance(parameters);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		MyScreen.game.setScreen(MyScreen.screens.get(screenName));
	}

	public static void changeScreenSafe(String screenName) {
		if (System.currentTimeMillis() - lastChecked > reactionTime) {
			lastChecked = System.currentTimeMillis();
			changeScreen(screenName);
		}
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
