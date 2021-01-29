package Moonworks.patches;

import Moonworks.cards.ExtendedPhotonRifle;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

/*
@SpirePatch(
        clz = VigorPower.class, //This is the class you're patching.
        method = "onUseCard" //This is the name of the method of that class that you're patching.
)
public class VigorPowerPrefixPatch
{
    @SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
    public static SpireReturn<?> VigorPatch(VigorPower __instance, AbstractCard card, UseCardAction action) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
    {
        if (card instanceof ExtendedPhotonRifle) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue(); //This allows you to either return early, change the value returned, or just continue like this.
    }
}*/