package com.obtuse.game.maingame.fight.levels.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Fonts;
import com.obtuse.game.Obtuse;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.abilities.all.SkipTurn;
import com.obtuse.game.gameobjects.UI.Border;
import com.obtuse.game.gameobjects.UI.InfoBackground;
import com.obtuse.game.gameobjects.UI.info.AbilityInfoBackground;
import com.obtuse.game.gameobjects.UI.info.BasicInfoBackground;
import com.obtuse.game.gameobjects.UI.info.GeneralInfoBackground;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Summon;
import com.obtuse.game.gameobjects.fight.buffs.Status;
import com.obtuse.game.gameobjects.fight.holders.ability.AbilityBackground;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.GameStage;
import com.obtuse.game.maingame.fight.Event;

import static com.obtuse.game.Obtuse.*;

public class InfoStage extends GameStage {
    public static float defaultAbilityWidth;
    public static float defaultAbilityHeight;
    // x, y pairs as fractions of the screen: four ability boxes in a 2x2 block on the
    // right, then the Pass box bottom left. Taller boxes than 2018 so a thumb can hit
    // them and the (now much larger) ability names still fit inside.
    public static float[] abilityPosition = {
            0.345f, 0.195f,
            0.665f, 0.195f,
            0.345f, 0.025f,
            0.665f, 0.025f,
            0.020f, 0.025f
    };

    static {
        refreshMetrics();
    }

    public static void refreshMetrics() {
        defaultAbilityWidth = w(0.31f);
        defaultAbilityHeight = h(0.155f);
    }
    private Array<Label> infoLabels = new Array<Label>();
    public Array<Label> abilitySelectLabels = new Array<Label>();
    public Array<AbilityBackground> abilityBackgrounds = new Array<AbilityBackground>();
    private Array<Border> abilityBorders = new Array<Border>();
    private InfoBackground infoBackground;

    public InfoStage(Stage stage) {
        super(stage);
    }

    public void choice(FightObject caster) {
        for (int i = 0; i < caster.abilities.size; i++) {
            Label name = new Label(caster.abilities.get(i).getName(), Fonts.get("fightInfoTable"));
            name.setWidth(defaultAbilityWidth);
            name.setHeight(defaultAbilityHeight);
            name.setWrap(true);
            name.setAlignment(Align.center);
            name.setPosition(w(abilityPosition[2 * i]), h(abilityPosition[2 * i + 1]));
            addAbilitySelectLabel(name);
        }
    }

    private void createGeneralBackground() {
        infoBackground = new GeneralInfoBackground();
        infoBackground.create(w(abilityPosition[8]), h(abilityPosition[9]), w(0.3f), h(0.225f));
        infoBackground.positionAtPointer(stage);
        add(infoBackground);
    }

    private void createInfoBackground(Holder holder) {
        infoBackground = new BasicInfoBackground();
        if (holder.type == 0 || holder.type == 2) {
            infoBackground.create(holder.getX() / cameraWidth * w(1) - infoBackground.getWidth(),
                    holder.getY() / cameraWidth * Obtuse.ratio * h(1) + holder.slot.getHeight() / 2 / cameraWidth * ratio * h(1));
        }
        if (holder.type == 1) {
            infoBackground.create(holder.getX() / cameraWidth * w(1) + holder.getWidth() / cameraWidth * w(1),
                    holder.getY() / cameraWidth * Obtuse.ratio * h(1) + holder.slot.getHeight() / 2 / cameraWidth * ratio * h(1));
        }
        add(infoBackground);
    }

    private void createAbilityInfoBackground(AbilityInstance ability) {
        if (!(ability.ability instanceof SkipTurn)) {
            infoBackground = new AbilityInfoBackground();
            if (ability.holder.getX() <= w(0.5f))
                infoBackground.create(ability.holder.abilityBackground.getX() + ability.holder.abilityBackground.getWidth(),
                        ability.holder.abilityBackground.getY() + ability.holder.abilityBackground.getHeight() / 3);
            else
                infoBackground.create(ability.holder.abilityBackground.getX() - infoBackground.getWidth(),
                        ability.holder.abilityBackground.getY() + ability.holder.abilityBackground.getHeight() / 3);
            add(infoBackground);
        }
    }

