package com.obtuse.game.maingame.fight;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.obtuse.game.Obtuse;
import com.obtuse.game.gameobjects.fight.Enemy;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.Hero;
import com.obtuse.game.gameobjects.fight.Summon;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.events.Damage;
import com.obtuse.game.maingame.fight.levels.FightLevel;

import java.util.Comparator;

public abstract class Arena {
    private static float[] profilePosition = {0.5f, 0f, 9f / 16}; // x, starting y (derived at runtime), x gap, y gap
    public FightGame fightGame;
    private float[] heroPositions;
    private float[] enemyPositions;
    private float[] summonPositions;
    public Array<Holder> heroHolders = new Array<Holder>();
    public Array<Holder> enemyHolders = new Array<Holder>();
    public Array<Holder> summonHolders = new Array<Holder>();
    public int index = 0;
    public Array<Enemy> enemies = new Array<Enemy>();
    public Array<Hero> heroes = new Array<Hero>();
    public Array<Summon> summons = new Array<Summon>();
    public Array<Event> beginingPhaseEvents = new Array<Event>();
    public Array<Event> endPhaseEvents = new Array<Event>();
    public static Array<Trigger> triggers = new Array<Trigger>();
    public Array<FightObject> fighterOrder = new Array<FightObject>();
    public Array<Arena> dependOnArenas = new Array<Arena>();
    protected boolean finished = false;

    public boolean isFinished() {
        return finished;
    }

    public Arena(FightGame fightGame) {
        this.fightGame = fightGame;
        heroPositions = setHeroPositions();
        enemyPositions = setEnemyPositions();
        summonPositions = setSummonPositions();
        for (int i = 0; i < heroPositions.length / 2; i++)
            heroHolders.add(new Holder(this, heroPositions[2 * i], heroPositions[2 * i + 1],0));
        for (int i = 0; i < enemyPositions.length / 2; i++)
            enemyHolders.add(new Holder(this, enemyPositions[2 * i], enemyPositions[2 * i + 1],1));
        for (int i = 0; i < summonPositions.length / 2; i++)
            summonHolders.add(new Holder(this, summonPositions[2 * i], summonPositions[2 * i + 1],2));
    }

    protected void link(Holder holder1, Holder holder2) {
        if (!holder1.adjacent.contains(holder2, true))
            holder1.adjacent.add(holder2);
        if (!holder2.adjacent.contains(holder1, true))
            holder2.adjacent.add(holder1);
    }

    protected void setupHolderFightObjects() {
        for (int i = 0; i < heroes.size; i++)
            heroHolders.get(i).setFightObject(heroes.get(i));
        for (int i = 0; i < enemies.size; i++)
            enemyHolders.get(i).setFightObject(enemies.get(i));
        for (int i = 0; i < summons.size; i++)
            summonHolders.get(i).setFightObject(summons.get(i));
    }

    private void removeHolderFightObjects() {
        for (Array<Holder> holderArray : new Array[]{heroHolders, enemyHolders, summonHolders})
            for (Holder holder : holderArray)
                if (holder.fightObject == null)
                    holder.removeFightObject();
    }

    public void resetHolderFightObjects() {
        removeHolderFightObjects();
        setupHolderFightObjects();
    }

    public void addNext(FightObject fightObject) {
        if (fightObject.getClass().getSuperclass() == Hero.class) {
            for (Holder holder : heroHolders)
                if (holder.spawnable()) {
                    add(fightObject, holder);
                    break;
                }
        } else if (fightObject.getClass().getSuperclass() == Enemy.class) {
            for (Holder holder : enemyHolders)
                if (holder.spawnable()) {
                    add(fightObject, holder);
                    break;
                }
        }/* else if (fightObject.getClass().getSuperclass() == Summon.class) {
            for (Holder holder : summonHolders)
                if (holder.fightObject == null) {
                    add(fightObject, holder);
                    break;
                }
        }*/
    }

    public void add(FightObject fightObject, Holder holder) {
        if (fightObject.getClass().getSuperclass() == Hero.class)
            heroes.add(((Hero) fightObject));
        else if (fightObject.getClass().getSuperclass() == Enemy.class)
            enemies.add(((Enemy) fightObject));
        else if (fightObject.getClass().getSuperclass() == Summon.class)
            summons.add(((Summon) fightObject));
        holder.setFightObject(fightObject);
    }

