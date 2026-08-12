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
        // Every size below is a fraction of how tall the surface is relative to the 2018 design
        // window (520px). Scale LINEARLY with the surface — no floor at 1 — so the text is the same
        // proportion of the screen everywhere; the old max(1, ...) kept full 520-px sizes on shorter
        // surfaces, which is why the text looked too big on smaller screens.
        scale = generatedHeight / designHeight;
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/000webfont.ttf"));
            // The royal-gold "box" UI (tooltips, move/inventory/loot boxes) uses the clean monospace
            // face from the style preview (JetBrains Mono); the pixel display font stays for the
            // world dialog and floating damage numbers.
            FreeTypeFontGenerator mono = new FreeTypeFontGenerator(Gdx.files.internal("fonts/JetBrainsMono-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = (int) (13 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("fightInfoTable", mono.generateFont(parameter));

            parameter.size = (int) (13 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.RED;
            fonts.put("fightInfoTableTitle", mono.generateFont(parameter));

            parameter.size = (int) (13 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("fightInfoTableContent", mono.generateFont(parameter));
            fonts.get("fightInfoTableContent").getData().markupEnabled = true; // coloured damage in the DD

            parameter.size = (int) (11 * scale);
            parameter.borderWidth = 1f;
            parameter.color = Color.WHITE;
            fonts.put("fightInfoTableDescription", mono.generateFont(parameter));
            fonts.get("fightInfoTableDescription").getData().markupEnabled = true; // coloured damage

            parameter.size = (int) (13 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.RED;
            fonts.put("inventoryInfoTableTitle", mono.generateFont(parameter));

            parameter.size = (int) (13 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("inventoryInfoTableContent", mono.generateFont(parameter));
            fonts.get("inventoryInfoTableContent").getData().markupEnabled = true; // coloured HP/SPD

            parameter.size = (int) (13 * scale);
            parameter.borderWidth = 1f;
            parameter.color = Color.WHITE;
            fonts.put("inventoryInfoTableDescription", mono.generateFont(parameter));
            fonts.get("inventoryInfoTableDescription").getData().markupEnabled = true; // coloured damage

            parameter.size = (int) (18 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("lootDescription", mono.generateFont(parameter));

            parameter.size = (int) (9 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("pp", mono.generateFont(parameter));

            parameter.size = (int) (20 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("damage", generator.generateFont(parameter));

            parameter.size = (int) (7 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("inventoryName", mono.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.RED;
            fonts.put("worldDialogTitle", generator.generateFont(parameter));

            parameter.size = (int) (15 * scale);
            parameter.borderWidth = 1;
            parameter.color = Color.WHITE;
            fonts.put("worldDialog", generator.generateFont(parameter));

            mono.dispose();
            generator.dispose();
        } catch (RuntimeException e) {e.printStackTrace();}
    }

    public static Label.LabelStyle get(String name) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonts.get(name);
        return labelStyle;
    }
}
