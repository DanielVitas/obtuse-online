package com.obtuse.game.maingame.fight.triggers;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.Trigger;
import com.obtuse.game.maingame.fight.events.DeathEvent;
import com.obtuse.game.maingame.fight.events.SummonEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;

/**
 * A slime that bursts into smaller slimes when it dies (Huge &rarr; 2 Big, each Big &rarr; 2 small).
 * <p>
 * The spawns are queued as sub-events of the parent's {@link DeathEvent} (exactly like
 * {@code ExplosionDeathTrigger}), so they register into {@code arena.enemies} / {@code allEnemies}
 * synchronously — before the turn's win-check runs. That way killing the last Huge/Big does NOT end
 * the fight: the newly spawned slimes keep it going. A spawned slime re-registers its own split
 * trigger via {@code setupEquipment()} in {@link SummonEvent}, so the cascade continues by itself.
 * <p>
 * One-shot: it removes itself after firing. {@code Arena.triggers} is cleared each fight setup, so it
 * never leaks. Subclass and implement {@link #child()} to choose what this slime splits into.
 */
public abstract class SlimeSplitTrigger extends Trigger {
    private final FightObject parent;
    private final int count;

    public SlimeSplitTrigger(FightObject parent, int count) {
        this.parent = parent;
        this.count = count;
    }

    /** A fresh instance of the smaller slime this one splits into. */
    protected abstract Enemy child();

    @Override
    public boolean check(Event event) {
        if (event.getClass() == DeathEvent.class)
            return ((DeathEvent) event).target == parent;
        return false;
    }

    @Override
    public void run(Event event) {
        if (parent.holder == null || parent.holder.arena == null) {
            remove();
            return;
        }
        Arena arena = parent.holder.arena;
        FightLevel level = (FightLevel) arena.fightGame.level;

        // Target holders, in preference order: the parent's own slot first (SummonEvent clears the
        // corpse), then any empty adjacent enemy slot, then any other empty enemy slot as a fallback.
        Array<Holder> targets = new Array<Holder>();
        targets.add(parent.holder);
        for (Holder holder : parent.holder.adjacent)
            if (isEmptyEnemySlot(holder) && !targets.contains(holder, true))
                targets.add(holder);
        for (Holder holder : arena.enemyHolders)
            if (isEmptyEnemySlot(holder) && !targets.contains(holder, true))
                targets.add(holder);

        int spawned = 0;
        for (int i = 0; i < targets.size && spawned < count; i++) {
            event.addSubEvent(new SummonEvent(parent, child(), targets.get(i), level), 0);
            spawned++;
        }
        remove();
    }

    private static boolean isEmptyEnemySlot(Holder holder) {
        return holder.type == 1 && (holder.fightObject == null || !holder.fightObject.alive());
    }
}
