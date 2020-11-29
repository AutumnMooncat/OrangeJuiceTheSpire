package Moonworks.patches;

import Moonworks.powers.WantedPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.ArrayList;

// Modified from Darkglade1 - Gensokyo. I would be completely lost without this
// A patch to make mind controlled enemies properly target someone else
public class DamageActionPrefixPatch {
    @SpirePatch(
            clz = DamageAction.class,
            method = "update"

    )
    public static class DamageActionRetarget {
        @SpireInsertPatch(locator = DamageActionRetarget.Locator.class, localvars = {"info"})
        public static void ChangeTarget(DamageAction __instance, @ByRef DamageInfo[] info) {
            if (__instance.source != null) {
                AbstractCreature newTarget = __instance.target;
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (aM != __instance.source && aM.hasPower(WantedPower.POWER_ID) && !aM.isDeadOrEscaped()) {
                        newTarget = aM;
                        break; //No need to continue, just hit the first target. We can make an arraylist later if there ar more than 1 target.
                    }
                }
                __instance.target = newTarget;
                /*
                if(__instance.target != null) {
                    info[0].applyPowers(__instance.source, __instance.target);
                }*/
                /* Original code
                if (__instance.source.hasPower(WantedPower.POWER_ID) && info[0].type == DamageInfo.DamageType.NORMAL) {
                    __instance.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                    if (__instance.target != null) {
                        info[0].applyPowers(__instance.source, __instance.target);
                    }
                }*/
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = VampireDamageAction.class,
            method = "update"

    )
    public static class VampireDamageActionRetarget {
        @SpireInsertPatch(locator = VampireDamageActionRetarget.Locator.class, localvars = {"info"})
        public static void ChangeTarget(VampireDamageAction __instance, @ByRef DamageInfo[] info) {
            if (__instance.source != null) { //if source isnt null
                AbstractCreature newTarget = __instance.target; //get the old target
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) { //look through all monster...
                    if (aM != __instance.source && aM.hasPower(WantedPower.POWER_ID) && !aM.isDeadOrEscaped()) { //for the right power
                        newTarget = aM; //If you find it, thats our new target
                        break; //No need to continue, just hit the first target. We can make an arraylist later if there ar more than 1 target.
                    }
                }
                __instance.target = newTarget; //set the target
                if(__instance.target != null) { //hit that target f they are not null
                    info[0].applyPowers(__instance.source, __instance.target);
                }
            }
            /* original code
            if (__instance.source != null) { // If source isnt null
                if (__instance.source.hasPower(WantedPower.POWER_ID) && info[0].type == DamageInfo.DamageType.NORMAL) { // and source has the right power
                    __instance.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng); // get a new target
                    if (__instance.target != null) { //hit that target if they arent null
                        info[0].applyPowers(__instance.source, __instance.target);
                    }
                }
            }*/
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}