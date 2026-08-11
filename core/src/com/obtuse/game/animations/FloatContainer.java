package com.obtuse.game.animations;

public class FloatContainer {
    private float f = 0;

    public FloatContainer(float f) {
        this.f = f;
    }

    public void set(float f) {
        this.f = f;
    }

    public float get() {
        return f;
    }

    public void add(float f) {
        this.f += f;
    }

    public boolean isMoreThan(float f) {
        return this.f > f;
    }
}
