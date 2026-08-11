package com.obtuse.game.maingame.loot.levels.chest.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Fonts;
import com.obtuse.game.Obtuse;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.gameobjects.UI.InfoBackground;
import com.obtuse.game.gameobjects.UI.info.GeneralInfoBackground;
import com.obtuse.game.gameobjects.items.AbilityOrb;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.GameStage;
import com.obtuse.game.maingame.loot.LootLevel;

import static com.obtuse.game.Fonts.get;
import static com.obtuse.game.Obtuse.*;

public class DefaultInfoStage extends GameStage {
    public Array<Label> labels = new Array<Label>();
    private InfoBackground infoBackground;

    public DefaultInfoStage(Stage stage) {
        super(stage);
    }

    // A bordered tooltip next to the pointer (name + PP + description), instead of the old flat
    // description printed at the bottom of the screen.
    public void setup(Item item) {
        infoBackground = new GeneralInfoBackground();
        infoBackground.create(w(0.55f), h(0.01f), w(0.445f), h(0.35f));
        infoBackground.positionAtPointer(stage);
        add(infoBackground);

        Label name = new Label(item.getName(), Fonts.get("inventoryInfoTableTitle"));
        name.setWidth(infoBackground.getWidth());
        name.setWrap(true);
        name.setAlignment(Align.bottom);
        name.setPosition(infoBackground.getX() + infoBackground.getWidth() / 2 - name.getWidth() / 2,
                infoBackground.getY() + infoBackground.getHeight());
        addLabel(name);

        if (item instanceof AbilityOrb) {
            Label pp = new Label(Integer.toString(((AbilityOrb) item).ability.pp) + " PP",
                    Fonts.get("inventoryInfoTableContent"));
            pp.setWidth(infoBackground.getWidth());
            pp.setAlignment(Align.right);
            pp.setPosition(infoBackground.getX(), infoBackground.getY());
            addLabel(pp);
        }

        Label description = new Label(item.getDescription(), Fonts.get("inventoryInfoTableDescription"));
        description.setWidth(infoBackground.getWidth());
        description.setWrap(true);
        description.setAlignment(Align.topLeft);
        description.setPosition(infoBackground.getX() + w(0.0025f),
                infoBackground.getY() + infoBackground.getHeight() - description.getHeight() - h(0.0025f));
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
        if (infoBackground != null)
            infoBackground.remove();
        infoBackground = null;
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
