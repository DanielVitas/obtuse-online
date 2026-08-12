package com.obtuse.game.maingame.fight;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.audio.MusicPlayer;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Party;
import com.obtuse.game.gameobjects.items.Choice;
import com.obtuse.game.gameobjects.world.interactive.WorldEnemy;
import com.obtuse.game.maingame.fight.arenas.BossArena;
import com.obtuse.game.maingame.fight.arenas.MainArena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

import java.lang.reflect.InvocationTargetException;

public abstract class Fight {
    public Array<Enemy> enemies = new Array<Enemy>();
    public Choice rewards = new Choice();
    public String fightID = "";
    public String musicName;
    public Class arenaClass = MainArena.class;

    public Fight() {

    }

    public void id(String fightID) {
        this.fightID = fightID;
    }

    public abstract void end(boolean victory);

    public void setup(FightGame fightGame) {
        if (musicName != null)
            MusicPlayer.play(musicName);
        Arena.triggers.clear();
        fightGame.allEnemies = new Array<Enemy>(this.enemies);
        fightGame.allHeroes = new Array<Hero>(Party.party);
        for (Hero hero : fightGame.allHeroes)
            hero.reset();
        for (Enemy enemy : fightGame.allEnemies)
            enemy.reset();

        try {
            fightGame.arenas.add((Arena) arenaClass.getConstructor(FightGame.class, Array.class, Array.class).newInstance(fightGame, fightGame.allHeroes, fightGame.allEnemies));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        fightGame.mainArena = fightGame.arenas.get(0);
        ((FightLevel) fightGame.level).switchArena(fightGame.arenas.get(0));
        for (Hero hero : fightGame.allHeroes)
            hero.setupEquipment();
        for (Enemy enemy : fightGame.allEnemies)
            enemy.setupEquipment();
    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
    }

    public void reset() {

    }
}
