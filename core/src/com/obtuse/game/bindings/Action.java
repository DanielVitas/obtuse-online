package com.obtuse.game.bindings;

import java.lang.reflect.Method;

public class Action {
    private Object object;
    private Method method;

    public Action(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public void call() {
        try {
            method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
