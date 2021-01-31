package Moonworks.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class MemoryAssociationPatch {

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class MemoryAssociation
    {
        public static final SpireField<ArrayList<AbstractPower>> associations = new SpireField<>(ArrayList::new);
    }
}
