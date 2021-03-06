package Moonworks.patches;

import Moonworks.cards.giftCards.PoppoTheSnatcher;
import Moonworks.cards.interfaces.OnDebuffedCard;
import Moonworks.cards.interfaces.OnEnemyBuffCard;
import Moonworks.powers.AbstractTrapPower;
import Moonworks.powers.WantedPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                AbstractCreature.class,
                AbstractCreature.class,
                AbstractPower.class,
                int.class,
                boolean.class,
                AbstractGameAction.AttackEffect.class
        })
public class ApplyPowerPrefixPatch
{
    @SpirePrefixPatch
    public static void PowerReader(ApplyPowerAction __instance, @ByRef AbstractCreature[] target, @ByRef AbstractCreature[] source, @ByRef AbstractPower[] powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        if(powerToApply[0].type == AbstractPower.PowerType.DEBUFF) { //If the power is a debuff
            //Wanted Logic
            for(AbstractMonster aM : AbstractDungeon.getMonsters().monsters) { //Look through monster list
                if(/*aM != source[0] &&*/ aM.hasPower(WantedPower.POWER_ID) && !aM.isDeadOrEscaped()) { //does one have this power?
                    target[0] = aM; //new target is that monster
                    break;
                }
            }
            if(target[0] != null) { //null check. Kinda useless since if target is ull the entire rest of the function call is going to go to shite
                powerToApply[0].owner = target[0]; //set owner
                /*if(AbstractDungeon.actionManager.currentAction.target != null){
                    AbstractDungeon.actionManager.currentAction.target = target[0]; //set action target
                }*/
                if (powerToApply[0] instanceof HexPower) { //Check for Hex
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target[0], target[0], powerToApply[0].ID)); //Remove it, Hex has no use on a monster
                }
            }
            //Unlucky Charm Logic
            if (target[0] == AbstractDungeon.player) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c instanceof OnDebuffedCard) {
                        ((OnDebuffedCard) c).powerApplied(__instance, target[0], source[0], powerToApply[0], stackAmount, effect);
                    }
                }
            }
        } else {
            //If the power is a buff, and it isn't a trap-like or problematic power
            if (!(powerToApply[0] instanceof AbstractTrapPower) &&
                    !(powerToApply[0] instanceof TheBombPower) &&
                    !(powerToApply[0] instanceof FadingPower) &&
                    !(powerToApply[0] instanceof SurroundedPower) &&
                    !(powerToApply[0] instanceof BackAttackPower) &&
                    !(powerToApply[0] instanceof MinionPower) &&
                    !(powerToApply[0] instanceof ModeShiftPower) &&
                    !(powerToApply[0] instanceof ReactivePower) &&
                    !(powerToApply[0] instanceof RegrowPower) &&
                    !(powerToApply[0] instanceof ResurrectPower) &&
                    !(powerToApply[0] instanceof StasisPower) &&
                    !(powerToApply[0] instanceof ThieveryPower) &&
                    !(powerToApply[0] instanceof TimeWarpPower) &&
                    !(powerToApply[0] instanceof UnawakenedPower)){
                //and if the source and target are not the player
                if (source[0] != AbstractDungeon.player && target[0] != AbstractDungeon.player) {
                    for (AbstractCard c : AbstractDungeon.player.hand.group) {
                        if (c instanceof OnEnemyBuffCard) {
                            ((OnEnemyBuffCard) c).powerApplied(__instance, target[0], source[0], powerToApply[0], stackAmount, effect);
                        }
                        if (c instanceof PoppoTheSnatcher) {
                            target[0] = AbstractDungeon.player;
                            powerToApply[0].owner = AbstractDungeon.player;
                            if (powerToApply[0] instanceof RitualPower) {
                                ReflectionHacks.setPrivate(powerToApply[0], RitualPower.class, "onPlayer", true);
                                //ReflectionHacks.setPrivate(powerToApply[0], RitualPower.class, "skipFirst", false);
                            }
                            if (powerToApply[0] instanceof SharpHidePower) {
                                powerToApply[0] = new ThornsPower(target[0], powerToApply[0].amount);
                            }
                        }
                    }
                }
            }
        }
    }
}