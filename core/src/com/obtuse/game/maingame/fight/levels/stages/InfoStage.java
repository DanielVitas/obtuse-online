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
    private InfoBackground infoBackground;
    // The big "Description Display" panel that fills the 4-move area (shown while targeting and on the
    // enemy's turn), plus a Cancel button that replaces Pass while choosing a target.
    private GeneralInfoBackground ddBackground;
    private Label ddLabel;
    private GeneralInfoBackground cancelBackground;
    private Label cancelLabel;

    public InfoStage(Stage stage) {
        super(stage);
    }

    // World rect (screen pixels) of the 2x2 move block, used for the big description panel.
    private static float ddX() { return w(abilityPosition[0]); }
    private static float ddY() { return h(abilityPosition[5]); }
    private static float ddW() { return w(abilityPosition[2]) + defaultAbilityWidth - ddX(); }
    private static float ddH() { return h(abilityPosition[1]) + defaultAbilityHeight - ddY(); }

    /** Show the big description panel (over the 4-move area) with wrapped text. */
    public void showDescription(String text) {
        clearDescription();
        float x = ddX(), y = ddY(), bw = ddW(), bh = ddH();
        ddBackground = new GeneralInfoBackground();
        ddBackground.create(x, y, bw, bh);
        add(ddBackground);
        float pad = bw * 0.05f;
        ddLabel = new Label(text, Fonts.get("fightInfoTableContent"));
        ddLabel.setColor(com.obtuse.game.gameobjects.UI.Border.ROYAL_TEXT);
        ddLabel.setWrap(true);
        ddLabel.setAlignment(Align.topLeft);
        ddLabel.setBounds(x + pad, y + pad, bw - 2 * pad, bh - 2 * pad);
        add(ddLabel);
    }

    public void clearDescription() {
        if (ddBackground != null) { ddBackground.remove(); ddBackground = null; }
        if (ddLabel != null) { ddLabel.remove(); ddLabel = null; }
    }

    /** Cancel button in the Pass slot (bottom-left), shown while choosing a target. */
    public void showCancel() {
        clearCancel();
        float x = w(abilityPosition[8]), y = h(abilityPosition[9]);
        float bw = w(0.14f), bh = defaultAbilityHeight * 0.55f;
        cancelBackground = new GeneralInfoBackground();
        cancelBackground.create(x, y, bw, bh);
        add(cancelBackground);
        cancelLabel = new Label("Cancel", Fonts.get("fightInfoTable"));
        cancelLabel.setColor(com.obtuse.game.gameobjects.UI.Border.ROYAL_TEXT);
        cancelLabel.setBounds(x, y, bw, bh);
        cancelLabel.setAlignment(Align.center);
        add(cancelLabel);
    }

    public void clearCancel() {
        if (cancelBackground != null) { cancelBackground.remove(); cancelBackground = null; }
        if (cancelLabel != null) { cancelLabel.remove(); cancelLabel = null; }
    }

    public void choice(FightObject caster) {
        // Name centred (roughly the middle of the box); the PP sits up in the top-left corner.
        float mx = defaultAbilityWidth * 0.09f;
        for (int i = 0; i < caster.abilities.size; i++) {
            Label name = new Label(caster.abilities.get(i).getName(), Fonts.get("fightInfoTable"));
            name.setWidth(defaultAbilityWidth - 2 * mx);
            name.setHeight(defaultAbilityHeight);
            name.setWrap(true);
            name.setAlignment(Align.center);
            name.setPosition(w(abilityPosition[2 * i]) + mx, h(abilityPosition[2 * i + 1]));
            addAbilitySelectLabel(name);
        }
    }

    private void createGeneralBackground() {
        infoBackground = new GeneralInfoBackground();
        infoBackground.create(0, 0, w(0.32f), h(0.1f)); // base width; buildTooltip grows it taller for long text
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
        if (holder.fightObject == null)
            return;
        createGeneralBackground();
        FightObject fo = holder.fightObject;
        Label name = new Label(fo.getName(), Fonts.get("fightInfoTableTitle"));
        // HP and SPD together on the line below the name.
        Label hpSpd = new Label((fo.hp - fo.damageTaken) + " /" + fo.hp + " HP    " + fo.speed + " SPD",
                Fonts.get("fightInfoTableContent"));
        StringBuilder body = new StringBuilder();
        if (fo.alive())
            for (Status status : fo.getStatuses()) {
                if (body.length() > 0) body.append("\n");
                body.append(status.getName());
            }
        if (holder.burning != 0) {
            if (body.length() > 0) body.append("\n");
            body.append("Burning: ").append(holder.burning);
        }
        Label description = body.length() > 0
                ? new Label(body.toString(), Fonts.get("fightInfoTableContent")) : null;
        infoBackground.buildTooltip(stage, name, null, hpSpd, description);
        addInfoLabel(name);
        addInfoLabel(hpSpd);
        if (description != null)
            addInfoLabel(description);
    }

    public void setup(AbilityInstance ability, FightObject caster) {
        createGeneralBackground();
        Label name = new Label(ability.getName(), Fonts.get("fightInfoTableTitle"));
        // Pass (SkipTurn) has no PP line; regular abilities show remaining PP.
        Label pp = (ability.ability instanceof SkipTurn) ? null
                : new Label((ability.pp - ability.ppUsed) + " /" + ability.pp + " PP",
                        Fonts.get("fightInfoTableContent"));
        // Description with the damage recomputed for the caster's equipment (coloured if changed).
        Label description = new Label(ability.ability.describedFor(caster),
                Fonts.get("fightInfoTableDescription"));
        infoBackground.buildTooltip(stage, name, pp, null, description);
        addInfoLabel(name);
        if (pp != null)
            addInfoLabel(pp);
        addInfoLabel(description);
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
        add(abilityBackground); // draws its own royal-gold box
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
        abilitySelectLabels.clear();
        abilityBackgrounds.clear();
    }

    @Override
    protected void createLights() {

    }

    @Override
    protected void createEnvironment() {

    }
}
