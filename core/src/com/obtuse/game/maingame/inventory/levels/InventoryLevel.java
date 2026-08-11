package com.obtuse.game.maingame.inventory.levels;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.buttons.SquareButton;
import com.obtuse.game.gameobjects.UI.SimpleArrow;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Party;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.gameobjects.items.AbilityOrb;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.gameobjects.items.Inventory;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.Level;
import com.obtuse.game.maingame.inventory.levels.stages.BackgroundStage;
import com.obtuse.game.maingame.inventory.levels.stages.InfoStage;
import com.obtuse.game.maingame.inventory.levels.stages.InventoryStage;
import com.obtuse.game.screens.MyScreen;

public class InventoryLevel extends Level {
    public Array<GameButton> inventoryButtons = new Array<GameButton>();
    public Array<GameButton> heroButtons = new Array<GameButton>();
    public Array<GameButton> arrowButtons = new Array<GameButton>();
    public Hero hero;
    public int index = 0;

    public InventoryLevel(MyScreen screen) {
        super(screen);
        add(new BackgroundStage(stage(0)));
        add(new InventoryStage(stage(0)));
        add(new InfoStage(stage(1)));
    }

    public void setup() {
        ((InventoryStage) stages.get(1)).setup(hero);
        ((InfoStage) stages.get(2)).setItemLabels(hero);
        ((InfoStage) stages.get(2)).setupAreaTitles((InventoryStage) stages.get(1));
        setInventoryButtons();
        setHeroButtons();
        setArrowButtons();
    }

    public void changeHero(int indexChange) {
        ((InfoStage) stages.get(2)).clearAdditionalLabels();
        if (hero != null)
            hero.remove();
        index += indexChange;
        while (index < 0)
            index += Party.party.size;
        Hero hero = Party.party.get(index % Party.party.size);
        this.hero = hero;
        ((InventoryStage) stages.get(1)).makeHero(hero);
        ((InfoStage) stages.get(2)).addHeroName(hero);
        setup();
    }

    public void equip(Item item) {
        e : {
            if (item instanceof AbilityOrb) {
                if (hero.abilityOrbs.size >= 4)
                    break e;
                hero.abilityOrbs.add((AbilityOrb) item);
            } else if (item instanceof Equipment) {
                if (hero.equipment.size >= 3)
                    break e;
                hero.equipment.add((Equipment) item);
            }
            Inventory.items.removeValue(item, true);
        }
    }

    public void unequip(int index) {
        if (Inventory.items.size < Inventory.maxSize) {
            Inventory.add(getHeroItem(index));
            if (index < hero.abilityOrbs.size)
                hero.abilityOrbs.removeIndex(index);
            else
                hero.equipment.removeIndex(index - hero.abilityOrbs.size);
        }
    }

    public void gatherInfo(Item item) {
        ((InfoStage) stages.get(2)).setup(item);
    }

    public void loseInfo() {
        ((InfoStage) stages.get(2)).clearInfoLabels();
    }

    public void setArrowButtons() {
        arrowButtons.clear();
        for (SimpleArrow arrow : ((InventoryStage) stages.get(1)).arrows)
            arrowButtons.add(new SquareButton(arrow.getX(), arrow.getY(), arrow.getWidth(), arrow.getHeight()));
    }

    public Item getHeroItem(int index) {
        if (index < hero.abilityOrbs.size)
            return hero.abilityOrbs.get(index);
        else
            return hero.equipment.get(index - hero.abilityOrbs.size);
    }

    public void setInventoryButtons() {
        inventoryButtons.clear();
        for (Item item : Inventory.items)
            addButton(inventoryButtons, item);
    }

    public void setHeroButtons() {
        heroButtons.clear();
        for (Item item : hero.abilityOrbs)
            addButton(heroButtons, item);
        for (Item item : hero.equipment)
            addButton(heroButtons, item);
    }

    public void addButton(Array<GameButton> buttonArray, Item item) {
        buttonArray.add(new SquareButton(item.getX(), item.getY(), item.getWidth(), item.getHeight()));
    }

    @Override
    public void run() {

    }

    @Override
    public void basic() {

    }
}
