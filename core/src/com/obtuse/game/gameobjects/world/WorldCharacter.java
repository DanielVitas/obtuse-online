package com.obtuse.game.gameobjects.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.animations.FloatContainer;
import com.obtuse.game.animations.GameAnimation;
import com.obtuse.game.bodies.boxes.Box;

import static java.lang.Math.abs;

public abstract class WorldCharacter extends WorldObject {
    protected FloatContainer stepCounter = new FloatContainer(0);
    private static float pixelToSeconds = 0.2f;
    protected float walkSpeed = 3;
    protected int direction;
    protected String name;
    protected String[] walkAnimationNames = {"walkDown", "walkRight", "walkLeft", "walkUp"};
    private float walkFD = 0;

    public WorldCharacter(String name, float walkFD, float x, float y, float width, float height) {
        super();
        path += "characters/" + name + "/";
        this.name = name;
        this.walkFD = walkFD;
        setWidth(width);
        setHeight(height);
        setBody(new Box(x, y, BodyDef.BodyType.DynamicBody, width, height));
        body.body.setFixedRotation(true);
        for (String animationName : walkAnimationNames)
            animations.put(animationName, new GameAnimation(new Animation<TextureRegion>(walkFD,
                    Obtuse.textureAtlas.findRegions(path + animationName + "/main"),
                    Animation.PlayMode.LOOP), stepCounter, 0, 0));
        currentlyDisplayed.add(animations.get(walkAnimationNames[0]));
    }

    protected abstract void makeSensorBox(int direction);

    protected void passiveWalk() {
        play(walkAnimationNames[direction], 0);
    }

    protected float playWalk(String animationName, int index) {
        AnimationDrawable animationDrawable = animations.get(animationName);
        currentlyDisplayed.insert(index, animationDrawable);
        currentlyDisplayed.removeIndex(index + 1);
        return animations.get(animationName).duration();
    }

    public void changeDirection(Vector2 v) {
        float vx = v.x;
        float vy = v.y;
        int n;
        if (vx != 0 || vy != 0) {
            if (abs(vx) > abs(vy))
                if (vx >= 0)
                    n = 1;
                else
                    n = 2;
            else if (vy <= 0)
                n = 0;
            else
                n = 3;
            if (direction != n) {
                direction = n;
                makeSensorBox(n);
                passiveWalk();
            }
        }
    }

    public void walk(Vector2 normalizedVector) { // 0 - down, 1 - right, 2 - left, 3 - up
        body.body.setLinearVelocity(normalizedVector.scl(walkSpeed)/*, body.body.getWorldCenter(), true*/);
    }

    @Override
    protected void changeImagePosition(float deltaX, float deltaY) {
        super.changeImagePosition(deltaX, deltaY);
        stepCounter.add(Math.max(abs(deltaX), abs(deltaY)) * pixelToSeconds);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
