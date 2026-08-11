package com.obtuse.game.maingame.world.levels.level1;

import com.obtuse.game.gameobjects.world.characters.Wizard;
import com.obtuse.game.maingame.world.WorldLevel;
import com.obtuse.game.maingame.world.levels.level1.stages.Stage1;
import com.obtuse.game.maingame.world.levels.level1.stages.Stage2;
import com.obtuse.game.screens.MyScreen;

public class Level1 extends WorldLevel {

    public Level1(MyScreen screen) {
        super(screen);
        add(new Stage1(stage(0)));
        add(new Stage2(stage(0)));
    }

    @Override
    protected String[] installMusicNames() {
        return new String[] {
                "satieGnossienne", "verdiDiesIrae"
        };
    }

    @Override
    public void run() {
        super.run();
        camera(0).position.set(main.body.body.getWorldCenter().x, main.body.body.getWorldCenter().y,0);
    }

    @Override
    public void basic() {
        main = new Wizard(0, 0);
        stage(0).addActor(main);


        camera(0).position.set(main.body.body.getWorldCenter().x, main.body.body.getWorldCenter().y,0);
    }
}
