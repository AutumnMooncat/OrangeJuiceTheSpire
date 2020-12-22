package Moonworks.patches;

import Moonworks.powers.AbstractTrapPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SadisticPower;

@SpirePatch(
        clz = SadisticPower.class, //This is the class you're patching.
        method = "onApplyPower" //This is the name of the method of that class that you're patching.
)
public class SadisticPowerPrefixPatch
{
    @SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
    public static SpireReturn<?> SadisticPatch(SadisticPower __instance, AbstractPower power, AbstractCreature target, AbstractCreature source) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
    {
        if (power instanceof AbstractTrapPower) {
            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(__instance.owner, __instance.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
        return SpireReturn.Continue(); //This allows you to either return early, change the value returned, or just continue like this.
    }
}