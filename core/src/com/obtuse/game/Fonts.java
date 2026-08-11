package com.obtuse.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.HashMap;
import java.util.Map;

public class Fonts {
    private static Map<String, BitmapFont> fonts = new HashMap<String, BitmapFont>();
    private static float scale = 1.3f;
    /** The sizes below were chosen for the 520 pixel tall desktop window of 2018. */
    private static final float designHeight = 520f;
    private static int generatedHeight = 0;

    public Fonts() {
        generateFonts();
    }

    /** Rebuild the fonts only when the surface height actually changed. */
    public static void refresh() {
        if (Gdx.graphics.getHeight() != generatedHeight && Gdx.graphics.getHeight() > 0)
            generateFonts();
    }

    public static void generateFonts() {
        // Absolute pixel sizes that suited a 520px window are unreadable on a 1080px
        // phone, so every size below is scaled by how much taller the surface really is.
        generatedHeight = Gdx.graphics.getHeight();
        scale = 1.3f * Math.max(1f, generatedHeight / designHeight);
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/000webfont.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("fightInfoTable", generator.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.RED;
            fonts.put("fightInfoTableTitle", generator.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("fightInfoTableContent", generator.generateFont(parameter));

            parameter.size = (int) (11 * scale);
            parameter.borderWidth = 1f;
            parameter.color = Color.WHITE;
            fonts.put("fightInfoTableDescription", generator.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.RED;
            fonts.put("inventoryInfoTableTitle", generator.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("inventoryInfoTableContent", generator.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1f;
            parameter.color = Color.WHITE;
            fonts.put("inventoryInfoTableDescription", generator.generateFont(parameter));

            parameter.size = (int) (20 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("lootDescription", generator.generateFont(parameter));

            parameter.size = (int) (20 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("pp", generator.generateFont(parameter));

            parameter.size = (int) (20 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("damage", generator.generateFont(parameter));

            parameter.size = (int) (10 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("inventoryName", generator.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.RED;
            fonts.put("worldDialogTitle", generator.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("worldDialog", generator.generateFont(parameter));

            generator.dispose();
        } catch (RuntimeException e) {e.printStackTrace();}
    }

    public static Label.LabelStyle get(String name) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonts.get(name);
        return labelStyle;
    }
}
