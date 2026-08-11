package com.obtuse.game.bodies.boxes;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class SensorBox extends Box {

    public SensorBox(float x, float y, BodyDef.BodyType bodyType, float width, float height) {
        super(x, y, bodyType, width, height);
        this.isSensor = true;
        this.body.setSleepingAllowed(false);
    }

    @Override
    protected FixtureDef fixtureDef() {
        FixtureDef fd = super.fixtureDef();
        //fd.filter.categoryBits = -0x001;
        //fd.filter.maskBits = -0x001;
        fd.filter.groupIndex = -0x001;
        return fd;
    }

}