    public void remove(FightObject fightObject) {
        if (fighterOrder.contains(fightObject, true))
            if (fighterOrder.indexOf(fightObject, true) < index)
                index -= 1;
        if (fightObject.getClass().getSuperclass() == Hero.class)
            heroes.removeValue(((Hero) fightObject), true);
        else if (fightObject.getClass().getSuperclass() == Enemy.class)
            enemies.removeValue(((Enemy) fightObject), true);
        else if (fightObject.getClass().getSuperclass() == Summon.class)
            summons.removeValue(((Summon) fightObject), true);
        if (fighterOrder.contains(fightObject, true))
            fighterOrder.removeValue(fightObject, true);
        fightObject.holder.removeFightObject();
    }

    protected abstract float[] setHeroPositions();
    protected abstract float[] setEnemyPositions();
    protected abstract float[] setSummonPositions();
    protected abstract void lose();
    protected abstract void win();

    protected boolean end() {
        for (Arena arena : dependOnArenas)
            if (!arena.finished) {
                return true;
            }
        finished = true;
        return false;
    }

    public static void trigger(Event event) {
        triggers.reverse();
        for (int i = 0; i < triggers.size; i++)
            if (triggers.get(i).checkAll(event))
                triggers.get(i).run(event);
        triggers.reverse();
    }

    private void phase(Array<Event> array) {
        array.reverse();
        Array<Event> toRemove  = new Array<Event>();
        for (int i = 0; i < array.size; i++)
            if (array.get(i).turn == 0) {
                trigger(array.get(i));
                array.get(i).preform();
                if (array.get(i).uses != -1)
                    array.get(i).uses -= 1;
                if (array.get(i).uses == 0)
                    toRemove.add(array.get(i));
            } else
                array.get(i).turn -= 1;
        array.removeAll(toRemove, true);
        array.reverse();
    }

    // Set when the order was already computed (by preorder(), for the summon sequence) so the first
    // conduct() uses that exact order instead of re-shuffling.
    private boolean preordered = false;

    /** Compute the turn order now (so the summon can appear in the exact first-turn order). */
    public void preorder() {
        order();
        preordered = true;
    }

    public void conduct() {
        check();
        if (preordered)
            preordered = false;
        else
            order();
        createProfiles();
        phase(beginingPhaseEvents);
        check();
        index = 0;
        while (index < fighterOrder.size) {
            refreshButtons();
            if (fighterOrder.get(index).alive() && check()) {
                fighterTurn(fighterOrder.get(index), fighterOrder.get(index).holder);
            }
            index ++;
        }
        // If a side was wiped during the round (check() above set finished), the battle is over — stop
        // simulating. Skip the end-of-round burning/regen phase so e.g. burning ground doesn't keep
        // hitting the surviving enemies after the last hero has already died.
        if (!finished) {
            runHolders();
            phase(endPhaseEvents);
        }
        deleteProfiles();
        check();
    }

    public void refreshButtons() {
        ((FightLevel) fightGame.level).refreshButtons(this);
    }

    public void runHolders() {
        for (Array<Holder> holderArray : new Array[]{heroHolders, enemyHolders, summonHolders})
            for (Holder holder : holderArray)
                if (holder.burning != 0)
                    if (holder.fightObject != null)
                        endPhaseEvents.add(new Damage(holder.burning, null, holder.fightObject).cause("burned away."));
    }

    private void fighterTurn(FightObject fightObject, Holder holder) {
        holder.onTurn = true;
        holder.playSlotAnimation();
        fightObject.profile.onTurn();

        phase(fightObject.preturn);
        // Check THIS fighter's own liveness, not fighterOrder.get(index): if it died during its own
        // preturn (e.g. Blood Sacrifice recoil) and its death-split removed it from fighterOrder,
        // get(index) is now a different fighter — or out of bounds — and a dead fighter would wrongly
        // take a turn.
        if (fightObject.alive() && check()) {
            fightObject.takeTurn(this, ((FightLevel) fightGame.level));
            phase(fightObject.postturn);
            check();
        }

        fightObject.profile.offTurn();
        holder.onTurn = false;
        holder.playSlotAnimation();
        fightGame.loseInfo();
    }

    public void refreshProfiles() {
        deleteProfiles();
        createProfiles();
    }

    /**
     * Every fighter across all active arenas, deduped — so during a duel the turn bar shows the
     * characters of BOTH arenas together, not just the one currently conducting.
     */
    private Array<FightObject> profileFighters() {
        Array<FightObject> all = new Array<FightObject>();
        // INDEXED loops, not for-each: fightGame.arenas is already being iterated by FightGame.runAll
        // with libGDX's pooled iterator, and a nested for-each over the same array invalidates the
        // outer one ("#iterator() cannot be used nested") — which aborts runAll before the duel arena
        // conducts, so the duellists never got a turn.
        if (fightGame != null)
            for (int a = 0; a < fightGame.arenas.size; a++) {
                Array<FightObject> order = fightGame.arenas.get(a).fighterOrder;
                for (int f = 0; f < order.size; f++)
                    if (!all.contains(order.get(f), true))
                        all.add(order.get(f));
            }
        if (all.size == 0)
            all.addAll(fighterOrder);
        return all;
    }

