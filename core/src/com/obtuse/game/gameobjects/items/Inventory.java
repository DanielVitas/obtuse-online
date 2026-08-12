package com.obtuse.game.gameobjects.items;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.gameobjects.items.abilityorbs.*;
import com.obtuse.game.gameobjects.items.equipment.*;

public abstract class Inventory {
    public static Array<Item> items = new Array<Item>();
    public static int maxSize = 12;

    public Inventory() {

    }

    public static void add(Item item) {
        items.add(item);
    }

    /** Start the player off with one of every ability orb and every piece of equipment. */
    public static void giveAllItems() {
        items.clear();
        // Ability orbs.
        items.add(new FireBallOrb());
        items.add(new PoisonOrb());
        items.add(new DelayedHitOrb());
        items.add(new BloodSacrificeOrb());
        items.add(new BurningGroundOrb());
        items.add(new ThunderstrikeOrb());
        items.add(new DummyOrb());
        items.add(new GuardOrb());
        items.add(new SwapOrb());
        items.add(new ReverseOrb());
        items.add(new DuelOrb());
        items.add(new SpeedSwitchOrb());
        items.add(new TabulaRasaOrb());
        items.add(new ShieldBashOrb());
        items.add(new EchoOrb());
        items.add(new StealOrb());
        items.add(new FlamingSwordOrb());
        // Equipment.
        items.add(new GlovesOfDamage());
        items.add(new HealingStaff());
        items.add(new Shield());
        items.add(new DivineOrb());
        items.add(new BloodChalice());
        items.add(new BootsOfSpeed());
        items.add(new Seashell());
        items.add(new SummoningHorn());
        items.add(new CrownOfThorns());
    }
}
