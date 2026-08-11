package com.obtuse.game.gameobjects.fight.heroes;

import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.items.abilityorbs.*;

public class Wizard extends Hero {

    public Wizard() {
        super("wizard",0.2f, 0.2f,0.2f,0.2f,0.1f,1f,
                0.2f,0.2f,0.2f,0.2f, "fight/objects/wizard/hurt",
                "fight/objects/wizard/death", "fight/objects/healing");
        setName("Wizard");
        setSize(1f,1f);
        //abilityOrbs.add(new EchoOrb(), new DecoyOrb(), new SpeedSwitchOrb(), new BurningGroundOrb());
        originalHP = 3;
    }
}