    private static final float PROFILE_GROUP_GAP = 0.5f; // gap between arena groups, as a fraction of profile spacing

    /**
     * Position every profile along the turn bar, centred, with a small gap BETWEEN each arena's
     * group — so during a duel the main-arena fighters and the duel-arena fighters read as two
     * clusters rather than one continuous run. (A fighter lives in exactly one arena's fighterOrder.)
     */
    private void layoutProfiles() {
        float spacing = profilePosition[2];
        float gap = spacing * PROFILE_GROUP_GAP;
        float y = Obtuse.cameraWidth / Obtuse.ratio - 9f / 16; // from the CURRENT ratio, not baked at class load
        Array<Array<FightObject>> groups = new Array<Array<FightObject>>();
        int total = 0;
        if (fightGame != null)
            for (int a = 0; a < fightGame.arenas.size; a++) {
                Array<FightObject> order = fightGame.arenas.get(a).fighterOrder;
                if (order.size > 0) { groups.add(order); total += order.size; }
            }
        if (groups.size == 0) { groups.add(fighterOrder); total = fighterOrder.size; }
        float totalWidth = total * spacing + (groups.size - 1) * gap;
        float x = Obtuse.cameraWidth / 2 - totalWidth / 2;
        for (int g = 0; g < groups.size; g++) {
            Array<FightObject> group = groups.get(g);
            for (int i = 0; i < group.size; i++) {
                group.get(i).profile.setX(x);
                group.get(i).profile.setY(y);
                x += spacing;
            }
            x += gap;
        }
    }

    private void createProfiles() {
        layoutProfiles();
        Array<FightObject> profiles = profileFighters();
        for (int i = 0; i < profiles.size; i++)
            fightGame.level.stage(0).addActor(profiles.get(i).profile);
    }

    /**
     * Re-derive the slot/fighter and turn-order positions for the CURRENT surface ratio. The layouts
     * (setHeroPositions etc.) and the profile row are all relative to the viewport height
     * (cameraWidth/ratio), but were computed once at construction — so on a window resize the callers
     * (FightGame.layoutChanged) recompute them here, then re-add the holders at the new spots.
     */
    public void relayout() {
        heroPositions = setHeroPositions();
        enemyPositions = setEnemyPositions();
        summonPositions = setSummonPositions();
        applyHolderPositions(heroHolders, heroPositions);
        applyHolderPositions(enemyHolders, enemyPositions);
        applyHolderPositions(summonHolders, summonPositions);
        layoutProfiles();
    }

    private void applyHolderPositions(Array<Holder> holders, float[] positions) {
        for (int i = 0; i < holders.size && 2 * i + 1 < positions.length; i++) {
            holders.get(i).x = positions[2 * i];
            holders.get(i).y = positions[2 * i + 1];
        }
    }

    private void deleteProfiles() {
        for (FightObject fightObject : profileFighters())
            fightObject.profile.remove();
    }

    private boolean check() {
        boolean e = true;
        lose : {
            for (Hero hero : heroes)
                if (hero.alive())
                    break lose;
            e = end();
            lose();
            return e;
        }
        win : {
            for (Enemy enemy : enemies)
                if (enemy.alive())
                    break win;
            e = end();
            win();
            return e;
        }
        return e;
    }

    public void order() {
        fighterOrder.clear();
        for (FightObject object : enemies)
            if (object.alive())
                fighterOrder.add(object);
        for (FightObject object : heroes)
            if (object.alive())
                fighterOrder.add(object);
        for (FightObject object : summons)
            if (object.alive())
                fighterOrder.add(object);
        fighterOrder.shuffle();
        fighterOrder.sort(new Comparator<FightObject>() {
            @Override
            public int compare(FightObject o1, FightObject o2) {
                return o2.speed - o1.speed;
            }
        });
        // Fighters who Passed last turn go first — still speed-ordered among themselves. Array.sort is
        // stable, so this secondary sort keeps the speed order within the passed / not-passed groups.
        fighterOrder.sort(new Comparator<FightObject>() {
            @Override
            public int compare(FightObject o1, FightObject o2) {
                return (o2.passed ? 1 : 0) - (o1.passed ? 1 : 0);
            }
        });
        // The Pass priority lasts exactly one turn: consume the flag now that the order is set.
        for (FightObject object : fighterOrder)
            object.passed = false;
    }

    public void clear() {
        enemies.clear();
        heroes.clear();
        beginingPhaseEvents.clear();
        endPhaseEvents.clear();
        fighterOrder.clear();
    }
}
