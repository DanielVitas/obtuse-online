package com.obtuse.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.effects.Effect;

import java.util.HashMap;
import java.util.Map;

public abstract class BasicObject extends DepthObject {
    protected Array<Effect> effects = new Array<Effect>();
    protected Map<String, AnimationDrawable> animations = new HashMap<String, AnimationDrawable>();
    public Array<AnimationDrawable> currentlyDisplayed = new Array<AnimationDrawable>();
    protected String path = "";


    protected  void addAnimation(String animationName, float frameDuration, Animation.PlayMode playMode) {
        addAnimation(animationName, frameDuration, playMode, 0, 0,0,0);
    }

    protected void addAnimation(String animationName, float frameDuration, Animation.PlayMode playMode, float width,
                                float height, float additionalX, float additionalY) {
        animations.put(animationName, new AnimationDrawable(new Animation<TextureRegion>(frameDuration,
                Obtuse.textureAtlas.findRegions(path + animationName + "/main"), playMode),
                width, height, additionalX, additionalY));
    }

    public float play(String animationName, int index) {
        AnimationDrawable animationDrawable = animations.get(animationName);
        animationDrawable.reset();
        currentlyDisplayed.insert(index, animationDrawable);
        currentlyDisplayed.removeIndex(index + 1);
        return animations.get(animationName).duration();
    }

    public float playNoReset(String animationName, int index) {
        AnimationDrawable animationDrawable = animations.get(animationName);
        currentlyDisplayed.insert(index, animationDrawable);
        currentlyDisplayed.removeIndex(index + 1);
        return animations.get(animationName).duration();
    }

    public void play(final String animationName, final int index, float delay) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                play(animationName, index);
            }
        }, delay);
    }

    public void setImagePosition(float x, float y) {
        changeImagePosition(getX() - x, getY() - y);
    }

    protected void changeImagePosition(float deltaX, float deltaY) {
        super.setPosition(getX() + deltaX, getY() + deltaY);
        for (Effect effect : effects)
            effect.changePosition(deltaX, deltaY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (AnimationDrawable animationDrawable : currentlyDisplayed)
            animationDrawable.draw(batch, getX(), getY(), getWidth(), getHeight());
        for (Effect effect : effects)
            effect.draw(batch);
    }

    @Override
    public void act(float delta) {
        for (AnimationDrawable animation : currentlyDisplayed)
            animation.act(delta);
        super.act(delta);
        for (Effect effect : effects)
            effect.update(delta);
    }

    @Override
    public float getZ() {
        return getY();
    }
}
