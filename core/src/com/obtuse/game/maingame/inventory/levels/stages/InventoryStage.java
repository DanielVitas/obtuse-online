package com.obtuse.game.maingame.inventory.levels.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.UI.Border;
import com.obtuse.game.gameobjects.UI.arrows.DownArrow;
import com.obtuse.game.gameobjects.UI.SimpleArrow;
import com.obtuse.game.gameobjects.UI.arrows.LeftArrow;
import com.obtuse.game.gameobjects.UI.arrows.RightArrow;
import com.obtuse.game.gameobjects.UI.arrows.UpArrow;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.items.Inventory;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.GameStage;

public class InventoryStage extends GameStage {
    private static float size = 0.5f;               // item icon size
    private static float box = 0.85f;               // slot frame — bigger than the icon; items still fit side by side
    // Inventory grid: 3 columns x 4 rows (= Inventory.maxSize 12). Tall-ish so it fits the portrait
    // screen; the row Y is derived from the visible world height each time so all 12 slots stay on
    // screen in landscape too (they used to be fixed world-Y and fell off the top in landscape).
    private static final float[] gridColumns = {0.55f, 1.8f, 3.05f};
    private static final int gridRows = 4;
    private static final float[] abilityOrbX = {5.75f, 6.75f, 7.75f, 8.75f};
    private static final float[] equipmentX = {6.25f, 7.25f, 8.25f};
    private Array<Item> created = new Array<Item>();
    public Array<SimpleArrow> arrows = new Array<SimpleArrow>();
    private Array<Border> borders = new Array<Border>();

    public InventoryStage(Stage stage) {
        super(stage);
    }

    public void setup(Hero hero) {
        clearItems();
        clearArrows();
        clearBorders();
        setupBorders();
        setupInventory();
        setupAbilityOrbs(hero);
        setupEquipment(hero);
        makeArrows();
    }

    private float visibleHeight() {
        return Obtuse.cameraWidth / Obtuse.ratio;
    }

    // World-unit Y of each inventory-grid row (item bottom-left). Centred vertically and scaled to
    // the visible world height so the 4 rows fit both the tall portrait and short landscape views.
    private float[] gridRowsY() {
        float vh = visibleHeight();
        float step = Math.min(vh * 0.22f, 2.2f);
        float centerY = vh * 0.43f;
        float half = (gridRows - 1) / 2f;
        float[] ys = new float[gridRows];
        for (int j = 0; j < gridRows; j++)
            ys[j] = centerY + (half - j) * step - size / 2f;
        return ys;
    }

    // Right column (hero on top, then ability-orb row, then equipment row). In portrait the roomy
    // const/ratio spacing the layout was designed for is kept; in landscape the view is short, so
    // those rows collapse into each other — there we stack them from the bottom with a minimum gap
    // (bigger than a slot box) so nothing overlaps and the hero stays on screen.
    private float equipmentRowY() {
        if (Obtuse.ratio < 1f) return 4.5f / Obtuse.ratio;
        return visibleHeight() * 0.08f;
    }

    private float rightGap() {
        return Math.max(1.15f, visibleHeight() * 0.18f);
    }

    private float abilityRowY() {
        if (Obtuse.ratio < 1f) return 6f / Obtuse.ratio;
        return equipmentRowY() + rightGap();
    }

    private float heroRowY() {
        if (Obtuse.ratio < 1f) return 7.5f / Obtuse.ratio;
        return abilityRowY() + rightGap();
    }

    // Gold frames around every slot (inventory grid, ability-orb slots, equipment slots) — drawn
    // whether or not the slot currently holds an item. World-unit coords/thickness (world camera).
    public void setupBorders() {
        float[] ys = gridRowsY();
        for (float y : ys)
            for (float x : gridColumns)
                addSlotBorder(x, y);
        float abilityY = abilityRowY();
        for (float x : abilityOrbX)
            addSlotBorder(x, abilityY);
        float equipmentY = equipmentRowY();
        for (float x : equipmentX)
            addSlotBorder(x, equipmentY);
    }

    // A frame centred on the item cell, a bit bigger than the 0.5 icon.
    private void addSlotBorder(float itemX, float itemY) {
        float t = 0.045f;
        Border border = new Border(itemX + size / 2f - box / 2f, itemY + size / 2f - box / 2f, box, box, t);
        borders.add(border);
        add(border);
    }

    public void clearBorders() {
        for (Border border : borders)
            border.remove();
        borders.clear();
    }

    public void clearArrows() {
        for (SimpleArrow arrow : arrows)
            arrow.remove();
        arrows.clear();
    }

    public void makeArrows() {
        addArrow(new LeftArrow(), 5.5f, heroRowY(), 0.5f, 1f);
        addArrow(new RightArrow(), 9f, heroRowY(), 0.5f, 1f);
    }

    private void addArrow(SimpleArrow arrow, float x, float y, float width, float height) {
        arrow.create(x, y, width, height);
        arrows.add(arrow);
        add(arrow);
    }

    public void makeHero(Hero hero) {
        hero.create(7.5f - hero.getWidth() / 2, heroRowY());
        hero.play("summon");
        add(hero);
    }

    public void setupInventory() {
        float[] ys = gridRowsY();
        int i = 0;
        int j = 0;
        for (Item item : Inventory.items) {
            if (i >= gridColumns.length) {
                i = 0;
                j++;
            }
            if (j >= ys.length)
                break;
            item.create(gridColumns[i], ys[j], size, size);
            addItem(item);
            i++;
        }
    }

    public void setupAbilityOrbs(Hero hero) {
        float y = abilityRowY();
        for (int i = 0; i < hero.abilityOrbs.size; i++) {
            hero.abilityOrbs.get(i).create(abilityOrbX[i], y, size, size);
            addItem(hero.abilityOrbs.get(i));
        }
    }

    public void setupEquipment(Hero hero) {
        float y = equipmentRowY();
        for (int i = 0; i < hero.equipment.size; i++) {
            hero.equipment.get(i).create(equipmentX[i], y, size, size);
            addItem(hero.equipment.get(i));
        }
    }

    public void clearItems() {
        for (Item item : created)
            item.remove();
        created.clear();
    }

    public void addItem(Item item) {
        created.add(item);
        add(item);
    }

    @Override
    protected void createLights() {

    }

    @Override
    protected void createEnvironment() {

    }
}
