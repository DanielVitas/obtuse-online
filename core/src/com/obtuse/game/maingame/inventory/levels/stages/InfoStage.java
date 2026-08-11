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
        infoBackground.create(0, 0, w(0.42f), h(0.1f)); // width only; buildTooltip sizes + positions
        add(infoBackground);
    }

    public void setup(Item item) {
        createGeneralBackground();
        Label name = new Label(item.getName(), Fonts.get("inventoryInfoTableTitle"));
        Label pp = null;
        if (item instanceof AbilityOrb)
            pp = new Label(((AbilityOrb) item).ability.pp + " PP", Fonts.get("inventoryInfoTableContent"));
        Label description = new Label(item.getDescription(), Fonts.get("inventoryInfoTableDescription"));
        infoBackground.buildTooltip(stage, name, pp, null, description);
        addInfoLabel(name);
        if (pp != null)
            addInfoLabel(pp);
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
        name.setWidth(w(0.11f)); // ~ the grid cell width, so short names fit on one line
        name.setWrap(true);
        name.setAlignment(Align.top);
        name.setPosition(item.getX() / cameraWidth * w(1) + item.getWidth() / 2 / cameraWidth * w(1) - name.getWidth() / 2,
                item.getY() / cameraWidth * ratio * h(1) - name.getHeight() - h(0.005f));
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
