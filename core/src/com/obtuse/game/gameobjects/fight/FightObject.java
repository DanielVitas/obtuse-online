package com.obtuse.game.gameobjects.fight;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.abilities.all.Poison;
import com.obtuse.game.audio.SoundPlayer;
import com.obtuse.game.gameobjects.BasicObject;
import com.obtuse.game.gameobjects.fight.buffs.PoisonStatus;
import com.obtuse.game.gameobjects.fight.buffs.Status;
import com.obtuse.game.gameobjects.fight.buffs.Stunned;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.gameobjects.items.AbilityOrb;
import com.obtuse.game.gameobjects.items.Equipment;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.Event;
import com.obtuse.game.maingame.fight.FightGame;
import com.obtuse.game.maingame.fight.Turn;
import com.obtuse.game.maingame.fight.events.PoisonEvent;
import com.obtuse.game.maingame.fight.levels.FightLevel;
import com.obtuse.game.screens.FightScreen;
import com.obtuse.game.screens.MyScreen;

public abstract class FightObject extends BasicObject {
    protected float defaultFD;
    protected float summonFD;
    protected float castFD;
    protected float hurtFD;
    protected float deathFD;
    protected float stunnedFD;
    protected float unstunnedFD;
    protected float deathStunnedFD;
    protected String hurtSound;
    protected String deathSound;
    protected String healSound;
    public Holder holder;
    public Profile profile;
    protected String displayName;
    public Array<Status> statuses = new Array<Status>();
    public Array<Equipment> equipment = new Array<Equipment>();
    public Array<AbilityOrb> abilityOrbs = new Array<AbilityOrb>();
    public Array<Event> preturn = new Array<Event>();
    public Array<Event> postturn = new Array<Event>();
    public Array<AbilityInstance> abilities = new Array<AbilityInstance>();
    public int hp;
    public int originalHP;
    public int damageTaken;
    public int originalSpeed;
    public int speed;
    private boolean isDead = false;
    protected boolean isStunned = false;
    public AbilityInstance lastUsed;

    public FightObject(float defaultFD, float summonFD, float castFD, float hurtFD, float deathFD,
                       float stunnedFD, float unstunnedFD, float deathStunnedFD, String hurtSound, String deathSound,
                       String healSound) {
        path += "fight/";
        this.defaultFD = defaultFD;
        this.summonFD = summonFD;
        this.castFD = castFD;
        this.hurtFD = hurtFD;
        this.deathFD = deathFD;
        this.stunnedFD = stunnedFD;
        this.unstunnedFD = unstunnedFD;
        this.deathStunnedFD = deathStunnedFD;
        this.hurtSound = hurtSound;
        this.deathSound = deathSound;
        this.healSound = healSound;
    }

    public void installAnimations() {
        addAnimation("default", defaultFD, Animation.PlayMode.LOOP);
        addAnimation("summon", summonFD, Animation.PlayMode.NORMAL);
        addAnimation("cast", castFD, Animation.PlayMode.NORMAL);
        addAnimation("hurt", hurtFD, Animation.PlayMode.NORMAL);
        addAnimation("death", deathFD, Animation.PlayMode.NORMAL);
        addAnimation("stunned", stunnedFD, Animation.PlayMode.NORMAL);
        addAnimation("unstunned", unstunnedFD, Animation.PlayMode.NORMAL);
        addAnimation("deathStunned", deathStunnedFD, Animation.PlayMode.NORMAL);
    }

    public void setupEquipment() {
        for (Equipment eq : equipment)
            eq.setup(this);
    }

    public Array<Status> getStatuses() {
        PoisonStatus poisonStatus = new PoisonStatus();
        Array<Status> statuses = new Array<Status>(this.statuses);
        int poisonStacks = 0;
        for (Array<Event> events : new Array[]{preturn, postturn})
            for (Event event : events)
                if (event instanceof PoisonEvent) {
                    poisonStacks ++;
                    if (!statuses.contains(poisonStatus, true))
                        statuses.add(poisonStatus);
                }
        poisonStatus.damage = Integer.toString(poisonStacks);
        return statuses;
    }

    public String getName() {
        return displayName;
    }

    public void setName(String name) {
        displayName = name;
    }

    public float damage(int dmg) {
        if (alive()) {
            damageTaken += dmg;
            holder.refreshHPLabel();
            holder.refreshHealthBar();
            if (damageTaken >= hp) {
                damageTaken = hp;
                return death();
            }
            return hurt(dmg);
        }
        return 0;
    }

    public Status getBuff(Class buffClass) {
        for (Status status : statuses)
            if (status.getClass() == buffClass)
                return status;
        return null;
    }

    public void removeBuff(Status status) {
        statuses.removeValue(status,false);
    }

    public float getClickHeight() {
        return getHeight();
    }

    public float defaultAnimation() {
        if (alive())
            if (isStunned)
                return 0;
            else
                return play("default",0);
        else
            return 0;
    }

    public float summon() {
        if (!alive())
            return 0;
        holder.healthBar.fill();
        return play("summon");
    }

    public float cast() {
        return play("cast");
    }

    public float hurt(int damage) {
        if (damage > 0)
            SoundPlayer.play(hurtSound);
        else if (damage < 0)
            SoundPlayer.play(healSound);
        if (isStunned)
            return 0;
        return play("hurt");
    }

    public float death() {
        SoundPlayer.play(deathSound);
        isDead = true;
        ((FightLevel) holder.arena.fightGame.level).removeHolderInfo(holder);
        if (isStunned)
            return play("deathStunned");
        return play("death");
    }

    public float stun() {
        isStunned = true;
        if (alive())
            return play("stunned");
        else
            return 0;
    }

    public float unstun() {
        isStunned = false;
        if (alive())
            return play("unstunned");
        else
            return 0;
    }

    public float play(String name) {
        float f = play(name,0);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                defaultAnimation();
            }
        },f - 0.02f);
        return f;
    }

    public boolean alive() {
        return !isDead;
    }

    public void reset() {
        preturn.clear();
        postturn.clear();
        abilities.clear();
        statuses.clear();
        lastUsed = null;
        hp = originalHP;
        damageTaken = 0;
        isDead = false;
        isStunned = false;
        speed = originalSpeed;
        for (AbilityOrb abilityOrb : abilityOrbs)
            abilities.add(new AbilityInstance(abilityOrb.ability));
        play("default",0);
    }

    protected void choice() {
        ((FightGame) ((FightScreen) MyScreen.screens.get("FightScreen")).gameGame).choice(this);
    }

    protected abstract void turn(Arena arena, FightLevel level);

    public void takeTurn(Arena arena, FightLevel level) {
        if (getBuff(Stunned.class) == null)
            turn(arena, level);
        else {
            removeBuff(getBuff(Stunned.class));
            Turn.sleep(unstun());
        }
    }

    public void create(float x, float y, float width, float height) {
        setWidth(width);
        setHeight(height);
        setPosition(x, y);
    }

    public void create(float x, float y) {
        create(x, y, getWidth(), getHeight());
    }
}
