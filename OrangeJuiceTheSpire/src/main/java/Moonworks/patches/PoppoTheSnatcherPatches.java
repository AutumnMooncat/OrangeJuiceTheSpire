package Moonworks.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.HexPower;
import com.megacrit.cardcrawl.powers.PainfulStabsPower;
import com.megacrit.cardcrawl.powers.SharpHidePower;

public class PoppoTheSnatcherPatches {

    @SpirePatch(
            clz = BeatOfDeathPower.class,
            method = "onAfterUseCard")
    public static class BeatOfDeathPatch
    {
        @SpirePrefixPatch
        public static void onAfterUseCardPatch(BeatOfDeathPower __instance, AbstractCard card, UseCardAction action)
        {
            if (__instance.owner == AbstractDungeon.player) {
                __instance.flash();
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, __instance.amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                __instance.updateDescription();
                SpireReturn.Return(null);
            }
        }
    }

    @SpirePatch(
            clz = SharpHidePower.class,
            method = "onUseCard")
    public static class SharpHidePowerPatch
    {
        @SpirePrefixPatch
        public static void onUseCardPatch(SharpHidePower __instance, AbstractCard card, UseCardAction action)
        {
            if (__instance.owner == AbstractDungeon.player) {
                __instance.flash();
                //TODO some effect
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, __instance.amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                __instance.updateDescription();
                SpireReturn.Return(null);
            }
        }
    }

    @SpirePatch(
            clz = PainfulStabsPower.class,
            method = "onInflictDamage")
    public static class PainfulStabsPowerPatch
    {
        @SpirePrefixPatch
        public static void onInflictDamagePatch(PainfulStabsPower __instance, DamageInfo info, int damageAmount, AbstractCreature target)
        {
            if (__instance.owner == AbstractDungeon.player) {
                __instance.flashWithoutSound();
                //TODO? Gain temp str? Deal damage?
                SpireReturn.Return(null);
            }
        }
    }

    @SpirePatch(
            clz = HexPower.class,
            method = "onUseCard")
    public static class HexPowerPatch
    {
        @SpirePrefixPatch
        public static void onUseCardPatch(HexPower __instance, AbstractCard card, UseCardAction action)
        {
            if (__instance.owner != AbstractDungeon.player) {
                __instance.flashWithoutSound();
                //TODO? Maybe give this some unique effect, for now doing nothing is fine
                SpireReturn.Return(null);
            }
        }
    }
}
