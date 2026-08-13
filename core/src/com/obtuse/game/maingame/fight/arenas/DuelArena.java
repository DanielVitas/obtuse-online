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

    public DuelArena(FightGame fightGame, Hero hero, Enemy enemy, Arena originalArena) {
        super(fightGame);
        clear();
        heroes.add(hero);
        enemies.add(enemy);
        this.originalArena = originalArena;
        setupHolderFightObjects();
    }

    // The two slots sit close together in the middle (enemy left, hero right) rather than at the far
    // edges, so the duel reads as an intimate one-on-one.
    @Override
    protected float[] setHeroPositions() {
        return new float[]{
                6f, Obtuse.cameraWidth / Obtuse.ratio * 0.5f
        };
    }

    @Override
    protected float[] setEnemyPositions() {
        return new float[]{
                4f, Obtuse.cameraWidth / Obtuse.ratio * 0.5f
        };
    }

    @Override
    protected boolean end() {
        boolean e = super.end();
        if (!e && !finishedFirst) {
            playReturnAnimation();   // teleport the duellists out (mirror of the cast) before they rejoin the main arena
            originalArena.addNext(heroes.get(0));
            originalArena.addNext(enemies.get(0));
            finishedFirst = true;
        }
        return e;
    }

    /** Replay the Duel teleport on the duellists, still in the duel arena, before they return to main. */
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
