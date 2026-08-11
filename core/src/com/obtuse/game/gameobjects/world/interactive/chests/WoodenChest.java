package com.obtuse.game.gameobjects.world.interactive.chests;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.bodies.boxes.Box;
import com.obtuse.game.gameobjects.world.interactive.Chest;

public class WoodenChest extends Chest {

    public WoodenChest(float x, float y) {
        super("woodenChest", 1f,0.2f,1f, x, y, 1, 11f/16);
        setBody(new Box(x, y, BodyDef.BodyType.StaticBody, getWidth(), 6f / 16));
    }

    @Override
    public boolean interact() {
        if (!opened)
            SoundPlayer.play("world/misc/woodenChestOpening");
        return super.interact();
    }

}


