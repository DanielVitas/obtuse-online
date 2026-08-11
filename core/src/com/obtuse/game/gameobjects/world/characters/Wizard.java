package com.obtuse.game.gameobjects.world.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.Obtuse;
import com.obtuse.game.bodies.boxes.Box;
import com.obtuse.game.gameobjects.world.MainCharacter;

public class Wizard extends MainCharacter {
    protected String[] torchAnimationNames = {"torchDown", "torchRight", "torchLeft", "torchUp"};

    public Wizard(float x, float y) {
        super("wizard",0.1f, x, y, Obtuse.pixels, Obtuse.pixels);
        setBody(new Box(getX(), getY(), BodyDef.BodyType.DynamicBody,getWidth() - 8f / 16, getHeight() - 12f / 16, 4f / 16,0));
        body.body.setFixedRotation(true);
        addAnimation(torchAnimationNames[0],0.2f, Animation.PlayMode.LOOP, 5f / 16, 15f / 16, 9f /16, 0);
        addAnimation(torchAnimationNames[1],0.2f, Animation.PlayMode.LOOP, 5f / 16, 15f / 16, 11f /16, 0);
        addAnimation(torchAnimationNames[2],0.2f, Animation.PlayMode.LOOP, 5f / 16, 15f / 16, 0f /16, 0);
        addAnimation(torchAnimationNames[3],0.2f, Animation.PlayMode.LOOP, 5f / 16, 15f / 16, 2f /16, 0);
        currentlyDisplayed.add(animations.get(torchAnimationNames[0]));
    }

    @Override
    protected void passiveWalk() {
        if (direction == 3) {
            play(walkAnimationNames[direction],1);
            playNoReset(torchAnimationNames[direction], 0);
        } else {
            play(walkAnimationNames[direction],0);
            playNoReset(torchAnimationNames[direction], 1);
        }
    }
}
