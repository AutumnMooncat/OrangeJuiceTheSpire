package Moonworks.augments;

import CardAugments.cardmods.AbstractAugment;
import CardAugments.cardmods.DynvarCarrier;
import Moonworks.OrangeJuiceMod;
import Moonworks.cards.Rbits;
import Moonworks.powers.SteadyPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SteadyMod extends AbstractAugment implements DynvarCarrier {
    public static final String ID = OrangeJuiceMod.makeID("SteadyMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] EXTRA_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private static final String KEY = "!" + ID + "!";

    private int amount = 0;
    private boolean setBaseVar;

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card instanceof Rbits) {
            setBaseVar = true;
        }
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        amount = (int) Math.ceil(block * (1 - MINOR_DEBUFF));
        return block * MINOR_DEBUFF;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card instanceof Rbits) {
            return magic + amount;
        }
        return magic;
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return card.cost != -2 && card.baseBlock > 1;
    }

    @Override
    public String getPrefix() {
        return TEXT[0];
    }

    @Override
    public String getSuffix() {
        return TEXT[1];
    }

    @Override
    public String getAugmentDescription() {
        return EXTRA_TEXT[0];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (setBaseVar) {
            return rawDescription;
        }
        return insertAfterText(rawDescription , String.format(TEXT[2], KEY));
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!setBaseVar) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SteadyPower(AbstractDungeon.player, amount)));
        }
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.COMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SteadyMod();
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
    public int val(AbstractCard card) {
        return amount;
    }

    @Override
    public int baseVal(AbstractCard card) {
        return amount;
    }

    @Override
    public boolean modified(AbstractCard card) {
        return false;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return card.upgradedBlock;
    }
}
