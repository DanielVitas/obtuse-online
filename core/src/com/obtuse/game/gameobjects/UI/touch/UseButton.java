package com.obtuse.game.gameobjects.UI.touch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.obtuse.game.buttons.GameButton;

/**
 * The big round "do the thing in front of me" control: a silver disc with a smaller,
 * darker disc inside it. The inner disc carries an icon of whatever pressing it would
 * actually do right now - open a chest, open a door, start a fight, talk to someone -
 * and shows nothing when there is nothing in reach.
 */
public class UseButton extends GameButton {
    private final float centreX;
    private final float centreY;
    private final float radius;
    private final Image icon;
    private String shown = "unset";

    public UseButton(Stage stage, float centreX, float centreY, float radius) {
        this.centreX = centreX;
        this.centreY = centreY;
        this.radius = radius;
        disc(stage, radius, new Color(0.80f, 0.83f, 0.88f, 0.55f));
        disc(stage, radius * 0.62f, new Color(0.34f, 0.37f, 0.43f, 0.75f));
        icon = new Image();
        icon.setVisible(false);
        stage.addActor(icon);
    }

    private void disc(Stage stage, float r, Color color) {
        Image image = new Image(new TextureRegion(TouchIcons.disc()));
        image.setBounds(centreX - r, centreY - r, r * 2, r * 2);
        image.setColor(color);
        stage.addActor(image);
    }

    /** key is one of the TouchIcons constants, or null when nothing is in reach. */
    public void setIcon(String key) {
        if (key == null ? shown == null : key.equals(shown))
            return;
        shown = key;
        TextureRegion region = TouchIcons.get(key);
        if (region == null) {
            icon.setVisible(false);
            return;
        }
        // fit the art inside the inner disc without distorting it
        float box = radius * 0.85f;
        float scale = Math.min(box / region.getRegionWidth(), box / region.getRegionHeight());
        float w = region.getRegionWidth() * scale;
        float h = region.getRegionHeight() * scale;
        icon.setDrawable(new TextureRegionDrawable(region));
        icon.setBounds(centreX - w / 2, centreY - h / 2, w, h);
        icon.setVisible(true);
    }

    @Override
    public boolean check(float x, float y) {
        float dx = x - centreX;
        float dy = y - centreY;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public float getX() {
        return centreX - radius;
    }

    @Override
    public float getY() {
        return centreY - radius;
    }

    @Override
    public float getWidth() {
        return radius * 2;
    }

    @Override
    public float getHeight() {
        return radius * 2;
    }
}
