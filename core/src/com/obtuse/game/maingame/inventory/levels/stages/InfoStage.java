package com.obtuse.game.maingame.inventory.levels.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Fonts;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.abilities.all.SkipTurn;
import com.obtuse.game.gameobjects.UI.InfoBackground;
import com.obtuse.game.gameobjects.UI.info.GeneralInfoBackground;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.items.AbilityOrb;
import com.obtuse.game.gameobjects.items.Inventory;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.GameStage;

import static com.obtuse.game.Obtuse.*;

public class InfoStage extends GameStage {
    private Array<Label> itemLabels = new Array<Label>();
    private Array<Label> additionalLabels = new Array<Label>();
    private Array<Label> infoLabels = new Array<Label>();
    private InfoBackground infoBackground;

    public InfoStage(Stage stage) {
        super(stage);
    }

    private void createGeneralBackground() {
        infoBackground = new GeneralInfoBackground();
        infoBackground.create(w(0.55f), h(0.01f), w(0.445f), h(0.35f));
        infoBackground.positionAtPointer(stage);
        add(infoBackground);
    }

    public void setup(Item item) {
            createGeneralBackground();

            if (item instanceof AbilityOrb) {
                Label pp = new Label(Integer.toString(((AbilityOrb) item).ability.pp) + " PP", Fonts.get("inventoryInfoTableContent"));
                pp.setWidth(infoBackground.getWidth());
                pp.setWrap(true);
                pp.setAlignment(Align.right);
                pp.setPosition(infoBackground.getX(),infoBackground.getY());
                addInfoLabel(pp);
            }

            Label name = new Label(item.getName(), Fonts.get("inventoryInfoTableTitle"));
            name.setWidth(infoBackground.getWidth());
            name.setWrap(true);
            name.setAlignment(Align.bottom);
            name.setPosition(infoBackground.getX() + infoBackground.getWidth() / 2 - name.getWidth() / 2,
                    infoBackground.getY() + infoBackground.getHeight());
            addInfoLabel(name);

            Label description = new Label(item.getDescription(), Fonts.get("inventoryInfoTableDescription"));
            description.setWidth(infoBackground.getWidth() - w(0.00f));
            description.setWrap(true);
            description.setAlignment(Align.topLeft);
            description.setPosition(infoBackground.getX() +  w(0.0025f),
                    infoBackground.getY()  + infoBackground.getHeight() - description.getHeight() - h(0.0025f));
            addInfoLabel(description);
    }

    public void addInfoLabel(Label label) {
        infoLabels.add(label);
        add(label);
    }

    public void clearInfoLabels() {
        for (Label label : infoLabels)
            label.addAction(Actions.removeActor());
        if (infoBackground != null)
            infoBackground.remove();
        infoLabels.clear();
        infoBackground = null;
    }

    public void setItemLabels(Hero hero) {
        clearItemLabels();
        addItemLabels(Inventory.items);
        addItemLabels(hero.abilityOrbs);
        addItemLabels(hero.equipment);
    }

    public void clearItemLabels() {
        for (Label label : itemLabels)
            label.remove();
        itemLabels.clear();
    }

    public void clearAdditionalLabels() {
        for (Label label : additionalLabels)
            label.remove();
        additionalLabels.clear();
    }

    public void addHeroName(Hero hero) {
        Label name = new Label(hero.getName(), Fonts.get("fightInfoTable"));
        name.setWidth(w(0.2f));
        name.setWrap(true);
        name.setAlignment(Align.top);
        name.setPosition(hero.getX() / cameraWidth * w(1) + hero.getWidth() / 2 / cameraWidth * w(1) - name.getWidth() / 2,
                hero.getY() / cameraWidth * ratio * h(1) - name.getHeight() - h(0.01f));
        additionalLabels.add(name);
        add(name);
    }

    private void addItemLabels(Array items) {
        for (Object item : items)
            createItemLabel((Item) item);
    }

    private void createItemLabel(Item item) {
        Label name = new Label(item.getName(), Fonts.get("inventoryName"));
        name.setWidth(w(0.1f));
        name.setWrap(true);
        name.setAlignment(Align.top);
        name.setPosition(item.getX() / cameraWidth * w(1) + item.getWidth() / 2 / cameraWidth * w(1) - name.getWidth() / 2,
                item.getY() / cameraWidth * ratio * h(1) - name.getHeight() - h(0.01f));
        itemLabels.add(name);
        add(name);
    }

    @Override
    protected void createLights() {

    }

    @Override
    protected void createEnvironment() {

    }
}
