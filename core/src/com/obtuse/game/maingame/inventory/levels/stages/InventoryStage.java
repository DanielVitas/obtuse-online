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
    private static float size = 0.5f;
    private static float[] columns = {0.5f, 1.5f, 2.5f, 3.5f};
    private static float[] rows = {6.5f, 5f, 3.5f, 2f, 0.5f};
    private static float[] abilityOrbLocation = {
            5.75f, 6f / Obtuse.ratio,
            6.75f, 6f / Obtuse.ratio,
            7.75f, 6f / Obtuse.ratio,
            8.75f, 6f / Obtuse.ratio
    };
    private static float[] equipmentLocation = {
            6.25f, 4.5f / Obtuse.ratio,
            7.25f, 4.5f / Obtuse.ratio,
            8.25f, 4.5f / Obtuse.ratio
    };
    private Array<Item> created = new Array<Item>();
    public Array<SimpleArrow> arrows = new Array<SimpleArrow>();

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

    public void clearArrows() {
        for (SimpleArrow arrow : arrows)
            arrow.remove();
        arrows.clear();
    }

    public void makeArrows() {
        addArrow(new LeftArrow(), 5.5f, 7.5f / Obtuse.ratio, 0.5f, 1f);
        addArrow(new RightArrow(), 9f, 7.5f / Obtuse.ratio, 0.5f, 1f);
    }

    private void addArrow(SimpleArrow arrow, float x, float y, float width, float height) {
        arrow.create(x, y, width, height);
        arrows.add(arrow);
        add(arrow);
    }

    public void makeHero(Hero hero) {
        hero.create(7.5f - hero.getWidth() / 2,7.5f / Obtuse.ratio);
        hero.play("summon");
        add(hero);
    }

    public void setupInventory() {
        int i = 0;
        int j = 0;
        for (Item item : Inventory.items) {
            if (i >= columns.length) {
                i = 0;
                j++;
            }
            item.create(columns[i], rows[j], size, size);
            addItem(item);
            i++;
        }
    }

    public void setupAbilityOrbs(Hero hero) {
        for (int i = 0; i < hero.abilityOrbs.size; i++) {
            hero.abilityOrbs.get(i).create(abilityOrbLocation[2 * i], abilityOrbLocation[2 * i + 1], size, size);
            addItem(hero.abilityOrbs.get(i));
        }
    }

    public void setupEquipment(Hero hero) {
        for (int i = 0; i < hero.equipment.size; i++) {
            hero.equipment.get(i).create(equipmentLocation[2 * i], equipmentLocation[2 * i + 1], size, size);
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
