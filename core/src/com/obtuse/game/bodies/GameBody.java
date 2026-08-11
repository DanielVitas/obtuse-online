package com.obtuse.game.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.obtuse.game.gameworld.GameWorld;

public abstract class GameBody {
    public Body body;
    protected float width, height;
    protected Vector2 toAnchor;
    private Object userData;
    public boolean isSensor = false;
    protected float additionalX = 0;
    protected float additionalY = 0;

    public GameBody(float x, float y, BodyDef.BodyType bodyType) {
        construct(x, y, bodyType);
    }

    public GameBody(float x, float y, BodyDef.BodyType bodyType, float width, float height) {
        this.width = width;
        this.height = height;
        construct(x, y, bodyType);
        body.setUserData(this);
    }

    public GameBody(float x, float y, BodyDef.BodyType bodyType, float width, float height, float additionalX, float additionalY) {
        this.width = width;
        this.height = height;
        this.additionalX = additionalX;
        this.additionalY = additionalY;
        construct(x, y, bodyType);
        body.setUserData(this);
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public Object getUserData() {
        return userData;
    }

    private void construct(float x, float y, BodyDef.BodyType bodyType) {
        BodyDef bd = bodyDef();
        bd.type = bodyType;
        bd.position.set(x, y);

        body = GameWorld.createBody(bd);

        Shape s = shape();
        FixtureDef fd = fixtureDef();
        fd.shape = s;

        body.createFixture(fd);

        s.dispose();

        Vector2 v = new Vector2(x, y);
        toAnchor = v.sub(body.getPosition());
    }

    public Vector2 getPosition() {
        return body.getPosition().add(toAnchor);
    }

    protected abstract BodyDef bodyDef();
    protected abstract Shape shape();
    protected abstract FixtureDef fixtureDef();
}
