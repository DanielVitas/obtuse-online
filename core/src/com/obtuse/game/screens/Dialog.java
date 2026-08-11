package com.obtuse.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;
import com.obtuse.game.bindings.BindingList;
import com.obtuse.game.gameobjects.BasicObject;
import com.obtuse.game.gameobjects.UI.DialogBackground;
import com.obtuse.game.maingame.fight.Turn;

public abstract class Dialog {
    public float duration;
    public String title;
    public String text;
    public long time;
    protected BasicObject background;
    protected Array<Label> labels = new Array<Label>();

    public Dialog(String title, String text) {
        this.title = title;
        this.text = text;
        this.time = System.currentTimeMillis();
        MyScreen.dialogInstance = new Turn() {
            @Override
            public void run() {
                show();
                while (true) {
                    if (MyScreen.dialogInstance != this) {
                        System.out.println("DialogException");
                        hide();
                        break;
                    }
                    if (System.currentTimeMillis() - time > Obtuse.reactionTime)
                        if (Gdx.input.isKeyPressed(BindingList.keys.get("interact"))) {
                            hide();
                            after();
                            break;
                        }
                    Turn.sleep();
                }
            }
        };
    }

    public Dialog(String title, String text, final float duration) {
        this.title = title;
        this.text = text;
        this.duration = duration;
        this.time = System.currentTimeMillis();
        MyScreen.dialogInstance = new Turn() {
            @Override
            public void run() {
                show();
                while (true) {
                    if (MyScreen.dialogInstance != this) {
                        System.out.println("DialogException");
                        break;
                    }
                    if (System.currentTimeMillis() - time > duration * 1000) {
                        hide();
                        after();
                        break;
                    }
                    Turn.sleep();
                }
            }
        };
    }

    public abstract void show();

    public abstract void hide();

    public abstract void after();
}
