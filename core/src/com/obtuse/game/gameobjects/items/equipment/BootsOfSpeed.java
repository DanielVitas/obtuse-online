package com.obtuse.game.gameobjects.items.equipment;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.items.Equipment;

public class BootsOfSpeed extends Equipment {
    public int speed = 2;

    public BootsOfSpeed() {
        super("bootsOfSpeed", 1f);
        setName("Boots of Speed");
        description = "Adds " + Integer.toString(speed) + " speed.";
    }

    @Override
    public void setup(FightObject fightObject) {
        fightObject.speed += speed;
    }
}
