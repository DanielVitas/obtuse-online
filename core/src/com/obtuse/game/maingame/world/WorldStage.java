package com.obtuse.game.maingame.world;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.gameobjects.world.interactive.WorldEnemy;
import com.obtuse.game.maingame.GameStage;

public abstract class WorldStage extends GameStage {
    public int area;
    public float x;
    public float y;
    private float width;
    private float height;
    private Array<WorldEnemy> enemies = new Array<WorldEnemy>();

    public WorldStage(int area, float x, float y, float width, float height, Stage stage) {
        super(stage);
        this.area = area;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addEnemy(WorldEnemy enemy) {
        enemies.add(enemy);
    }

    public void setupEnemies() {
        for (WorldEnemy enemy : enemies)
            if (enemy.active)
                add(enemy);
    }

    public boolean check(float x, float y) {
        if (this.x <= x && x <= this.x + width)
            if (this.y <= y && y <= this.y + height)
                return true;
        return false;
    }

    @Override
    public void create() {
        super.create();
        setupEnemies();
    }
}
