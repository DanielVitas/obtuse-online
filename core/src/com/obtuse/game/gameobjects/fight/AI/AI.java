package com.obtuse.game.gameobjects.fight.AI;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.abilities.Ability;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

import java.util.Random;

import static com.badlogic.gdx.Input.Keys.O;

public abstract class AI {
    protected long outTime = 200;
    protected Random random = new Random();
    protected long timeInitiated = 0;

    public abstract void decide(Arena arena, FightObject caster, FightLevel level);

    public void turn(Arena arena, FightObject caster, FightLevel level) {
        timeInitiated = System.currentTimeMillis();
        decide(arena, caster, level);
    }

    private Array<AbilityInstance> getUsableAbilities(FightObject fightObject) {
        Array<AbilityInstance> usableAbilities = new Array<AbilityInstance>();
        for (AbilityInstance ability : fightObject.abilities)
            if (ability.ppUsed < ability.pp)
                usableAbilities.add(ability);
        return usableAbilities;
    }

    /** True when the fighter has no ability with PP left — it must Pass rather than act. */
    protected boolean outOfPP(FightObject fightObject) {
        return getUsableAbilities(fightObject).size == 0;
    }

    /** Announce the move in the big DD panel, linger ~1s, then run it (damage/animation follow). */
    protected void perform(FightObject caster, AbilityInstance ability, Holder target, FightLevel level) {
        level.showEnemyAction(caster, ability);
        com.obtuse.game.maingame.fight.Turn.sleep(1f);
        ability.run(caster, target, level);
        level.hideDescription();
    }

    /** Pass (like a hero): 1 unmodifiable self-damage and next-turn priority, announced in the DD. */
    protected void pass(FightObject caster, FightLevel level) {
        perform(caster, new AbilityInstance(new com.obtuse.game.abilities.all.SkipTurn()), null, level);
    }

    protected boolean timeOut() {
        return System.currentTimeMillis() - timeInitiated > outTime;
    }

    protected Array<Holder> getAvailableTargets(Arena arena, AbilityInstance ability, boolean inverse) {
        Array<Holder> targets = new Array<Holder>();
        Array<Integer> abilityTargets;
        if (inverse)
            abilityTargets = ability.ability.inverseTargets(ability.ability.targets);
        else
            abilityTargets = ability.ability.targets;
        if (abilityTargets.contains(Ability.HEROSLOT,true))
            targets.addAll(arena.heroHolders);
        if (abilityTargets.contains(Ability.ENEMYSLOT,true))
            targets.addAll(arena.enemyHolders);
        if (abilityTargets.contains(Ability.SUMMONSLOT,true))
            targets.addAll(arena.summonHolders);
        if (abilityTargets.contains(Ability.HERO,true))
            targets.addAll(removeDeadHolders(arena.heroHolders));
        if (abilityTargets.contains(Ability.ENEMY,true))
            targets.addAll(removeDeadHolders(arena.enemyHolders));
        if (abilityTargets.contains(Ability.SUMMON,true))
            targets.addAll(removeDeadHolders(arena.summonHolders));
        if (abilityTargets.contains(Ability.EMPTYHEROSLOT,true))
            targets.addAll(complement(arena.heroHolders, removeDeadHolders(arena.heroHolders)));
        if (abilityTargets.contains(Ability.EMPTYENEMYSLOT,true))
            targets.addAll(complement(arena.enemyHolders, removeDeadHolders(arena.enemyHolders)));
        if (abilityTargets.contains(Ability.EMPTYSUMMONSLOT,true))
            targets.addAll(complement(arena.summonHolders, removeDeadHolders(arena.summonHolders)));
        return targets;
    }

    protected Array<Holder> getSuggestedTargets(Arena arena, AbilityInstance ability, boolean inverse) {
        Array<Holder> targets = new Array<Holder>();
        Array<Integer> abilityTargets;
        if (inverse)
            abilityTargets = ability.ability.inverseTargets(ability.ability.suggestedTargets);
        else
            abilityTargets = ability.ability.suggestedTargets;
        if (abilityTargets.contains(Ability.HEROSLOT,true))
            targets.addAll(arena.heroHolders);
        if (abilityTargets.contains(Ability.ENEMYSLOT,true))
            targets.addAll(arena.enemyHolders);
        if (abilityTargets.contains(Ability.SUMMONSLOT,true))
            targets.addAll(arena.summonHolders);
        if (abilityTargets.contains(Ability.HERO,true))
            targets.addAll(removeDeadHolders(arena.heroHolders));
        if (abilityTargets.contains(Ability.ENEMY,true))
            targets.addAll(removeDeadHolders(arena.enemyHolders));
        if (abilityTargets.contains(Ability.SUMMON,true))
            targets.addAll(removeDeadHolders(arena.summonHolders));
        if (abilityTargets.contains(Ability.EMPTYHEROSLOT,true))
            targets.addAll(complement(arena.heroHolders, removeDeadHolders(arena.heroHolders)));
        if (abilityTargets.contains(Ability.EMPTYENEMYSLOT,true))
            targets.addAll(complement(arena.enemyHolders, removeDeadHolders(arena.enemyHolders)));
        if (abilityTargets.contains(Ability.EMPTYSUMMONSLOT,true))
            targets.addAll(complement(arena.summonHolders, removeDeadHolders(arena.summonHolders)));
        return targets;
    }

    public Array<Holder> complement(Array<Holder> main, Array<Holder> without) {
        Array<Holder> holders = new Array<Holder>(main);
        for (Holder holder : without)
            holders.removeValue(holder, true);
        return holders;
    }

    public Array<Holder> removeEmptyHolders(Array<Holder> holders) {
        Array<Holder> filledHolders = new Array<Holder>();
        for (Holder holder : holders)
            if (holder.fightObject != null)
                filledHolders.add(holder);
        return filledHolders;
    }

    public Array<Holder> removeDeadHolders(Array<Holder> holders) {
        Array<Holder> filledHolders = new Array<Holder>();
        for (Holder holder : removeEmptyHolders(holders))
            if (holder.fightObject.alive())
                filledHolders.add(holder);
        return filledHolders;
    }

    public AbilityInstance randomAbility(FightObject fightObject) {
        if (getUsableAbilities(fightObject).size > 0)
            return getUsableAbilities(fightObject).get(random.nextInt(getUsableAbilities(fightObject).size));
        return null;
    }
}
