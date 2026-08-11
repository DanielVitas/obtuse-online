package com.obtuse.game.maingame.fight.arenas;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.FightGame;

public class BossArena extends Arena {

    public BossArena(FightGame fightGame, Array<Hero> heroes, Array<Enemy> enemies) {
        super(fightGame);
        clear();
        this.heroes.addAll(heroes);
        this.enemies.addAll(enemies);
        setupHolderFightObjects();
        for (int i = 0; i < summonHolders.size; i++)
            heroHolders.get(i).summonHolder = summonHolders.get(i);

        link(heroHolders.get(0), heroHolders.get(1));
        link(heroHolders.get(1), heroHolders.get(2));
        link(summonHolders.get(0), summonHolders.get(1));
        link(summonHolders.get(0), heroHolders.get(0));
        link(summonHolders.get(1), heroHolders.get(1));
        link(summonHolders.get(1), summonHolders.get(2));
        link(summonHolders.get(2), heroHolders.get(2));
    }

    @Override
    protected float[] setHeroPositions() {
        return new float[]{
                8f, Obtuse.cameraWidth / Obtuse.ratio * 0.7f,
                8f, Obtuse.cameraWidth / Obtuse.ratio * 0.55f,
                8f, Obtuse.cameraWidth / Obtuse.ratio * 0.4f
        };
    }

    @Override
    protected float[] setEnemyPositions() {
        return new float[]{
                2f, Obtuse.cameraWidth / Obtuse.ratio * 0.55f
        };
    }

    @Override
    protected float[] setSummonPositions() {
        return new float[]{
                6.5f, Obtuse.cameraWidth / Obtuse.ratio * 0.7f,
                6.5f, Obtuse.cameraWidth / Obtuse.ratio * 0.55f,
                6.5f, Obtuse.cameraWidth / Obtuse.ratio * 0.4f
        };
    }

    @Override
    protected void lose() {
        //System.out.println("lost");
    }

    @Override
    protected void win() {
        //System.out.println("won main");
    }
}
