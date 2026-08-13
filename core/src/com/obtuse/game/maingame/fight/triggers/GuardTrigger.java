package com.obtuse.game.maingame.fight.triggers;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class GuardTrigger extends Trigger {
    public FightObject caster;
    public FightObject target;
    public int damageGuarded;
    public int damage = 0;
    public FightLevel level;

    public GuardTrigger(FightObject caster, FightObject target, int damageGuarded, FightLevel level) {
        this.caster = caster;
        this.target = target;
        this.damageGuarded = damageGuarded;
        this.level = level;
    }

    @Override
    public boolean check(Event event) {
        if (caster.holder.arena == target.holder.arena)
            if (event.getClass() == Damage.class)
                if (((Damage) event).taker == target) {
                    if (((Damage) event).damage >= damageGuarded) {
                        ((Damage) event).damage -= damageGuarded;
                        damage = damageGuarded;
                    } else {
                        damage = ((Damage) event).damage;
                        ((Damage) event).damage = 0;
                    }
                    return true;
                }
        return false;
    }

    @Override
    public void run(Event event) {
        Damage damage = new Damage(this.damage, caster, caster).cause("fell shielding an ally.");
        Turn.sleep(damage.run());
    }
}
