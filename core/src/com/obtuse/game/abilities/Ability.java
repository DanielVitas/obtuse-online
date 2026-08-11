package com.obtuse.game.abilities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.obtuse.game.Obtuse;
import com.obtuse.game.animations.AnimationDrawable;
import com.obtuse.game.gameobjects.BasicObject;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.buffs.EchoStatus;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Slot;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.levels.FightLevel;

import java.util.HashMap;
import java.util.Map;

public  abstract class Ability {
    private String displayName;
    public String description = "";
    public int pp;
    public Array<Integer> targets = new Array<Integer>();
    public Array<Integer> suggestedTargets = new Array<Integer>();
    protected Map<String, AnimationDrawable> animations = new HashMap<String, AnimationDrawable>();
    protected String path = "fight/abilities/";

    public static int HEROSLOT = 0;
    public static int ENEMYSLOT = 1;
    public static int SUMMONSLOT = 2;
    public static int HERO = 3;
    public static int ENEMY = 4;
    public static int SUMMON = 5;
    public static int EMPTYHEROSLOT = 6; // HEROSLOT \ HERO
    public static int EMPTYENEMYSLOT = 7;
    public static int EMPTYSUMMONSLOT = 8;

    public Ability(String name, int pp) {
        path += name + "/";
        this.pp = pp;
    }

    protected void addAnimation(String animationName, float frameDuration, Animation.PlayMode playMode, float width,
                                float height, float additionalX, float additionalY) {
        animations.put(animationName, new AnimationDrawable(new Animation<TextureRegion>(frameDuration,
                Obtuse.textureAtlas.findRegions(path + animationName + "/main"), playMode),
                width, height, additionalX, additionalY));
    }

    public float play(BasicObject object, String animationName) {
        return play(object, animationName, object.currentlyDisplayed.size);
    }

    public float playCharacter(Slot object, String animationName) {
        if (object.holder.fightObject != null)
            return play(object.holder.fightObject, animationName);
        return play(object, animationName, object.currentlyDisplayed.size);
    }

    public float play(final BasicObject object, String animationName, final int index) {
        AnimationDrawable animationDrawable = animations.get(animationName).clone();
        animationDrawable.reset();
        animationDrawable.additionalX = object.getWidth() / 2 - animationDrawable.width / 2;
        if (object instanceof Slot)
            animationDrawable.additionalY = object.getHeight() / 2;
        object.currentlyDisplayed.insert(index, animationDrawable);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                try {
                    object.currentlyDisplayed.removeIndex(index);
                } catch (IndexOutOfBoundsException e) {
                    object.currentlyDisplayed.removeIndex(object.currentlyDisplayed.size - 1);
                }
            }
        }, animationDrawable.duration());
        return animationDrawable.duration();
    }

    protected void addTarget(int i) {
        targets.add(i);
    }

    protected void addSuggestedTarget(int i) {
        suggestedTargets.add(i);
    }

    public abstract void cast(FightObject caster, Holder target, FightLevel level);
    public void animate(FightObject caster, Holder target, FightLevel level) {
        Turn.sleep(caster.cast());
    }

    public void run(FightObject caster, Holder target, FightLevel level) {
        cast(caster, target, level);
        animate(caster, target, level);
        if (caster.getBuff(EchoStatus.class) != null) {
            cast(caster, target, level);
            animate(caster, target, level);
            caster.removeBuff(caster.getBuff(EchoStatus.class));
        }
    }

    public boolean validTarget(Holder holder) {
        for (int i : holder.getTypes())
            if (targets.contains(i, true))
                return true;
        return false;
    }

    public boolean validInverseTarget(Holder holder) {
        for (int i : holder.getTypes())
            if (inverseTargets(targets).contains(i, true))
                return true;
        return false;
    }

    public Array<Integer> inverseTargets(Array<Integer> targets) {
        Array<Integer> inverseTargets = new Array<Integer>();
        if (targets.contains(HERO, true))
            inverseTargets.add(ENEMY);
        if (targets.contains(ENEMY, true))
            inverseTargets.add(HERO, SUMMON);
        if (targets.contains(HEROSLOT, true))
            inverseTargets.add(ENEMYSLOT);
        if (targets.contains(ENEMYSLOT, true))
            inverseTargets.add(HEROSLOT, SUMMONSLOT);
        if (targets.contains(EMPTYHEROSLOT, true))
            inverseTargets.add(EMPTYENEMYSLOT);
        if (targets.contains(EMPTYENEMYSLOT, true))
            inverseTargets.add(EMPTYHEROSLOT, EMPTYSUMMONSLOT);
        return inverseTargets;
    }

    public void setName(String name) {
        displayName = name;
    }

    public String getName() {
        return displayName;
    }
}
