package Moonworks.patches;

import Moonworks.powers.Heat300PercentPower;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.powers.FrailPower;

//Alchyr#3696
//This goes in its own file. Call it NoBlockPatch.java or something.
@SpirePatch(
        clz = EntanglePower.class, //This is the class you're patching.
        method = "atEndOfTurn" //This is the name of the method of that class that you're patching.
)
public class StackableEntanglePatch
{
    @SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
    public static SpireReturn<?> StackableEntangle(EntanglePower __instance, boolean isPlayer) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
    {
        __instance.amount--;
        if (__instance.amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, __instance.ID));
        }
        return SpireReturn.Return(null); //This allows you to either return early, change the value returned, or just continue like this.
    }
}