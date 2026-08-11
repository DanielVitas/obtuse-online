package com.obtuse.game.gameobjects.fight.heroes;

import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.items.abilityorbs.*;
import com.obtuse.game.gameobjects.items.equipment.DivineOrb;
import com.obtuse.game.gameobjects.items.equipment.Shield;

public class MageKnight extends Hero {

    public MageKnight() {
        super("mageKnight",0.1f,0.1f,0.08f,0.1f,0.1f,1f,
                1f, 0.1f,0.1f,0.1f, "fight/objects/mageKnight/hurt",
                "fight/objects/mageKnight/death", "fight/objects/healing");
        setName("Mage Knight");
        setSize(2f,2f);
        /*BloodSacrificeOrb bs = new BloodSacrificeOrb();
        ((BloodSacrifice) bs.ability).damage = 3;
        abilityOrbs.add(bs);*/
        abilityOrbs.add(new PoisonOrb());
        //equipment.add(new DivineOrb(), new Shield());
        originalHP = 4;
        originalSpeed = 1;
    }

    @Override
    public float getClickHeight() {
        return super.getClickHeight() / 2;
    }
}
