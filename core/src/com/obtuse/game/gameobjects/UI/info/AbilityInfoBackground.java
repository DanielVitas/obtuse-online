package com.obtuse.game.gameobjects.UI.info;

import com.obtuse.game.gameobjects.UI.InfoBackground;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.ratio;
import static com.obtuse.game.Obtuse.w;

public class AbilityInfoBackground extends InfoBackground {

    public AbilityInfoBackground() {
        super("ability", 1f);
        setSize(w(0.3f), w(0.15f));
    }
}
