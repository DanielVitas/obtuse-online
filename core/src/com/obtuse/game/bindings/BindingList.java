package com.obtuse.game.bindings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class BindingList {
    private Map<String, Array<Binding>> bindings = new HashMap<String, Array<Binding>>();
    public static Map<String, Integer> keys = new HashMap<String, Integer>();

    public BindingList() {
        keys.put("down", Input.Keys.S);
        keys.put("right", Input.Keys.D);
        keys.put("left", Input.Keys.A);
        keys.put("up", Input.Keys.W);
        keys.put("interact", Input.Keys.SPACE);
        keys.put("inventory", Input.Keys.I);
    }

    public void keyBindings(String filter) {
        for (Binding binding : bindings.get(filter))
            if (Gdx.input.isKeyPressed(binding.key))
                binding.call();
    }


    public void buttonBindings(String filter, float x, float y) {
        for (Binding binding : bindings.get(filter))
            if (binding.button.check(x, y))
                binding.call();
    }

    public void add(String filter, Binding binding) {
        if (!bindings.containsKey(filter))
            bindings.put(filter, new Array<Binding>());
        bindings.get(filter).add(binding);
    }
}
