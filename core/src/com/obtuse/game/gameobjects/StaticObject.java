package com.obtuse.game.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.bodies.GameBody;
import com.obtuse.game.effects.Effect;
import com.obtuse.game.gameworld.GameWorld;

import static com.obtuse.game.Obtuse.textureAtlas;

public abstract class StaticObject extends DepthObject {
    public GameBody body;
    protected Array<Effect> effects = new Array<Effect>();
    protected Array<Image> images = new Array<Image>();
    protected String path = "";

    public StaticObject() {

    }

    protected TextureRegion getTextureRegion(String name) {
        return textureAtlas.findRegion(path + name);
    }

    protected void add(String name, float additionalX, float additionalY) {
        Image image = new Image(getTextureRegion(name));
        add(image, additionalX, additionalY);
    }

    protected void add(Image image, float additionalX, float additionalY) {
        image.setPosition(getX() + additionalX, getY() + additionalY);
        images.add(image);
    }

    public void setBody(GameBody body) {
        if (this.body != null)
            GameWorld.world.destroyBody(this.body.body);
        this.body = body;
        body.setUserData(this);
    }

    @Override
    public void setPosition(float x, float y) {
        for (Image image : images)
            image.setPosition(image.getX() - getX() + x,image.getY() - getY() + y);
        for (Effect effect : effects)
            effect.changePosition(getX() - x, getY() - y);
        super.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (Image image : images)
            image.draw(batch, parentAlpha);
        for (Effect effect : effects)
            effect.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (Effect effect : effects)
            effect.update(delta);
    }
}
