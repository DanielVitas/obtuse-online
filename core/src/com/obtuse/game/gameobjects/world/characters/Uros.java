package com.obtuse.game.gameobjects.world.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.obtuse.game.Obtuse;
import com.obtuse.game.bodies.boxes.Box;
import com.obtuse.game.bodies.boxes.SensorBox;
import com.obtuse.game.gameobjects.UI.touch.TouchIcons;
import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.gameobjects.items.abilityorbs.GuardOrb;
import com.obtuse.game.gameobjects.items.abilityorbs.ShieldBashOrb;
import com.obtuse.game.gameobjects.items.equipment.DivineOrb;
import com.obtuse.game.gameobjects.world.MainCharacter;
import com.obtuse.game.gameobjects.world.WorldCharacter;
import com.obtuse.game.progress.ProgressKeeper;
import com.obtuse.game.screens.LootScreen;
import com.obtuse.game.screens.MyScreen;
import com.obtuse.game.screens.dialogs.SpeechBubble;

import static com.obtuse.game.Obtuse.changeScreen;

public class Uros extends WorldCharacter {

    public Uros(float x, float y) {
        super("uros",0.1f, x, y, Obtuse.pixels, Obtuse.pixels);
        setBody(new Box(x, y, BodyDef.BodyType.KinematicBody,6f / 16, 4f / 16,5f / 16,0));
    }

    @Override
    public boolean interact() {
        if (ProgressKeeper.done("win","demon"))
            if (ProgressKeeper.done("win","skeletons") && ProgressKeeper.done("win","redSkull"))
                if (ProgressKeeper.done("quest","monsters1"))
                    Obtuse.dialog(new SpeechBubble(this, "Please don't hurt me, that's all I have. :'(", 3.5f) {
                        @Override
                        public void after() {

                        }
                    });
                else
                    Obtuse.dialog(new SpeechBubble(this, "Wow! Thank you, random adventurer. I stole this gem. " +
                            "You can have it. XD", 3.5f) {
                        @Override
                        public void after() {
                            final Choice choice = new Choice();
                            choice.add(new DivineOrb());
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    ((LootScreen) MyScreen.screens.get("LootScreen")).generateLoot("chest", choice);
                                    changeScreen("LootScreen");
                                }
                            });
                            ProgressKeeper.add("quest","monsters1");
                        }
                    });
            else
                Obtuse.dialog(new SpeechBubble(this, "Since you killed the Demon, skeletons won't be any problem. :)", 3.5f) {
                    @Override
                    public void after() {

                    }
                });
        else
            if (ProgressKeeper.done("win","skeletons"))
                Obtuse.dialog(new SpeechBubble(this, "Hmm, if you are struggling, you might find " +
                        "something useful behind the drawer in front of me. ^^", 3.5f) {
                    @Override
                    public void after() {

                    }
                });
            else
                Obtuse.dialog(new SpeechBubble(this, "T_T There are monsters outside. Please get rid of them. ;_;", 3.5f) {
                    @Override
                    public void after() {

                    }
                });
        return true;
    }

    @Override
    public String actionIcon() {
        return TouchIcons.TALK;
    }

    @Override
    protected void makeSensorBox(int direction) {

    }
}
