package com.obtuse.game.gameobjects.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.bodies.GameBody;
import com.obtuse.game.gameobjects.BasicObject;
import com.obtuse.game.gameworld.GameWorld;

public abstract class WorldObject extends BasicObject {
    public GameBody body;
    public Array<GameBody> additionalBodies = new Array<GameBody>();

    public WorldObject() {
        path += "world/";
    }

    public boolean interact() {
        return false;
    }

    /**
     * Which icon the on screen Use button should show while the player is standing next
     * to this object, or null if touching it would do nothing. See TouchIcons.
     */
    public String actionIcon() {
        return null;
    }

    public void setAdditionalBody(GameBody body, int index) {
        additionalBodies.insert(index, body);
        GameWorld.world.destroyBody(additionalBodies.get(index + 1).body);
        additionalBodies.removeIndex(index + 1);
        body.setUserData(this);
    }

    public void setBody(GameBody body) {
        if (this.body != null)
            GameWorld.world.destroyBody(this.body.body);
        this.body = body;
        body.setUserData(this);
    }

    public void addBody(GameBody body) {
        additionalBodies.add(body);
        body.setUserData(this);
    }

    @Override
    public boolean remove() {
        if (this.body != null)
            GameWorld.world.destroyBody(this.body.body);
        for (int i = 0; i < this.additionalBodies.size; i++)
            GameWorld.world.destroyBody(this.additionalBodies.get(i).body);
        return super.remove();
    }

    @Override
    public void setRotation(float radians) {
        float degrees = radians * MathUtils.radiansToDegrees;
        super.setRotation(degrees);
    }

    @Override
    protected void changeImagePosition(float deltaX, float deltaY) {
        super.changeImagePosition(deltaX, deltaY);
        for (GameBody gameBody : additionalBodies)
            gameBody.body.setTransform(gameBody.getPosition().add(new Vector2(deltaX, deltaY)), 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (body != null) {
            if (body.body != null)
                setRotation(body.body.getAngle());
            changeImagePosition(body.getPosition().x - getX(), body.getPosition().y - getY());
        }
    }
}
