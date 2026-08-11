package com.obtuse.game.maingame.world.levels.level1.stages;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.obtuse.game.bodies.boxes.Box;
import com.obtuse.game.gameobjects.fight.enemies.Reaper;
import com.obtuse.game.gameobjects.items.abilityorbs.*;
import com.obtuse.game.gameobjects.items.equipment.*;
import com.obtuse.game.gameobjects.world.characters.Uros;
import com.obtuse.game.gameobjects.world.interactive.chests.WoodenChest;
import com.obtuse.game.gameobjects.world.interactive.enemies.*;
import com.obtuse.game.gameobjects.world.interactive.house.Door;
import com.obtuse.game.gameobjects.world.obstacles.house.Bed;
import com.obtuse.game.gameobjects.world.obstacles.house.Bookshelf;
import com.obtuse.game.gameobjects.world.obstacles.house.Clock;
import com.obtuse.game.gameobjects.world.obstacles.house.Closet;
import com.obtuse.game.gameobjects.world.obstacles.house.walls.UpFiller;
import com.obtuse.game.gameobjects.world.obstacles.house.walls.Wall;
import com.obtuse.game.gameobjects.world.staticObjects.background.Floor;
import com.obtuse.game.gameobjects.world.staticObjects.walls.WoodenWall;
import com.obtuse.game.maingame.world.WorldStage;

public class Stage1 extends WorldStage {

    public Stage1(Stage stage) {
        super(0,0,0,10,10, stage);
        addEnemy(new BasicSkeleton(0, 8));
        addEnemy(new BasicRedSkull(2,8));
        addEnemy(new BasicDemon(4,8));
        addEnemy(new BasicDemonCat(7,8));
        addEnemy(new BasicReaper(10,8));
    }

    @Override
    protected void createLights() {

    }

    @Override
    protected void createEnvironment() {
        add(new Floor(-2, -2, 5, 5));
        add(new Wall(-2,3));
        add(new UpFiller(0,4));
        add(new Wall(1,3));
        add(new WoodenWall(-2 - 2f / 16,-2, 2f / 16, 7));
        add(new WoodenWall(3,-2, 2f / 16, 7));
        WoodenWall w = new WoodenWall(-2 - 2f / 16,-2 - 2f / 16, 5 + 4f / 16, 2f / 16);
        w.setBody(new Box(w.getX(), w.getY() - 1f + 4f / 16, BodyDef.BodyType.StaticBody, w.getWidth(), w.getHeight() + 1f - 4f / 16));
        add(w);
        add(new Bookshelf(2,0));

        WoodenChest chest = new WoodenChest(1, 3 - 4f / 16);
        chest.add(new BurningGroundOrb(), new DelayedHitOrb(), new SwapOrb());
        add(chest);
        /*WoodenChest chest4 = new WoodenChest(1.5f, 2);
        chest4.add(new FlamingSwordOrb(), new StealOrb(), new ReverseOrb());
        add(chest4);*/

        add(new Door(0,3));
        add(new Clock(-2,3 - 4f / 16));
        add(new Closet(2,3 - 4f / 16));
        add(new Bed(-2,-2 + 4f / 16));

        WoodenChest chest2 = new WoodenChest(-2, -2);
        chest2.add(new FireBallOrb(), new FlamingSwordOrb(), new TabulaRasaOrb());
        add(chest2);

        WoodenChest chest3 = new WoodenChest(2f, -1);
        chest3.add(new HealingStaff(), new GlovesOfDamage(), new BootsOfSpeed());
        add(chest3);

        WoodenChest chest4 = new WoodenChest(2f, 0.35f);
        chest4.add(new SummoningHorn(), new Seashell(), new CrownOfThorns());
        add(chest4);

        add(new Uros(2, 2));
    }
}
