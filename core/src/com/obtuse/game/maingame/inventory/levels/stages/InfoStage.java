package com.obtuse.game.maingame.inventory.levels.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Fonts;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.abilities.all.SkipTurn;
import com.obtuse.game.gameobjects.UI.Border;
import com.obtuse.game.gameobjects.UI.GoldFrame;
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
    private Array<Label> areaTitles = new Array<Label>();
    private Array<GoldFrame> areaFrames = new Array<GoldFrame>();
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

    // Gold frames + titles around the three grouped areas. The InventoryStage gives world rects;
    // we draw the frames on THIS screen-space stage (the 1x1 pixel bleeds on the world camera) by
    // projecting those rects to screen — the same projection the item-name labels use.
    public void setupAreaTitles(com.obtuse.game.maingame.inventory.levels.stages.InventoryStage inv) {
        clearAreaTitles();
        addArea("Inventory", inv.gridArea());
        addArea("Moves", inv.movesArea());
        addArea("Items", inv.itemsArea());
    }

    private void addArea(String text, float[] r) {
        float sx = r[0] / cameraWidth * w(1);
        float sy = r[1] / cameraWidth * ratio * h(1);
        float sw = r[2] / cameraWidth * w(1);
        float sh = r[3] / cameraWidth * ratio * h(1);
        GoldFrame frame = new GoldFrame(sx, sy, sw, sh);
        areaFrames.add(frame);
        add(frame);

        Label label = new Label(text, Fonts.get("inventoryInfoTableTitle"));
        label.setColor(Border.ROYAL_BRIGHTGOLD);
        label.setWidth(sw);
        label.setAlignment(Align.left);
        label.setPosition(sx, sy + sh + h(0.004f));
        areaTitles.add(label);
        add(label);
    }

    public void clearAreaTitles() {
        for (Label label : areaTitles)
            label.remove();
        for (GoldFrame frame : areaFrames)
            frame.remove();
        areaTitles.clear();
        areaFrames.clear();
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

    // Name tag over the SELECTED hero's head (only the selected hero shows one).
    public void addHeroName(Hero hero, InventoryStage inv) {
        Label name = new Label(hero.getName(), Fonts.get("fightInfoTable"));
        name.setColor(Border.ROYAL_TEXT);
        name.setWidth(w(0.25f));
        name.setWrap(true);
        name.setAlignment(Align.center);
        name.setPosition(inv.selectedCenterXWorld() / cameraWidth * w(1) - name.getWidth() / 2,
                inv.selectedTopYWorld() / cameraWidth * ratio * h(1) + h(0.006f));
        additionalLabels.add(name);
        add(name);
    }

    // HP + SPD for the shown hero, written between the hero and the Moves box.
    public void addHeroStats(Hero hero, InventoryStage inv) {
        Label stats = new Label(hero.hp + " HP    " + hero.speed + " SPD",
                Fonts.get("inventoryInfoTableContent"));
        stats.setColor(Border.ROYAL_TEXT);
        float sw = w(0.34f);
        stats.setWidth(sw);
        stats.setAlignment(Align.center);
        stats.setPosition(inv.selectedCenterXWorld() / cameraWidth * w(1) - sw / 2,
                inv.statsRowY() / cameraWidth * ratio * h(1));
        additionalLabels.add(stats);
        add(stats);
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
