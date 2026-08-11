package com.obtuse.game.gameobjects.fight.holders.fightObject;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.animations.FloatContainer;
import com.obtuse.game.animations.GameAnimation;
import com.obtuse.game.gameobjects.BasicObject;
import com.obtuse.game.maingame.fight.Turn;

public class HealthBar extends BasicObject {
    public Holder holder;
    private FloatContainer counter = new FloatContainer(0);

    public HealthBar(Holder holder) {
        this.holder = holder;
        path += "fight/healthBar/" ;
        animations.put("default", new GameAnimation(new Animation<TextureRegion>(0.01f,
                Obtuse.textureAtlas.findRegions(path + "default" + "/main"), Animation.PlayMode.REVERSED),
                counter, 0, 0));
        addAnimation("reversed", 0.005f, Animation.PlayMode.REVERSED);
        addAnimation("background", 1f, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("background"));
        currentlyDisplayed.add(animations.get("default"));
        setSize(0.5f,0.5f);
    }

    public void fill() {
        new Turn() {
            @Override
            public void run() {
                Turn.sleep(play("reversed", 1));
                play("default",1);
                holder.refreshHealthBar();
            }
        };
    }

    public void update(float f) {
        counter.set(f);
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
