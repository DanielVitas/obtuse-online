package com.obtuse.game.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.obtuse.game.Obtuse;

public abstract class Effect extends ParticleEffect {
    protected boolean showBoolean = true;
    private float x, y;

    public Effect(String path) {
        super();
        this.load(Gdx.files.internal(path), Obtuse.textureAtlas);
        start();
    }

    public void start() {
        this.start();
    }

    public void show(boolean showBoolean) {
        this.showBoolean = showBoolean;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.x = x;
        this.y = y;
    }

    public void changePosition(float deltaX, float deltaY) {
        setPosition(x + deltaX, y + deltaY);
    }

}
