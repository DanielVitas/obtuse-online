package com.obtuse.game.gameobjects.fight.AI;

import com.badlogic.gdx.utils.Array;
import com.obtuse.game.abilities.AbilityInstance;
import com.obtuse.game.gameobjects.fight.FightObject;
import com.obtuse.game.gameobjects.fight.holders.fightObject.Holder;
import com.obtuse.game.maingame.fight.Arena;
import com.obtuse.game.maingame.fight.levels.FightLevel;

public class SuggestedRandomAI extends AI{
    private AbilityInstance ability;
    private Holder target;

    @Override
    public void decide(Arena arena, FightObject caster, FightLevel level) {
        slowChooseAbilityAndTarget(arena, caster);
        if (ability == null) {
            if (outOfPP(caster))
                pass(caster, level);   // no PP left → Pass instead of skipping for free
            return;
        }
        perform(caster, ability, target, level);   // DD message + 1s linger, then the move runs
    }

    private void slowChooseAbilityAndTarget(Arena arena, FightObject caster) {
        target = null;
        while (target == null) {
            if (timeOut()) {
                ability = null;
                break;
            }
            ability = randomAbility(caster);
            if (ability == null)
                break;
            if (ability.ability.targets.size == 0)
                return;
            Array<Holder> targets = getSuggestedTargets(arena, ability, true);
            if (targets.size > 0)
                target = targets.get(random.nextInt(targets.size));
        }
    }
}
