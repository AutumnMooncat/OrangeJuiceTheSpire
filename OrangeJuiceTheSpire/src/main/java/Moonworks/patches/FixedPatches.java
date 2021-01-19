package Moonworks.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class FixedPatches {

    @SpirePatch(
            clz= DamageInfo.class,
            method=SpirePatch.CLASS
    )
    public static class FixedField
    {
        public static SpireField<Boolean> fixed = new SpireField<>(() -> Boolean.FALSE);
    }


}