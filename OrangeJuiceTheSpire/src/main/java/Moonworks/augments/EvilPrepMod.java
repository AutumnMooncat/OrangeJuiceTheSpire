package Moonworks.augments;

import CardAugments.cardmods.AbstractAugment;
import CardAugments.cardmods.DynvarCarrier;
import CardAugments.patches.MultiPreviewFieldPatches;
import Moonworks.OrangeJuiceMod;
import Moonworks.cards.tempCards.EvilSpyWorkExecution;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EvilPrepMod extends AbstractAugment implements DynvarCarrier {
    public static final String ID = OrangeJuiceMod.makeID("EvilPrepMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    private static final String KEY = "!" + ID + "!";

    private static final int CARDS = 2;
    int amount = 0;
    int base = -1;

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        amount = (int) Math.ceil((damage * (1 - MAJOR_DEBUFF))/4f);
        if (base == -1) {
            base = amount;
        }
        return damage * MAJOR_DEBUFF;
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return OrangeJuiceMod.enableChimeraCrossover && card.baseDamage > 1 && card.type != AbstractCard.CardType.POWER && cardCheck(card, c -> doesntUpgradeCost());
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        String[] nameParts = removeUpgradeText(cardName);
        return TEXT[0] + nameParts[0] + TEXT[1] + nameParts[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], KEY);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new MakeTempCardInDrawPileAction(new EvilSpyWorkExecution(), amount, true, true));
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

    @Override
    public String key() {
        return ID;
    }

    @Override
    public int val(AbstractCard abstractCard) {
        return amount;
    }

    @Override
    public int baseVal(AbstractCard abstractCard) {
        return amount;
    }

    @Override
    public boolean modified(AbstractCard abstractCard) {
        return false;
    }

    @Override
    public boolean upgraded(AbstractCard abstractCard) {
        return amount != base;
    }
}
