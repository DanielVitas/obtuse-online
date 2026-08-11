package com.obtuse.game.bodies.boxes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.obtuse.game.bodies.GameBody;

public class Box extends GameBody {

    public Box(float x, float y, BodyDef.BodyType bodyType, float width, float height) {
        super(x, y, bodyType, width, height);
    }

    public Box(float x, float y, BodyDef.BodyType bodyType, float width, float height, float additionalX, float additionalY) {
        super(x, y, bodyType, width, height, additionalX, additionalY);
    }

    @Override
    protected BodyDef bodyDef() {
        return new BodyDef();
    }

    @Override
    protected Shape shape() {
        PolygonShape s = new PolygonShape();
        //s.setAsBox(width / 2, height / 2);
        s.set(new Vector2[]{new Vector2(additionalX,additionalY), new Vector2(additionalX, additionalY + height),
                new Vector2(additionalX + width, additionalY + height), new Vector2(additionalX + width,additionalY)});
        return s;
    }

    @Override
    protected FixtureDef fixtureDef() {
        FixtureDef fd = new FixtureDef();
        fd.density = 5f;
        fd.friction = 0f;
        //fd.filter.categoryBits = 0x001;
        //fd.filter.maskBits = 0x001;
        return fd;
    }
}
