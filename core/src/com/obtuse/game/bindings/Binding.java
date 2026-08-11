package com.obtuse.game.bindings;

import com.obtuse.game.buttons.GameButton;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Binding {
    public Action action;
    public int key;
    public GameButton button;

    public Binding(Action actions, int key, GameButton gameButton) {
        this.action = action;
        this.key = key;
        this.button = gameButton;
    }

    public Binding(Object object, Method method) {
        this.action = new Action(object, method);
    }

    public Binding(Object object, Method method, int key) {
        this(object, method);
        this.key = key;
    }

    public Binding(Object object, Method method, GameButton gameButton) {
        this(object, method);
        this.button = gameButton;
    }

    public void call() {
        action.call();
    }
}
