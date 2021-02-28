package Moonworks.patches;

import Moonworks.cards.interfaces.OnDealDamageCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class OnDamageMonsterPatches {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"

    )
    public static class DealDamageListener {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void dealDamageListener(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            if (info.owner == AbstractDungeon.player) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c instanceof OnDealDamageCard) {
                        damageAmount[0] = ((OnDealDamageCard) c).onDealDamage(__instance, info, damageAmount[0]);
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Math.class, "min");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
