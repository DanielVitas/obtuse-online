package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Summon;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.arenas.DuelArena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class DuelEvent extends Event {
    public FightObject caster;
    public FightObject target;
    public FightLevel level;

    public DuelEvent(FightObject caster, FightObject target, FightLevel level) {
        super(0,1);
        this.caster = caster;
        this.target = target;
        this.level = level;
    }

    @Override
    protected float run() {
        Hero hero;
        Enemy enemy;
        if (caster.getClass().getSuperclass() == Hero.class) {
            hero = (Hero) caster;
            enemy = (Enemy) target;
        } else {
            hero = (Hero) target;
            enemy = (Enemy) caster;
        }
        int i = 1;
        Arena originalArena = caster.holder.arena;
        // Capture the two vacated slots BEFORE remove()/the duel arena reassign each fighter's .holder,
        // so the duel can reserve them (chain) and return both fighters to them when it ends.
        Holder heroHolder = hero.holder;
        Holder enemyHolder = enemy.holder;
        if (originalArena.fighterOrder.indexOf(target,true) < originalArena.fighterOrder.indexOf(caster,true))
            i += 1;
        originalArena.remove(caster);
        originalArena.remove(target);
        heroHolder.lock();
        enemyHolder.lock();
        Arena duelArena = new DuelArena(originalArena.fightGame, hero, enemy, originalArena, heroHolder, enemyHolder);
        originalArena.dependOnArenas.add(duelArena);
        originalArena.fightGame.arenas.add(duelArena);
        originalArena.index -= i;
        // The turn bar was built at the START of this round, before the duel existed, so the two
        // duellists dropped off it until next round. Compute the duel's order now and redraw the bar
        // so both arenas' fighters show together from THIS turn.
        duelArena.preorder();
        originalArena.refreshProfiles();
        // Apply the duel "distant arena" view now (zoom the active arena, show the other in the corner)
        // rather than waiting for the next switchArena.
        ((FightLevel) originalArena.fightGame.level).updateDuelView(originalArena);
        //((FightLevel) caster.holder.arena.fightGame.level).switchArena(originalArena);
        return 0;
    }
}
