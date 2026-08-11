package com.obtuse.game.gameobjects.items;

import com.badlogic.gdx.utils.Array;

public class Choice {
    public Array<Item> items = new Array<Item>();

    public Choice() {

    }

    public void add(Item item) {
        items.add(item);
    }

    public void add(Item item1, Item item2) {
        items.add(item1, item2);
    }

    public void add(Item item1, Item item2, Item item3) {
        items.add(item1, item2, item3);
    }
}
