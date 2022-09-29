/*
package Moonworks.augments;

import CardAugments.cardmods.AbstractAugment;
import CardAugments.patches.MultiPreviewFieldPatches;
import Moonworks.OrangeJuiceMod;
import Moonworks.cards.tempCards.EvilSpyWorkExecution;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class EvilPrepMod extends AbstractAugment {
    public static final String ID = OrangeJuiceMod.makeID("EvilPrepMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int CARDS = 2;

    @Override
    public void onInitialApplication(AbstractCard card) {
        MultiPreviewFieldPatches.addPreview(card, new EvilSpyWorkExecution());
        card.cost = card.cost + 1;
        card.costForTurn = card.cost;
    }

    //TODO rework
    @Override
    public boolean validCard(AbstractCard card) {
        return false;
        //return OrangeJuiceMod.enableChimeraCrossover && card.cost >= 0 && card.type != AbstractCard.CardType.POWER && cardCheck(card, c -> doesntUpgradeCost());
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        String[] nameParts = removeUpgradeText(cardName);
        return TEXT[0] + nameParts[0] + TEXT[1] + nameParts[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], CARDS);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new MakeTempCardInDrawPileAction(new EvilSpyWorkExecution(), CARDS, true, true));
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.COMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new EvilPrepMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
*/
