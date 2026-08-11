package com.obtuse.game.buttons;

public abstract class GameButton {

    public  GameButton() {

    }

    public abstract boolean check(float x, float y);
    public abstract float getX();
    public abstract float getY();
    public abstract float getWidth();
    public abstract float getHeight();

}
