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

    @Override
    protected float[] setHeroPositions() {
        return new float[]{
                8f, Obtuse.cameraWidth / Obtuse.ratio * 0.5f
        };
    }

    @Override
    protected float[] setEnemyPositions() {
        return new float[]{
                1f, Obtuse.cameraWidth / Obtuse.ratio * 0.5f
        };
    }

    @Override
    protected boolean end() {
        boolean e = super.end();
        if (!e && !finishedFirst) {
            originalArena.addNext(heroes.get(0));
            originalArena.addNext(enemies.get(0));
            finishedFirst = true;
        }
        return e;
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
