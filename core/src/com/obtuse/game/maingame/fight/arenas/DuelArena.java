package com.obtuse.game.maingame.fight.arenas;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.FightGame;

public class DuelArena extends Arena {
    public Arena originalArena;
    public boolean finishedFirst = false;
    // The slots the two duellists left behind in the main arena; both return here when the duel ends.
    public com.obtuse.game.gameobjects.fight.holders.fightObject.Holder heroOriginalHolder, enemyOriginalHolder;

    public DuelArena(FightGame fightGame, Hero hero, Enemy enemy, Arena originalArena,
                     com.obtuse.game.gameobjects.fight.holders.fightObject.Holder heroOriginalHolder,
                     com.obtuse.game.gameobjects.fight.holders.fightObject.Holder enemyOriginalHolder) {
        super(fightGame);
        clear();
        heroes.add(hero);
        enemies.add(enemy);
        this.originalArena = originalArena;
        this.heroOriginalHolder = heroOriginalHolder;
        this.enemyOriginalHolder = enemyOriginalHolder;
        setupHolderFightObjects();
    }

    // The two slots sit close together in the middle (enemy left, hero right) rather than at the far
    // edges, so the duel reads as an intimate one-on-one.
    @Override
    protected float[] setHeroPositions() {
        return new float[]{
                6.5f, Obtuse.cameraWidth / Obtuse.ratio * 0.5f
        };
    }

    @Override
    protected float[] setEnemyPositions() {
        return new float[]{
                3.5f, Obtuse.cameraWidth / Obtuse.ratio * 0.5f
        };
    }

    @Override
    protected boolean end() {
        boolean e = super.end();   // marks the duel finished, so it drops out of the corner view
        if (!e && !finishedFirst) {
            finishedFirst = true;
            // Return BOTH the survivor and the corpse to the exact slots they left (unlocking the chain).
            returnFighter(heroes.get(0), heroOriginalHolder);
            returnFighter(enemies.get(0), enemyOriginalHolder);
            // Glide the view back to the (now full-screen) main arena — the finished duel is gone from
            // the corner — then teleport the returned fighters INTO the main arena.
            ((com.obtuse.game.maingame.fight.levels.FightLevel) fightGame.level).switchArena(originalArena);
            playReturnAnimation();
        }
        // The battle-over check (FightGame.check, on allHeroes/allEnemies) runs right after this round's
        // conduct — so if the duel death left one side wiped, victory/defeat resolves there.
        return e;
    }

    /** Put a duellist back on its reserved home slot (unlocking the chain). If that slot was taken in
     *  the meantime (e.g. an ally Swapped into it), fall back to the first free slot. */
    private void returnFighter(FightObject fighter, com.obtuse.game.gameobjects.fight.holders.fightObject.Holder home) {
        if (home != null) {
            home.unlock();
            if (home.fightObject == null) {
                originalArena.add(fighter, home);
                return;
            }
        }
        originalArena.addNext(fighter);
    }

    /** Replay the Duel teleport on the duellists — now in the MAIN arena, as they arrive back. */
    private void playReturnAnimation() {
        com.obtuse.game.abilities.all.Duel duel = new com.obtuse.game.abilities.all.Duel();
        com.obtuse.game.audio.SoundPlayer.play("fight/abilities/duel/teleportation");
        float wait = 0;
        for (FightObject fighter : new FightObject[]{heroes.get(0), enemies.get(0)})
            if (fighter != null)
                wait = Math.max(wait, duel.play(fighter, "teleportation"));
        if (wait > 0)
            com.obtuse.game.maingame.fight.Turn.sleep(wait);
    }

    @Override
    protected float[] setSummonPositions() {
        return new float[0];
    }

    @Override
    protected void lose() {

    }

    @Override
    protected void win() {

    }
}
