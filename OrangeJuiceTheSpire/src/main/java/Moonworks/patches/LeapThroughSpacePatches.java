package Moonworks.patches;

import Moonworks.OrangeJuiceMod;
import Moonworks.powers.LeapThroughSpacePower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;

public class LeapThroughSpacePatches {

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class ShufflePrePatch {
        @SpirePrefixPatch
        public static void patch(EmptyDeckShuffleAction __instance) {
            if (!(boolean) ReflectionHacks.getPrivate(__instance, EmptyDeckShuffleAction.class, "shuffled")) {
                AbstractPower pow = AbstractDungeon.player.getPower(LeapThroughSpacePower.POWER_ID);
                if (pow instanceof LeapThroughSpacePower) {
                    OrangeJuiceMod.logger.info("Doing a shuffle!");
                    ((LeapThroughSpacePower) pow).determineShuffledCards();
                    for (AbstractCard c : ((LeapThroughSpacePower) pow).determinedCards) {
                        OrangeJuiceMod.logger.info("Manually removed card: "+c);
                        //AbstractDungeon.getCurrRoom().souls.shuffle(c, false);
                        //AbstractDungeon.getCurrRoom().souls.onToDeck(c, false);
                        AbstractDungeon.player.discardPile.group.remove(c);
                    }
                }
            }
        }
    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class ShufflePostPatch {
        @SpirePostfixPatch
        public static void patch(EmptyDeckShuffleAction __instance) {
            if (__instance.isDone) {
                AbstractPower pow = AbstractDungeon.player.getPower(LeapThroughSpacePower.POWER_ID);
                if (pow instanceof LeapThroughSpacePower) {
                    OrangeJuiceMod.logger.info("Shuffle completed maybe?");
                    //((LeapThroughSpacePower) pow).determineShuffledCards();
                    Collections.reverse(((LeapThroughSpacePower) pow).determinedCards);
                    for (AbstractCard c : ((LeapThroughSpacePower) pow).determinedCards) {
                        //AbstractDungeon.getCurrRoom().souls.shuffle(c, false);
                        AbstractDungeon.getCurrRoom().souls.onToDeck(c, false);
                        OrangeJuiceMod.logger.info("Re-adding card to TOP: "+c);
                        //AbstractDungeon.player.discardPile.group.remove(c);
                    }
                }
            }
        }
    }
}
