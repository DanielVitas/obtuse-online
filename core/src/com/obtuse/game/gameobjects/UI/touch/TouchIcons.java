package com.obtuse.game.gameobjects.UI.touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;

/**
 * Art for the on screen controls. Most of it already exists in the atlas - the chest,
 * the door and the flaming sword are the same sprites the game draws in the world, so
 * the button always shows the thing you are about to touch. The bag and the speech
 * bubble had no equivalent, so they ship as loose PNGs under assets/touch/.
 */
public class TouchIcons {
    public static final String CHEST = "chest";
    public static final String DOOR = "door";
    public static final String FIGHT = "fight";
    public static final String TALK = "talk";

    private static Texture disc;
    private static TextureRegion bag;
    private static TextureRegion talk;

    /**
     * A white anti-aliased circle, generated rather than drawn by hand so it stays smooth
     * at any size. Tint it to colour it: the Use button uses one silver and one darker
     * copy of this single texture.
     */
    public static Texture disc() {
        if (disc == null) {
            int size = 256;
            float r = size / 2f - 1f;
            Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++) {
                    float dx = x - size / 2f + 0.5f;
                    float dy = y - size / 2f + 0.5f;
                    float d = (float) Math.sqrt(dx * dx + dy * dy);
                    // one pixel of feathering at the rim is all it takes to kill the jaggies
                    float a = MathUtils.clamp(r - d + 0.5f, 0f, 1f);
                    pixmap.drawPixel(x, y, Color.rgba8888(1f, 1f, 1f, a));
                }
            disc = new Texture(pixmap);
            disc.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            pixmap.dispose();
        }
        return disc;
    }

    public static TextureRegion bag() {
        if (bag == null)
            bag = load("touch/bag.png");
        return bag;
    }

    private static TextureRegion load(String path) {
        try {
            return new TextureRegion(new Texture(Gdx.files.internal(path)));
        } catch (RuntimeException e) {
            Gdx.app.error("OBTUSE", "missing touch icon " + path, e);
            return null;
        }
    }

    /** The icon for a given interaction, or null if there is nothing to interact with. */
    public static TextureRegion get(String key) {
        if (key == null)
            return null;
        if (CHEST.equals(key))
            return Obtuse.textureAtlas.findRegion("world/interactive/chests/woodenChest/default/main");
        if (DOOR.equals(key)) {
            // last frame of the opening animation, i.e. the door at its most open
            Array<TextureAtlas.AtlasRegion> frames =
                    Obtuse.textureAtlas.findRegions("world/interactive/house/door/up/opening/main");
            return frames.size == 0 ? null : frames.get(frames.size - 1);
        }
        if (FIGHT.equals(key))
            return Obtuse.textureAtlas.findRegion("fight/summons/flamingSword/default/main");
        if (TALK.equals(key)) {
            if (talk == null)
                talk = load("touch/speech.png");
            return talk;
        }
        return null;
    }
}
