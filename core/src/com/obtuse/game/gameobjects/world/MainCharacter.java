package com.obtuse.game.gameobjects.world;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;
import com.obtuse.game.bodies.boxes.SensorBox;

public abstract class MainCharacter extends WorldCharacter {
    private static float sensorSize = 0.25f;
    public Array<WorldObject> sensoredObjects = new Array<WorldObject>();

    public MainCharacter(String name, float walkFrameDuration, float x, float y, float width, float height) {
        super(name, walkFrameDuration, x, y, width, height);
        addBody(new SensorBox(getX(), getY(), BodyDef.BodyType.StaticBody, 1, 1));
        makeSensorBox(direction);
    }

    public void beginContact(WorldObject object) {
        sensoredObjects.add(object);
    }

    public void endContact(WorldObject object) {
        sensoredObjects.removeValue(object, false);
    }

    @Override
    protected void makeSensorBox(int direction) {
        switch (direction) {
            case 0:
                setAdditionalBody(new SensorBox(getX(), getY() - sensorSize, BodyDef.BodyType.DynamicBody,
                        Obtuse.pixels, sensorSize), 0);
                break;
            case 1:
                setAdditionalBody(new SensorBox(getX() + getWidth() - 4f / 16, getY(), BodyDef.BodyType.DynamicBody,
                        sensorSize + 4f / 16, Obtuse.pixels), 0);
                break;
            case 2:
                setAdditionalBody(new SensorBox(getX() - sensorSize, getY(), BodyDef.BodyType.DynamicBody,
                        sensorSize + 4f / 16, Obtuse.pixels), 0);
                break;
            case 3:
                setAdditionalBody(new SensorBox(getX(), getY() + getHeight() - 12f / 16, BodyDef.BodyType.DynamicBody,
                        Obtuse.pixels, sensorSize + 12f / 16), 0);
                break;
        }
    }
}
