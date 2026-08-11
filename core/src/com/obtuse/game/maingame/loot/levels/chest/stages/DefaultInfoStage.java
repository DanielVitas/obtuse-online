package com.obtuse.game.maingame.loot.levels.chest.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Fonts;
import com.obtuse.game.Obtuse;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.gameobjects.items.AbilityOrb;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.GameStage;
import com.obtuse.game.maingame.loot.LootLevel;

import static com.obtuse.game.Fonts.get;
import static com.obtuse.game.Obtuse.*;

public class DefaultInfoStage extends GameStage {
    public Array<Label> labels = new Array<Label>();

    public DefaultInfoStage(Stage stage) {
        super(stage);
    }

    public void setup(Item item) {
        if (item instanceof AbilityOrb)
            setup((AbilityOrb) item);
        else if (item instanceof Equipment)
            setup((Equipment) item);
    }

    public void setup(AbilityOrb abilityOrb) {
        Label pp = new Label(Integer.toString(abilityOrb.ability.pp) + " PP", Fonts.get("lootDescription"));
        pp.setWidth(w(0.8f));
        pp.setAlignment(Align.right);
        pp.setPosition(w(0.1f), h(0.1f));
        addLabel(pp);

        Label description = new Label(abilityOrb.getDescription(), Fonts.get("lootDescription"));
        description.setWidth(w(0.8f));
        description.setWrap(true);
        description.setAlignment(Align.topLeft);
        description.setPosition(w(0.1f), h(0.35f));
        addLabel(description);
    }

    public void setup(Equipment equipment) {
        Label description = new Label(equipment.getDescription(), Fonts.get("lootDescription"));
        description.setWidth(w(0.8f));
        description.setWrap(true);
        description.setAlignment(Align.topLeft);
        description.setPosition(w(0.1f), h(0.35f));
        addLabel(description);
    }

    public void addLabel(Label label) {
        labels.add(label);
        add(label);
    }

    public void clearLabels() {
        for (Label label : labels)
            label.remove();
        labels.clear();
    }

    @Deprecated
    public void labelAbove(GameButton button, Item item) {
        Label name = new Label(item.getName(), Fonts.get("fightInfoTable"));

        name.setWrap(true);
        name.pack();
        name.setWidth(w(0.2f));
        name.pack();
        name.setWidth(w(0.2f));

        name.setAlignment(Align.bottom);
        name.setPosition(button.getX() / cameraWidth * w(1) + button.getWidth() / 2 / cameraWidth * w(1) - name.getWidth() / 2,
                button.getY() / cameraWidth * ratio * h(1) + button.getHeight() / cameraWidth * h(1) + h(0.05f));
        add(name);
    }

    public void labelBelow(GameButton button, Item item) {
        Label name = new Label(item.getName(), Fonts.get("fightInfoTable"));
        name.setWidth(w(0.2f));
        name.setWrap(true);
        name.setAlignment(Align.top);
        name.setPosition(button.getX() / cameraWidth * w(1) + button.getWidth() / 2 / cameraWidth * w(1) - name.getWidth() / 2,
                button.getY() / cameraWidth * ratio * h(1) - name.getHeight() - h(0.01f));
        add(name);
    }

    @Override
    protected void createLights() {

    }

    @Override
    protected void createEnvironment() {

    }
}
