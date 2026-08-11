package com.obtuse.game.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public class GameAnimation extends AnimationDrawable {

    public GameAnimation(Animation animation, FloatContainer counter, float additionalX, float additionalY) {
        super(animation, 0, 0, additionalX, additionalY);
        stateTime = counter;
    }

    public void update(float f) {
        stateTime = new FloatContainer(f);
    }

    @Override
    public void act(float delta) {

    }
}
