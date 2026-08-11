package com.obtuse.game.gameobjects.world.interactive.house;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.bodies.boxes.Box;
import com.obtuse.game.gameobjects.UI.touch.TouchIcons;
import com.obtuse.game.gameobjects.world.WorldInteractive;
import com.obtuse.game.gameobjects.world.WorldObject;
import com.obtuse.game.gameobjects.world.WorldObstacle;

public class Door extends WorldInteractive {
    private boolean opened = false;

    public Door(float x, float y) {
        super("house/door/up",1f, x, y,1f,1f);
        addAnimation("opening", 0.2f, Animation.PlayMode.NORMAL);
    }

    @Override
    public boolean interact() {
        if (!opened) {
            SoundPlayer.play("world/misc/woodenDoorOpening");
            play("opening", 0);
            setBody(new Box(getX(), getY(), BodyDef.BodyType.StaticBody, 2f / 16, getHeight()));
            opened = true;
        }
        return true;
    }

    @Override
    public String actionIcon() {
        return opened ? null : TouchIcons.DOOR;
    }
}
