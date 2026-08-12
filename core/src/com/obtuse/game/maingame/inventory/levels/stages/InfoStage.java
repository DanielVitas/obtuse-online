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
        infoBackground.create(0, 0, w(0.32f), h(0.1f)); // base width; buildTooltip grows it taller for long text
        add(infoBackground);
    }

    public void setup(Item item, Hero hero) {
        createGeneralBackground();
        Label name = new Label(item.getName(), Fonts.get("inventoryInfoTableTitle"));
        Label pp = null;
        String desc = item.getDescription();
        if (item instanceof AbilityOrb) {
            pp = new Label(((AbilityOrb) item).ability.pp + " PP", Fonts.get("inventoryInfoTableContent"));
            // Damage recomputed for the viewed hero's equipment (coloured if changed).
            desc = ((AbilityOrb) item).ability.describedFor(hero);
        }
        Label description = new Label(desc, Fonts.get("inventoryInfoTableDescription"));
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

    public void setItemLabels(Hero hero, InventoryStage inv) {
        clearItemLabels();
        addItemLabels(inv.pageItems);          // only the items on the current grid page
        addItemLabels(hero.abilityOrbs);
        addItemLabels(hero.equipment);
        addPageLabel(inv);
    }

    // "page X / N" centred under the grid, between the page arrows (only when there are >1 pages).
    private void addPageLabel(InventoryStage inv) {
        int count = inv.pageCount();
        if (count <= 1)
            return;
        Label page = new Label((inv.page + 1) + " / " + count, Fonts.get("inventoryInfoTableContent"));
        page.setColor(Border.ROYAL_TEXT);
        page.setAlignment(Align.center);
        page.pack();
        page.setPosition(inv.pageLabelCenterX() / cameraWidth * w(1) - page.getWidth() / 2,
                inv.pageLabelY() / cameraWidth * ratio * h(1));
        itemLabels.add(page);
        add(page);
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

    // Name centred BELOW the hero, with HP + SPD on the line below the name.
    public void addHeroInfo(Hero hero) {
        float cx = (hero.getX() + hero.getWidth() / 2f) / cameraWidth * w(1);
        float feetY = hero.getY() / cameraWidth * ratio * h(1);

        Label name = new Label(hero.getName(), Fonts.get("fightInfoTable"));
        name.setColor(Border.ROYAL_TEXT);
        name.setAlignment(Align.center);
        name.pack();
        float nameY = feetY - h(0.01f) - name.getHeight();
        name.setPosition(cx - name.getWidth() / 2, nameY);
        additionalLabels.add(name);
        add(name);

        // HP/SPD with the hero's equipment factored in — green if a piece buffs it, red if it costs.
        int effHp = hero.hp, effSpd = hero.speed;
        for (com.obtuse.game.gameobjects.items.Equipment eq : hero.equipment) {
            effHp = eq.previewMaxHp(effHp);
            effSpd = eq.previewSpeed(effSpd);
        }
        Label stats = new Label(colouredStat(effHp, hero.hp) + " HP   " + colouredStat(effSpd, hero.speed) + " SPD",
                Fonts.get("inventoryInfoTableContent"));
        stats.setColor(Border.ROYAL_TEXT);
        stats.setAlignment(Align.center);
        stats.pack();
        float statsY = nameY - h(0.004f) - stats.getHeight();
        stats.setPosition(cx - stats.getWidth() / 2, statsY);
        additionalLabels.add(stats);
        add(stats);
    }

    // A stat number, coloured green if higher than base (a buff) or red if lower (a penalty).
    private static String colouredStat(int value, int base) {
        if (value > base) return "[#7ddc7d]" + value + "[]";
        if (value < base) return "[#ff6b6b]" + value + "[]";
        return Integer.toString(value);
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
