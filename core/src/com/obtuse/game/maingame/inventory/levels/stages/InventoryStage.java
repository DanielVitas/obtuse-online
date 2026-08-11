package com.obtuse.game.maingame.inventory.levels.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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

    // The whole party is shown side by side; the selected hero is bigger and stands forward (a lower
    // Y draws on top — getZ() is Y). Selecting another makes it walk forward while the old one steps
    // back "in line". Heroes are drawn to a common target height (so the selected one always reads as
    // bigger regardless of its intrinsic size), keeping each hero's own aspect ratio.
    public Array<Hero> heroes = new Array<Hero>();
    private final Array<Float> baseW = new Array<Float>();
    private final Array<Float> baseH = new Array<Float>();
    public int selectedIndex = 0;
    private static final float SELECTED_H = 2.1f, LINE_H = 1.45f, FORWARD_STEP = 0.4f;

    public InventoryStage(Stage stage) {
        super(stage);
    }

    public void setup(Hero hero) {
        clearItems();
        clearArrows();
        setupInventory();
        setupAbilityOrbs(hero);
        setupEquipment(hero);
        makeArrows();
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
    private float equipmentRowY() {
        if (Obtuse.ratio < 1f) return 4.5f / Obtuse.ratio;
        return visibleHeight() * 0.08f;
    }

    private float rightGap() {
        // Roomier than a slot pitch so the hero (drawn big) plus its HP/SPD stat line both fit in the
        // gap above the Moves box without colliding with the "Moves" title.
        return Math.max(2.4f, visibleHeight() * 0.26f);
    }

    // The hero's HP/SPD line, set in the gap between the hero and the Moves box (clear of both the
    // hero's feet and the "Moves" title just above the box).
    public float statsRowY() {
        float[] moves = movesArea();
        return moves[1] + moves[3] + 0.6f;
    }

    public float heroCenterX() {
        return 7.5f;
    }

    private float abilityRowY() {
        if (Obtuse.ratio < 1f) return 6f / Obtuse.ratio;
        return equipmentRowY() + rightGap();
    }

    private float heroRowY() {
        if (Obtuse.ratio < 1f) return 7.5f / Obtuse.ratio;
        return abilityRowY() + rightGap();
    }

    // World-unit rects [x, y, w, h] of the three grouped areas: the inventory grid, the ability-orb
    // ("Moves") row, and the equipment ("Items") row. The InfoStage reads these to place titles.
    public float[] gridArea() {
        float pad = 0.14f;
        return new float[]{gridLeftX - pad, gridBottomY() - pad, gridCols * cellW() + 2 * pad, gridRows * cellH() + 2 * pad};
    }

    // Extra room below the icon row so the item name (drawn under the icon) fits inside the frame.
    public float[] movesArea() {
        float padX = 0.24f, padTop = 0.24f, padBottom = 0.95f;
        float left = abilityOrbX[0], right = abilityOrbX[abilityOrbX.length - 1] + size, y = abilityRowY();
        return new float[]{left - padX, y - padBottom, (right - left) + 2 * padX, size + padTop + padBottom};
    }

    public float[] itemsArea() {
        float padX = 0.24f, padTop = 0.24f, padBottom = 0.95f;
        float left = equipmentX[0], right = equipmentX[equipmentX.length - 1] + size, y = equipmentRowY();
        return new float[]{left - padX, y - padBottom, (right - left) + 2 * padX, size + padTop + padBottom};
    }

    public void clearArrows() {
        for (SimpleArrow arrow : arrows)
            arrow.remove();
        arrows.clear();
    }

    // Arrows are gone: heroes are picked by tapping them directly (see placeHeroes / heroSlotRect).
    public void makeArrows() {
    }

    private void addArrow(SimpleArrow arrow, float x, float y, float width, float height) {
        arrow.create(x, y, width, height);
        arrows.add(arrow);
        add(arrow);
    }

    // Place the whole party in a row and set the selected one big + forward, everyone else small + in
    // line. Actors are added once (and their intrinsic sizes captured) so re-layout just repositions.
    public void placeHeroes(Array<Hero> party, int selected) {
        if (heroes.size == 0)
            for (Hero hero : party) {
                heroes.add(hero);
                baseW.add(hero.getWidth());
                baseH.add(hero.getHeight());
                add(hero);
            }
        selectedIndex = selected;
        for (int i = 0; i < heroes.size; i++)
            applyHeroState(i, i == selected, false);
    }

    // Walk the newly-selected hero forward and the previously-selected one back into line.
    public void animateSelect(int selected) {
        selectedIndex = selected;
        for (int i = 0; i < heroes.size; i++)
            applyHeroState(i, i == selected, true);
    }

    private void applyHeroState(int i, boolean selected, boolean animate) {
        Hero hero = heroes.get(i);
        float hgt = selected ? SELECTED_H : LINE_H;
        float wdt = hgt * (baseW.get(i) / baseH.get(i)); // keep the hero's own aspect
        float x = heroCenterXFor(i) - wdt / 2f;
        float y = heroRowY() - (selected ? FORWARD_STEP : 0f); // forward = lower (drawn on top)
        hero.clearActions();
        if (animate)
            hero.addAction(Actions.parallel(Actions.sizeTo(wdt, hgt, 0.22f), Actions.moveTo(x, y, 0.22f)));
        else {
            hero.setSize(wdt, hgt);
            hero.setPosition(x, y);
        }
        hero.play("default", 0);
    }

    private float heroPitch() {
        int n = heroes.size;
        return n > 1 ? Math.min(2.8f, 6f / n) : 0f;
    }

    private float heroCenterXFor(int i) {
        int n = heroes.size;
        return 7.5f + (i - (n - 1) / 2f) * heroPitch();
    }

    // World rect [x, y, w, h] of hero i's tap area (a column a little wider/taller than the sprite).
    public float[] heroSlotRect(int i) {
        float p = heroPitch();
        float cw = p > 0 ? p * 0.92f : 3f;
        float ch = SELECTED_H + 0.6f;
        return new float[]{heroCenterXFor(i) - cw / 2f, heroRowY() - FORWARD_STEP, cw, ch};
    }

    // Anchor for the selected hero's name tag: centred over its head.
    public float selectedCenterXWorld() {
        return heroes.size == 0 ? 7.5f : heroCenterXFor(selectedIndex);
    }

    public float selectedTopYWorld() {
        return heroRowY() - FORWARD_STEP + SELECTED_H;
    }

    public void setupInventory() {
        float cw = cellW(), ch = cellH();
        int i = 0;
        int j = 0;
        for (Item item : Inventory.items) {
            if (i >= gridCols) {
                i = 0;
                j++;
            }
            if (j >= gridRows)
                break;
            // Icon in the upper part of the cell; its name label sits below it, inside the frame.
            item.create(colLeft(i) + (cw - size) / 2f, rowBottom(j) + ch - size - ch * 0.12f, size, size);
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
