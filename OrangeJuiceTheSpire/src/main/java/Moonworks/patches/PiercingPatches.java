package Moonworks.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class PiercingPatches {

    @SpirePatch(
            clz= DamageInfo.class,
            method=SpirePatch.CLASS
    )
    public static class PiercingField
    {
        public static SpireField<Boolean> piercing = new SpireField<>(() -> Boolean.FALSE);
    }


}
