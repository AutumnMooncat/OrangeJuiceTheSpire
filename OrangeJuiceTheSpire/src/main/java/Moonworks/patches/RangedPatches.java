package Moonworks.patches;

import Moonworks.cards.interfaces.RangedAttack;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class RangedPatches {

    @SpirePatch(
            clz= DamageInfo.class,
            method=SpirePatch.CLASS
    )
    public static class RangedField
    {
        public static SpireField<Boolean> ranged = new SpireField<>(() -> Boolean.FALSE);
    }


    @SpirePatch(
            clz = ThornsPower.class, //This is the class you're patching.
            method = "onAttacked" //This is the name of the method of that class that you're patching.
    )
    public static class ThornsPowerPrefixPatch
    {
        @SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
        public static SpireReturn<?> ThornsPatch(ThornsPower __instance, DamageInfo info, int damageAmount) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
        {
            if (RangedField.ranged.get(info)) {
                return SpireReturn.Return(damageAmount);
            }
            return SpireReturn.Continue(); //This allows you to either return early, change the value returned, or just continue like this.
        }
    }

    @SpirePatch(
            clz = SharpHidePower.class, //This is the class you're patching.
            method = "onUseCard" //This is the name of the method of that class that you're patching.
    )
    public static class SharpHidePowerPrefixPatch
    {
        @SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
        public static SpireReturn<?> SharpHidePatch(SharpHidePower __instance, AbstractCard card, UseCardAction action) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
        {
            if (card instanceof RangedAttack) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue(); //This allows you to either return early, change the value returned, or just continue like this.
        }
    }

}
