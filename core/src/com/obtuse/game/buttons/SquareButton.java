package com.obtuse.game.buttons;


import com.badlogic.gdx.math.Rectangle;

public class SquareButton extends GameButton {
    private Rectangle rectangle;

    public SquareButton(float x, float y, float width, float height) {
        super();
        rectangle = new Rectangle(x, y, width, height);
    }

    @Override
    public boolean check(float x, float y) {
        return rectangle.contains(x, y);
    }

    @Override
    public float getX() {
        return rectangle.getX();
    }

    @Override
    public float getY() {
        return rectangle.getY();
    }

    @Override
    public float getWidth() {
        return rectangle.getWidth();
    }

    @Override
    public float getHeight() {
        return rectangle.getHeight();
    }

}
