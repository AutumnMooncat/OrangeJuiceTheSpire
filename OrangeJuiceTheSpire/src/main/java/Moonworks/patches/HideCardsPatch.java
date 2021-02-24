package Moonworks.patches;

import Moonworks.cards.uniqueCards.UniqueCard;
import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.EverythingFix;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Arrays;

public class HideCardsPatch {

    @SpirePatch(
            clz = EverythingFix.Initialize.class,
            method = "Insert"
    )
    public static class CardLibraryDontDisplayPairCards {
        public static void Postfix(Object __obj_instance) {
            for (AbstractCard.CardColor color : EverythingFix.Fields.cardGroupMap.keySet()) {
                ArrayList<AbstractCard> remove = new ArrayList<>();
                for (AbstractCard card : EverythingFix.Fields.cardGroupMap.get(color).group) {
                    if (card instanceof UniqueCard) {
                        remove.add(card);
                    }
                }
                EverythingFix.Fields.cardGroupMap.get(color).group.removeAll(remove);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeCardPools"
    )
    public static class AbstractDungeonDontAddPairCards {
        public static void Postfix(AbstractDungeon __instance) {
            ArrayList<CardGroup> groups = new ArrayList<>(Arrays.asList(AbstractDungeon.srcColorlessCardPool, AbstractDungeon.srcCurseCardPool, AbstractDungeon.srcRareCardPool, AbstractDungeon.srcUncommonCardPool, AbstractDungeon.srcCommonCardPool, AbstractDungeon.colorlessCardPool, AbstractDungeon.curseCardPool, AbstractDungeon.rareCardPool, AbstractDungeon.uncommonCardPool, AbstractDungeon.commonCardPool));
            for (CardGroup group : groups) {
                ArrayList<AbstractCard> remove = new ArrayList<>();
                for (AbstractCard card : group.group) {
                    if (card instanceof UniqueCard) {
                        remove.add(card);
                    }
                }
                group.group.removeAll(remove);
            }
        }
    }
}
