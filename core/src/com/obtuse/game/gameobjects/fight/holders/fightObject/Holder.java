package com.obtuse.game.gameobjects.fight.holders.fightObject;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Fonts;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Summon;
import com.obtuse.game.gameobjects.fight.holders.fightObject.slots.BasicSlot;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class Holder {
    public Arena arena;
    public boolean onTurn = false;
    public float x, y;
    public Array<Holder> adjacent = new Array<Holder>();
    public Slot slot;
    public FightObject fightObject;
    public int type;
    public HealthBar healthBar;
    public Label hpLabel;
    public Label damageLabel = new Label("", Fonts.get("damage"));
    public Holder summonHolder = null;
    public int burning = 0;

    public Holder(Arena arena, float x, float y, int type) {
        this.arena = arena;
        slot = new BasicSlot();
        slot.holder = this;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public float burn() {
        return slot.burn();
    }

    public void playSlotAnimation() {
        if (onTurn)
            slot.play("onTurn",0);
        else
            slot.play("default",0);
    }

    public void removeFightObject() {
        ((FightLevel) arena.fightGame.level).removeHolderInfo(this);
        fightObject.remove();
        fightObject.profile.remove();
        healthBar = null;
        fightObject = null;
    }

    public void removeFightWithoutProfileObject() {
        ((FightLevel) arena.fightGame.level).removeHolderInfo(this);
        fightObject.remove();
        healthBar = null;
        fightObject = null;
    }

    public void refreshHPLabel() {
        if (hpLabel != null)
            hpLabel.setText(Integer.toString(fightObject.hp - fightObject.damageTaken));
    }

    public void refreshHealthBar() {
        healthBar.update(((float) (fightObject.hp - fightObject.damageTaken)) / fightObject.hp);
    }

    public void setHealthBarPosition() {
        if (fightObject != null) {
            if (fightObject.getClass().getSuperclass() == Hero.class ||
                    fightObject.getClass().getSuperclass() == Summon.class)
                healthBar.create(getX() + getWidth() + healthBar.getWidth() * 0, getY() + getHeight() - 0.2f);
            if (fightObject.getClass().getSuperclass() == Enemy.class)
                healthBar.create(getX() - healthBar.getWidth(), getY() + getHeight() - 0.2f);
            refreshHealthBar();
        }
    }

    public Array<Integer> getTypes() {
        Array<Integer> types = new Array<Integer>();
        types.add(type);
        if (fightObject != null)
            if (fightObject.alive())
                types.add(type + 3);
            else
                types.add(type + 6);
        else
            types.add(type + 6);
        return types;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y - slot.getHeight() / 2;
    }

    public float getWidth() {
        return slot.getWidth();
    }

    public float getHeight() {
        if (fightObject != null)
            if (fightObject.alive())
                return slot.getHeight() / 2 + fightObject.getClickHeight();
        return slot.getHeight();
    }

    public void createAndAdd(Stage stage) {
        slot.create(x, y - slot.getHeight() / 2);
        stage.addActor(slot);
        if (fightObject != null) {
            fightObject.create(x + slot.getWidth() / 2 - fightObject.getWidth() / 2, y);
            stage.addActor(fightObject);
            if (fightObject.alive()) {
                setHealthBarPosition();
                stage.addActor(healthBar);
            }
        }
    }

    public void setFightObject(FightObject fightObject) {
        fightObject.holder = this;
        this.fightObject = fightObject;
        //hpLabel = new Label("", Fonts.get("testFont2"));
        healthBar = new HealthBar(this);
        refreshHPLabel();
        refreshHealthBar();
        setHealthBarPosition();
    }
}
