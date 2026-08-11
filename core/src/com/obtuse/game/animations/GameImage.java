package com.obtuse.game.animations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameImage extends Image {
    public float additionalX, additionalY;

    public GameImage(TextureRegion textureRegion) {
        super(textureRegion);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
