package com.obtuse.game.gameworld;

import com.badlogic.gdx.physics.box2d.*;
import com.obtuse.game.bodies.GameBody;
import com.obtuse.game.gameobjects.world.MainCharacter;
import com.obtuse.game.gameobjects.world.WorldObject;

public class WorldContactListener implements ContactListener {

    public WorldContactListener() {

    }

    private Fixture getContactObjectClass(Contact contact, Class wantedClass) {
        try {
            if (((GameBody) contact.getFixtureA().getBody().getUserData()).getUserData().getClass() == wantedClass)
                return contact.getFixtureA();
        } catch (NullPointerException e) {e.printStackTrace();}
        try {
            if (((GameBody) contact.getFixtureB().getBody().getUserData()).getUserData().getClass() == wantedClass)
                return contact.getFixtureB();
        } catch (NullPointerException e) {e.printStackTrace();}
        return null;
    }

    private Fixture[] getContactObjectSuperClass(Contact contact, Class wantedClass) {
        try {
            if (((GameBody) contact.getFixtureA().getBody().getUserData()).getUserData().getClass().getSuperclass()
                    == wantedClass)
                return new Fixture[]{contact.getFixtureA(), contact.getFixtureB()};
        } catch (NullPointerException e) {e.printStackTrace();}
        try {
            if (((GameBody) contact.getFixtureB().getBody().getUserData()).getUserData().getClass().getSuperclass()
                    == wantedClass)
                return new Fixture[]{contact.getFixtureB(), contact.getFixtureA()};
        } catch (NullPointerException e) {e.printStackTrace();}
        return new Fixture[]{null, null};
    }

    private void addMainSensor(Contact contact) {
        Fixture[] fixtures = getContactObjectSuperClass(contact, MainCharacter.class);
        Fixture mainSensorFixture = fixtures[0];
        Fixture otherFixture = fixtures[1];
        if (mainSensorFixture != null)
            //if (mainSensorFixture.isSensor())
            if (((GameBody) otherFixture.getBody().getUserData()).getUserData() instanceof WorldObject)
                if (!(((GameBody) otherFixture.getBody().getUserData()).getUserData() instanceof MainCharacter)) {
                    MainCharacter mc = (MainCharacter)
                            ((GameBody) mainSensorFixture.getBody().getUserData()).getUserData();
                    mc.beginContact((WorldObject) ((GameBody) otherFixture.getBody().getUserData()).getUserData());
                }
    }

    private void removeMainSensor(Contact contact) {
        Fixture[] fixtures = getContactObjectSuperClass(contact, MainCharacter.class);
        Fixture mainSensorFixture = fixtures[0];
        Fixture otherFixture = fixtures[1];
        if (mainSensorFixture != null) {
            MainCharacter mc = (MainCharacter) mainSensorFixture.getBody().getUserData();
            mc.endContact((WorldObject) ((GameBody) otherFixture.getBody().getUserData()).getUserData());
        }
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (((GameBody) contact.getFixtureA().getBody().getUserData()).isSensor ||
                ((GameBody) contact.getFixtureB().getBody().getUserData()).isSensor) {
            addMainSensor(contact);
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
