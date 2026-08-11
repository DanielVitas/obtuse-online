package com.obtuse.game.gameobjects.fight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.gameobjects.BasicObject;

public class Profile extends BasicObject {
    public boolean onTurn = false;
    public boolean hovered = false;

    public Profile(String name, float defaultFD, float onTurnDF) {
        path += "fight/" + name + "/profile/";
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        addAnimation("onTurn", onTurnDF, Animation.PlayMode.LOOP);
        addAnimationBackground("default",1f, Animation.PlayMode.LOOP);
        addAnimationBackground("hovered",1f, Animation.PlayMode.LOOP);
        addAnimationBackground("onTurn",1f, Animation.PlayMode.LOOP);
        currentlyDisplayed.add(animations.get("profile-default"), animations.get("default"));
        setSize(0.5f,0.5f);
    }

    public float defaultAnimation() {
        if (hovered)
            return play("profile-hovered",0);
        else
            if (onTurn)
                return play("profile-onTurn",0);
            else
                return play("profile-default",0);
    }

    public float onTurn() {
        onTurn = true;
        return defaultAnimation();
    }

    public float offTurn() {
        onTurn = false;
        return defaultAnimation();
    }

    public float hover() {
        hovered = true;
        return defaultAnimation();
    }

    public float unhover() {
        hovered = false;
        return defaultAnimation();
    }

    public void addAnimationBackground(String animationName, float frameDuration, Animation.PlayMode playMode) {
        animations.put("profile-" + animationName, new AnimationDrawable(new Animation<TextureRegion>(frameDuration,
                Obtuse.textureAtlas.findRegions("UI/profiles/basic/" + animationName + "/main"), playMode),
                9f / 16, 9f / 16, -0.5f / 16, -0.5f / 16));
    }
}
