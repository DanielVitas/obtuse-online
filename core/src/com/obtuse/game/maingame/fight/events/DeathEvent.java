package com.obtuse.game.maingame.fight.events;

import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.arenas.DuelArena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

import java.util.Random;

public class DeathEvent extends Event {
    public FightObject dealer;
    public FightObject target;
    private String deathVerb;   // cause-of-death phrase, or null for a generic / contextual one

    private static final Random random = new Random();
    private static final String[] DEFAULTS = {
            "perished!", "has fallen!", "was slain!", "met their end.", "is no more."
    };

    public DeathEvent(FightObject dealer, FightObject target) {
        this(dealer, target, null);
    }

    public DeathEvent(FightObject dealer, FightObject target, String deathVerb) {
        super(0, 1);
        this.dealer = dealer;
        this.target = target;
        this.deathVerb = deathVerb;
    }

    @Override
    public float run() {
        if (target == null || target.holder == null || target.holder.arena == null)
            return 0;
        String verb = deathVerb;
        if (verb == null)   // no specific cause: a duel death gets its own line, otherwise a random default
            verb = target.holder.arena instanceof DuelArena ? "fell in the duel."
                    : DEFAULTS[random.nextInt(DEFAULTS.length)];
        ((FightLevel) target.holder.arena.fightGame.level).showDeath(target.getName() + " " + verb);
        return 1f;   // hold the message for a second before the fight moves on
    }
}
