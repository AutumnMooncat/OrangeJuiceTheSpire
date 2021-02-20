package Moonworks.patches;

import Moonworks.powers.CrystallizePower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CrystallizePatch {

    @SpirePatch(
            clz = ApplyPowerAction.class, //This is the class you're patching.
            method = "update" //This is the name of the method of that class that you're patching.
    )
    public static class ApplyPowerActionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> updateReader(ApplyPowerAction __instance)
        {
            if (__instance.target != null && !__instance.target.isDeadOrEscaped()) {
                AbstractPower pow = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "powerToApply");
                if (__instance.target.hasPower(CrystallizePower.POWER_ID) && pow.type == AbstractPower.PowerType.DEBUFF) {
                    AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(__instance.target, ApplyPowerAction.TEXT[0]));
                    CardCrawlGame.sound.play("NULLIFY_SFX");
                    __instance.target.getPower(CrystallizePower.POWER_ID).flashWithoutSound();
                    __instance.target.getPower(CrystallizePower.POWER_ID).onSpecificTrigger();
                    __instance.isDone = true;
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
