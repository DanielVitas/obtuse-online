package com.obtuse.game.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class AnimationDrawable extends BaseDrawable {
    private Animation animation;
    protected FloatContainer stateTime = new FloatContainer(0);
    public float additionalX, additionalY;
    public float width, height;

    public AnimationDrawable(Animation animation, float width, float height, float additionalX, float additionalY) {
        this.animation = animation;
        this.width = width;
        this.height = height;
        this.additionalX = additionalX;
        this.additionalY = additionalY;
    }

    public AnimationDrawable clone() {
        return new AnimationDrawable(animation, width, height, additionalX, additionalY);
    }

    public float duration() {
        return animation.getAnimationDuration();
    }

    public void reset() {
        stateTime.set(0);
    }

    public void act(float delta) {
        stateTime.add(delta);
        if (stateTime.isMoreThan(animation.getAnimationDuration()))
            if (animation.getPlayMode() == Animation.PlayMode.LOOP)
                stateTime.add(-animation.getAnimationDuration());
            else
                stateTime.add(-delta);
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        float w, h;
        if (this.width == 0)
            w = width;
        else
            w = this.width;
        if (this.height == 0)
            h = height;
        else
            h = this.height;
        try {
            TextureRegion textureRegion = (TextureRegion) animation.getKeyFrame(stateTime.get());
            if (textureRegion != null)
                batch.draw(textureRegion, x + additionalX, y + additionalY, w, h);
        } catch (ArrayIndexOutOfBoundsException e) {e.printStackTrace();}
    }

}
