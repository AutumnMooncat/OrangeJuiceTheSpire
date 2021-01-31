package Moonworks.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TypeOverridePatch {

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class TypeOverrideField
    {
        public static SpireField<String> typeOverride = new SpireField<>(() -> null);
    }


}
