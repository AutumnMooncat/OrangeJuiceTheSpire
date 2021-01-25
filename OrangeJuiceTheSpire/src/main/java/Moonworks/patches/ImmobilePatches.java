package Moonworks.patches;
/*
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ImmobilePatches {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class ImmobileField
    {
        public static SpireField<Boolean> immobile = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"

    )
    public static class KeepBlockPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void KeepBlock(GameActionManager __instance) {

        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "clear");
                int[] hits = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{hits[hits.length - 1]};
            }
        }
    }
}
*/