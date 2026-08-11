package com.obtuse.game.gameobjects.UI.info;

import com.obtuse.game.gameobjects.UI.InfoBackground;

import static com.obtuse.game.Obtuse.h;
import static com.obtuse.game.Obtuse.ratio;
import static com.obtuse.game.Obtuse.w;

public class BasicInfoBackground extends InfoBackground {

    public BasicInfoBackground() {
        super("basic", 1f);
        setSize(w(0.1f), w(0.15f));
    }
}
