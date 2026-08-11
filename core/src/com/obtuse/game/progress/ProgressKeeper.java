package com.obtuse.game.progress;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class ProgressKeeper {
    private static HashMap<String, Array<String>> log = new HashMap<String, Array<String>>();

    public static boolean done(String category, String actionDone) {
        if (log.containsKey(category))
            return log.get(category).contains(actionDone, true);
        return false;
    }

    public static void add(String category, String actionDone) {
        if (!log.containsKey(category))
            log.put(category, new Array<String>());
        if (!log.get(category).contains(actionDone,true))
            log.get(category).add(actionDone);
    }
}
