package com.obtuse.game.maingame.loot.levels.chest;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.obtuse.game.Fonts;
import com.obtuse.game.buttons.GameButton;
import com.obtuse.game.buttons.SquareButton;
import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.gameobjects.items.Item;
import com.obtuse.game.maingame.loot.LootLevel;
import com.obtuse.game.maingame.loot.levels.chest.stages.DefaultChestStage;
import com.obtuse.game.maingame.loot.levels.chest.stages.DefaultInfoStage;
import com.obtuse.game.screens.MyScreen;

import static com.obtuse.game.Fonts.get;
import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.ratio;
import static com.obtuse.game.Obtuse.w;

public class ChestLootLevel extends LootLevel {

    public ChestLootLevel(MyScreen screen) {
        super(screen);
        add(new DefaultChestStage(stage(0)));
        add(new DefaultInfoStage(stage(1)));
    }

    private void labelAbove(Choice choice, int index) {
        ((DefaultInfoStage) stages.get(1)).labelAbove(buttons.get(index), choice.items.get(index));
    }

    private void labelBelow(Choice choice, int index) {
        ((DefaultInfoStage) stages.get(1)).labelBelow(buttons.get(index), choice.items.get(index));
    }

    @Override
    public void gatherInfo(Item item) {
        ((DefaultInfoStage) stages.get(1)).setup(item);
    }

    @Override
    public void loseInfo() {
        ((DefaultInfoStage) stages.get(1)).clearLabels();
    }

    @Override
    public void setup(Choice choice) {
        buttons.clear();
        // These used to be the constants 4 and 4.5, which sat comfortably inside the
        // 8.1 world units the 640x520 desktop window showed. A 20:9 phone only shows
        // about 4.5 units of height, so those slots landed above the top edge and the
        // items were invisible. Derive them from what the camera can actually see.
        float visible = camera(0).viewportHeight;
        float high = visible * 0.56f;
        float low = visible * 0.48f;
        switch (choice.items.size) {
            case 1:
                buttons.add(new SquareButton(4.5f, low, 1, 1));
                labelBelow(choice,0);
                break;
            case 2:
                buttons.add(new SquareButton(2.5f, high, 1, 1));
                labelAbove(choice,0);
                buttons.add(new SquareButton(6.5f, high, 1, 1));
                labelAbove(choice,1);
                break;
            case 3:
                buttons.add(new SquareButton(2.5f, high, 1, 1));
                labelAbove(choice,0);
                buttons.add(new SquareButton(4.5f, low, 1, 1));
                labelBelow(choice,1);
                buttons.add(new SquareButton(6.5f, high, 1, 1));
                labelAbove(choice,2);
                break;
        }
        for (int i = 0; i < choice.items.size; i++) {
            choice.items.get(i).create(buttons.get(i).getX(), buttons.get(i).getY(),
                    buttons.get(i).getHeight(), buttons.get(i).getWidth());
            stage(0).addActor(choice.items.get(i));
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void basic() {
    }
}
