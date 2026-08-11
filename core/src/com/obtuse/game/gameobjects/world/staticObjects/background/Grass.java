package com.obtuse.game.gameobjects.world.staticObjects.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.obtuse.game.gameobjects.world.staticObjects.Background;

import static com.obtuse.game.Obtuse.textureAtlas;

public class Grass extends Background {

    public Grass(float x, float y, float width, float height) {
        super();
        path += "grass/";
        setSize(width, height);
        setPosition(x, y);

        Texture texture = new Texture(Gdx.files.internal("images/" + path + "grass.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texture);
        float c = width * 16f / 64;
        float d = height * 16f / 64;
        textureRegion.setRegion(0, 0, (int) (texture.getWidth() * c) + 1,
                (int) (texture.getHeight() * d) + 1);
        Image image = new Image(textureRegion);
        image.setSize(width, height);
        add(image, 0, 0);
    }
}
