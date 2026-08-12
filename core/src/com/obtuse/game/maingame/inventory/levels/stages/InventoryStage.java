package com.obtuse.game.maingame.inventory.levels.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;
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
    // Page navigation: the inventory holds every item, shown 12 (one grid) at a time.
    public Array<SimpleArrow> pageArrows = new Array<SimpleArrow>();
    public Array<Item> pageItems = new Array<Item>();   // the items on the currently shown page
    public int page = 0;

    public InventoryStage(Stage stage) {
        super(stage);
    }

    private static int perPage() {
        return gridCols * gridRows;
    }

    public int pageCount() {
        return Math.max(1, (Inventory.items.size + perPage() - 1) / perPage());
    }

    public void setup(Hero hero, int page) {
        this.page = page;
        clearItems();
        clearArrows();
        clearPageArrows();
        setupInventory(page);
        setupAbilityOrbs(hero);
        setupEquipment(hero);
        makeArrows();
        makePageArrows();
    }

    private float visibleHeight() {
        return Obtuse.cameraWidth / Obtuse.ratio;
    }

    // Cells are TALLER than they are wide so the item name fits under the icon inside the frame.
    // Width is fixed (3 columns fit the left third); height is capped for portrait and shrunk so
    // all 4 rows still fit the short landscape view. Frames are a full cell so they tile edge-to-edge.
    private float cellW() {
        return 1.15f;
    }

    private float cellH() {
        return Math.min(1.55f, visibleHeight() * 0.9f / gridRows);
    }

    private float gridBottomY() {
        return visibleHeight() * 0.43f - gridRows * cellH() / 2f;
    }

    private float colLeft(int i) {
        return gridLeftX + i * cellW();
    }

    // Row j counted from the TOP (so items fill top-to-bottom); returns the cell's bottom edge.
    private float rowBottom(int jFromTop) {
        return gridBottomY() + (gridRows - 1 - jFromTop) * cellH();
    }

    // Right column (hero on top, then ability-orb row, then equipment row). In portrait the roomy
    // const/ratio spacing the layout was designed for is kept; in landscape the view is short, so
    // those rows collapse into each other — there we stack them from the bottom with a minimum gap
    // (bigger than a slot box) so nothing overlaps and the hero stays on screen.
    // Items row sits low; Moves a good gap above it; the hero higher still, leaving room BELOW the
    // hero for its name + HP/SPD line. Portrait has plenty of height so the rows are spread out;
    // landscape is short, so the gaps shrink to whatever still fits on screen without overlap.
    private float equipmentRowY() {
        if (Obtuse.ratio < 1f) return 2.6f / Obtuse.ratio;
        return Math.max(1.0f, visibleHeight() * 0.06f);
    }

    private float abilityRowY() {
        if (Obtuse.ratio < 1f) return 5.4f / Obtuse.ratio;   // ~2.8 gap above Items
        // Landscape is short: keep the gaps just big enough that the boxes don't overlap and the
        // hero row still fits on screen.
        return equipmentRowY() + Math.max(1.4f, visibleHeight() * 0.2f);
    }

    private float heroRowY() {
        if (Obtuse.ratio < 1f) return 7.9f / Obtuse.ratio;   // ~2.5 gap above Moves for name + HP/SPD
        return abilityRowY() + Math.max(1.4f, visibleHeight() * 0.2f);
    }

    // World-unit rects [x, y, w, h] of the three grouped areas: the inventory grid, the ability-orb
    // ("Moves") row, and the equipment ("Items") row. The InfoStage reads these to place titles.
    public float[] gridArea() {
        float pad = 0.14f;
        return new float[]{gridLeftX - pad, gridBottomY() - pad, gridCols * cellW() + 2 * pad, gridRows * cellH() + 2 * pad};
    }

    // Extra room below the icon row so the item name (drawn under the icon) fits inside the frame.
    // Portrait has the height for a roomy box; landscape is short, so the box is shorter there.
    private float areaPadBottom() {
        return Obtuse.ratio < 1f ? 0.95f : 0.5f;
    }

    public float[] movesArea() {
        float padX = 0.24f, padTop = 0.24f, padBottom = areaPadBottom();
        float left = abilityOrbX[0], right = abilityOrbX[abilityOrbX.length - 1] + size, y = abilityRowY();
        return new float[]{left - padX, y - padBottom, (right - left) + 2 * padX, size + padTop + padBottom};
    }

    public float[] itemsArea() {
        float padX = 0.24f, padTop = 0.24f, padBottom = areaPadBottom();
        float left = equipmentX[0], right = equipmentX[equipmentX.length - 1] + size, y = equipmentRowY();
        return new float[]{left - padX, y - padBottom, (right - left) + 2 * padX, size + padTop + padBottom};
    }

    public void clearArrows() {
        for (SimpleArrow arrow : arrows)
            arrow.remove();
        arrows.clear();
    }

    // Left/right arrows flank the hero to cycle through the party.
    public void makeArrows() {
        addArrow(new LeftArrow(), 5.5f, heroRowY(), 0.5f, 1f);
        addArrow(new RightArrow(), 9f, heroRowY(), 0.5f, 1f);
    }

    private void addArrow(SimpleArrow arrow, float x, float y, float width, float height) {
        arrow.create(x, y, width, height);
        arrows.add(arrow);
        add(arrow);
    }

    // Prev/next page arrows below the grid (only when there is more than one page).
    public void makePageArrows() {
        if (pageCount() <= 1)
            return;
        float y = gridBottomY() - 0.85f;
        float gridRight = gridLeftX + gridCols * cellW();
        addPageArrow(new LeftArrow(), gridLeftX, y, 0.45f, 0.6f);
        addPageArrow(new RightArrow(), gridRight - 0.45f, y, 0.45f, 0.6f);
    }

    private void addPageArrow(SimpleArrow arrow, float x, float y, float width, float height) {
        arrow.create(x, y, width, height);
        pageArrows.add(arrow);
        add(arrow);
    }

    public void clearPageArrows() {
        for (SimpleArrow arrow : pageArrows)
            arrow.remove();
        pageArrows.clear();
    }

    // World point centred below the grid, where the "page X / N" label goes.
    public float pageLabelCenterX() {
        return gridLeftX + gridCols * cellW() / 2f;
    }

    public float pageLabelY() {
        return gridBottomY() - 0.7f;
    }

    public void makeHero(Hero hero) {
        hero.create(7.5f - hero.getWidth() / 2, heroRowY());
        hero.play("summon");
        add(hero);
    }

    public void setupInventory(int page) {
        pageItems.clear();
        float cw = cellW(), ch = cellH();
        int start = page * perPage();
        for (int k = 0; k < perPage(); k++) {
            int idx = start + k;
            if (idx >= Inventory.items.size)
                break;
            Item item = Inventory.items.get(idx);
            int i = k % gridCols, j = k / gridCols;
            // Icon in the upper part of the cell; its name label sits below it, inside the frame.
            item.create(colLeft(i) + (cw - size) / 2f, rowBottom(j) + ch - size - ch * 0.12f, size, size);
            addItem(item);
            pageItems.add(item);
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
