package com.obtuse.game.gameobjects.items;

import com.badlogic.gdx.utils.Array;

public abstract class Inventory {
    public static Array<Item> items = new Array<Item>();
    public static int maxSize = 12;

    public Inventory() {

    }

    public static void add(Item item) {
        items.add(item);
    }
}