    public void setup(Holder holder) {
        createGeneralBackground();
        int index = 0;

        if (holder.fightObject != null) {
            Label name = new Label(holder.fightObject.getName(), Fonts.get("fightInfoTableTitle"));
            name.setWidth(infoBackground.getWidth());
            name.setWrap(true);
            name.setAlignment(Align.bottom);
            name.setPosition(infoBackground.getX() + infoBackground.getWidth() / 2 - name.getWidth() / 2,
                    infoBackground.getY() + infoBackground.getHeight());
            addInfoLabel(name);


            Array<Status> statuses = holder.fightObject.getStatuses();
            if (holder.fightObject.alive())
                while (index < statuses.size) {
                    Label buffLabel = new Label(statuses.get(index).getName(), Fonts.get("fightInfoTableContent"));
                    buffLabel.setWidth(w(0.090f));
                    buffLabel.setPosition(infoBackground.getX() + w(0.0025f),
                            infoBackground.getY() + infoBackground.getHeight() - buffLabel.getHeight() * (index + 1) - h(0.0025f));
                    addInfoLabel(buffLabel);
                    index += 1;
                }

            Label hp = new Label(Integer.toString(holder.fightObject.hp - holder.fightObject.damageTaken) + " /"
                    + Integer.toString(holder.fightObject.hp) + " HP", Fonts.get("fightInfoTableContent"));
            hp.setWidth(infoBackground.getWidth());
            hp.setAlignment(Align.right);
            hp.setPosition(infoBackground.getX(), infoBackground.getY());
            addInfoLabel(hp);

            Label speed = new Label(Integer.toString(holder.fightObject.speed) + " SPD", Fonts.get("fightInfoTableContent"));
            speed.setWidth(infoBackground.getWidth());
            speed.setPosition(infoBackground.getX() + w(0.01f), infoBackground.getY());
            addInfoLabel(speed);
        }

        if (holder.burning != 0) {
            Label burning = new Label("Burning: " + holder.burning, Fonts.get("fightInfoTableContent"));
            burning.setWidth(w(0.090f));
            burning.setPosition(infoBackground.getX() +  w(0.0025f),
                    infoBackground.getY() + infoBackground.getHeight() - burning.getHeight() * (index + 1) - h(0.0025f));
            addInfoLabel(burning);
            index += 1;
        }
    }

    public void setup(AbilityInstance ability) {
        if (!(ability.ability instanceof SkipTurn)) {
            createGeneralBackground();

            Label name = new Label(ability.getName(), Fonts.get("fightInfoTableTitle"));
            name.setWidth(infoBackground.getWidth());
            name.setWrap(true);
            name.setAlignment(Align.bottom);
            name.setPosition(infoBackground.getX() + infoBackground.getWidth() / 2 - name.getWidth() / 2,
                    infoBackground.getY() + infoBackground.getHeight());
            addInfoLabel(name);

            Label pp = new Label(Integer.toString(ability.pp - ability.ppUsed) + " /" + Integer.toString(ability.pp)
                    + " PP", Fonts.get("fightInfoTableContent"));
            pp.setWidth(infoBackground.getWidth());
            pp.setAlignment(Align.right);
            pp.setPosition(infoBackground.getX(),infoBackground.getY());
            addInfoLabel(pp);

            Label description = new Label(ability.getDescription(), Fonts.get("fightInfoTableDescription"));
            description.setWidth(infoBackground.getWidth() - w(0.00f));
            description.setWrap(true);
            description.setAlignment(Align.topLeft);
            description.setPosition(infoBackground.getX() +  w(0.0025f),
                    infoBackground.getY()  + infoBackground.getHeight() - description.getHeight() - h(0.0025f));
            addInfoLabel(description);
        }
    }

    public void addHPLabel(Holder holder) {
        if (holder.hpLabel != null) {
            holder.hpLabel.setPosition(holder.getX() / 10 * w(1), holder.getY() / 10 * Obtuse.ratio * h(1));
            holder.hpLabel.setX(holder.hpLabel.getX() + w(0.08f));
            holder.hpLabel.setY(holder.hpLabel.getY() + h(0.0f));
            holder.hpLabel.setSize(1, 1);
            add(holder.hpLabel);
        }
        add(holder.damageLabel);
    }

    public void addInfoLabel(Label label) {
        infoLabels.add(label);
        add(label);
    }

    public void addAbilitySelectLabel(Label label) {
        abilitySelectLabels.add(label);
    }

    public void installAbilitySelectLabels() {
        for (Label label : abilitySelectLabels)
            add(label);
    }

    public void addAbilityBackground(AbilityBackground abilityBackground) {
        abilityBackgrounds.add(abilityBackground);
        add(abilityBackground);
        // Gold frame around each selectable move box.
        float t = Math.min(abilityBackground.getWidth(), abilityBackground.getHeight()) * 0.045f;
        Border border = new Border(abilityBackground.getX(), abilityBackground.getY(),
                abilityBackground.getWidth(), abilityBackground.getHeight(), t);
        abilityBorders.add(border);
        add(border);
    }

    public void clearInfoLabels() {
        for (Label label : infoLabels)
            label.addAction(Actions.removeActor());
        if (infoBackground != null)
            infoBackground.remove();
        infoLabels.clear();
        infoBackground = null;
    }

    public void clearAbilitySelectLabels() {
        for (Label label : abilitySelectLabels)
            label.remove();
        for (AbilityBackground abilityBackground : abilityBackgrounds)
            abilityBackground.remove();
        for (Border border : abilityBorders)
            border.remove();
        abilitySelectLabels.clear();
        abilityBackgrounds.clear();
        abilityBorders.clear();
    }

    @Override
    protected void createLights() {

    }

    @Override
    protected void createEnvironment() {

    }
}
