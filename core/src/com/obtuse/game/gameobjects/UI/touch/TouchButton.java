package com.obtuse.game.gameobjects.UI.touch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.obtuse.game.Fonts;
import com.obtuse.game.Obtuse;
import com.obtuse.game.buttons.SquareButton;

/**
 * An on screen control for touch devices: a hit rectangle plus the actor that shows it.
 * The actor is added to the given stage, so the rectangle and what the player sees
 * can never drift apart.
 */
public class TouchButton extends SquareButton {
    private Actor actor;

    /** A button drawn with a region of the texture atlas, e.g. an arrow of the movement pad. */
    public TouchButton(Stage stage, String regionName, float x, float y, float width, float height) {
        this(stage, Obtuse.textureAtlas.findRegion(regionName), x, y, width, height);
    }

    /** A button drawn with art that did not come from the atlas, e.g. the bag icon. */
    public TouchButton(Stage stage, TextureRegion region, float x, float y, float width, float height) {
        super(x, y, width, height);
        if (region == null)
            return;
        // The arrow art is 16x8 and 8x16 pixels. Stretching that to fill a square button
        // magnifies it about 14x AND squashes it 2:1, which smears it across the screen.
        // Scale by a whole number instead so the pixels stay square and crisp, keep the
        // source aspect ratio, and centre the art inside the (larger) touch area.
        int scale = (int) Math.min(width / region.getRegionWidth(), height / region.getRegionHeight());
        if (scale < 1)
            scale = 1;
        float w = region.getRegionWidth() * scale;
        float h = region.getRegionHeight() * scale;
        Image image = new Image(region);
        image.setBounds(x + (width - w) / 2, y + (height - h) / 2, w, h);
        image.getColor().a = 0.75f;
        stage.addActor(image);
        actor = image;
    }

    /** A button drawn as a word, for actions that have no icon in the atlas. */
    public TouchButton(Stage stage, String text, String fontName, float x, float y, float width, float height) {
        super(x, y, width, height);
        Label label = new Label(text, Fonts.get(fontName));
        label.setBounds(x, y, width, height);
        label.setAlignment(Align.center);
        stage.addActor(label);
        actor = label;
    }

    public Actor getActor() {
        return actor;
    }
}
