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
    // Inventory grid: 3 columns x 4 rows (= Inventory.maxSize 12). Square cells sized from the
    // visible world height so all 12 fit both the tall portrait and short landscape views; the
    // slot frames are drawn at the full cell size so they tile exactly edge-to-edge.
    private static final int gridCols = 3;
    private static final int gridRows = 4;
    private static final float gridLeftX = 0.35f;   // left edge of the first grid cell
    private static final float rightSlot = 1f;      // ability/equipment slot cell = their 1.0 pitch (tiles too)
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

    // Square cell size: box = cell pitch so the frames tile edge-to-edge; capped so portrait isn't
    // huge, and small enough that all 4 rows fit the short landscape view.
    private float cellSize() {
        return Math.min(1.25f, visibleHeight() * 0.23f);
    }

    private float gridBottomY() {
        return visibleHeight() * 0.43f - gridRows * cellSize() / 2f;
    }

    private float colLeft(int i) {
        return gridLeftX + i * cellSize();
    }

    // Row j counted from the TOP (so items fill top-to-bottom); returns the cell's bottom edge.
    private float rowBottom(int jFromTop) {
        return gridBottomY() + (gridRows - 1 - jFromTop) * cellSize();
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

    // Dashed silver frames around every slot (inventory grid, ability-orb slots, equipment slots),
    // drawn whether or not the slot holds an item. Each frame is a full cell so adjacent frames tile
    // edge-to-edge. World-unit coords/thickness (world camera).
    public void setupBorders() {
        float cs = cellSize();
        for (int j = 0; j < gridRows; j++)
            for (int i = 0; i < gridCols; i++)
                addCellBorder(colLeft(i), rowBottom(j), cs);
        float abilityY = abilityRowY();
        for (float x : abilityOrbX)
            addCellBorder(x + size / 2f - rightSlot / 2f, abilityY + size / 2f - rightSlot / 2f, rightSlot);
        float equipmentY = equipmentRowY();
        for (float x : equipmentX)
            addCellBorder(x + size / 2f - rightSlot / 2f, equipmentY + size / 2f - rightSlot / 2f, rightSlot);
    }

    private void addCellBorder(float x, float y, float cell) {
        Border border = new Border(x, y, cell, cell, cell * 0.02f, Border.SILVER, true);
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
        float cs = cellSize();
        int i = 0;
        int j = 0;
        for (Item item : Inventory.items) {
            if (i >= gridCols) {
                i = 0;
                j++;
            }
            if (j >= gridRows)
                break;
            // Centre the 0.5 icon inside its cell.
            item.create(colLeft(i) + cs / 2f - size / 2f, rowBottom(j) + cs / 2f - size / 2f, size, size);
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
