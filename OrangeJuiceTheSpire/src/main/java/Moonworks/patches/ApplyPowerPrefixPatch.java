package Moonworks.patches;

import Moonworks.powers.Heat300PercentPower;
import Moonworks.powers.WantedPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.HexPower;

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
            for(AbstractMonster aM : AbstractDungeon.getMonsters().monsters) { //Look through monster list
                if(aM != source[0] && aM.hasPower(WantedPower.POWER_ID) && !aM.isDeadOrEscaped()) { //does one have this power?
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
        }
    }
}