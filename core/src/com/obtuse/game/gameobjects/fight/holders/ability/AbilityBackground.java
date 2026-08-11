package com.obtuse.game.gameobjects.fight.holders.ability;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.obtuse.game.gameobjects.BasicObject;
import com.obtuse.game.gameobjects.UI.Border;

public class AbilityBackground extends BasicObject {
    // 0 = default, 1 = hovered, 2 = selected — drives the gold box's frame brightness.
    private int state = 0;
    private boolean disabled = false;
    private static final com.badlogic.gdx.graphics.Color DIM =
            new com.badlogic.gdx.graphics.Color(0.07f, 0.07f, 0.09f, 0.6f);

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public AbilityBackground(String name, float defaultFD, float hoveredFD, float selectedFD) {
        path += "UI/abilities/backgrounds/" + name + "/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        addAnimation("hovered", hoveredFD, Animation.PlayMode.LOOP);
        addAnimation("selected", selectedFD, Animation.PlayMode.NORMAL);
        currentlyDisplayed.add(animations.get("default"));
    }

    @Override
    public float play(String animationName, int index) {
        if ("hovered".equals(animationName)) state = 1;
        else if ("selected".equals(animationName)) state = 2;
        else state = 0;
        return super.play(animationName, index);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Royal-gold box instead of the old grey sprite; brighter frame when hovered/selected.
        float t = Math.min(getWidth(), getHeight()) * 0.05f;
        Border.drawGoldBox(batch, getX(), getY(), getWidth(), getHeight(), t, parentAlpha, disabled ? 0 : state);
        if (disabled) // dim the whole box when the move has no PP left
            Border.fillRect(batch, getX(), getY(), getWidth(), getHeight(), DIM, parentAlpha);
    }

    public void create(float x, float y, float width, float height) {
        setWidth(width);
        setHeight(height);
        setPosition(x, y);
    }

    public void create(float x, float y) {
        create(x, y, getWidth(), getHeight());
    }
}
