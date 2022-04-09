package Moonworks.augments;

import CardAugments.cardmods.AbstractAugment;
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

public class SteadyMod extends AbstractAugment {
    public static final String ID = OrangeJuiceMod.makeID("SteadyMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private int amount = 0;
    private boolean setBaseVar;

    @Override
    public void onInitialApplication(AbstractCard card) {
        amount += card.baseBlock;
        modifyBaseStat(card, BuffType.BLOCK, BuffScale.MINOR_DEBUFF);
        amount -= card.baseBlock;
        if (card instanceof Rbits) {
            card.baseMagicNumber += amount;
            card.magicNumber += amount;
            setBaseVar = true;
        }
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return OrangeJuiceMod.enableChimeraCrossover && card.cost != -2 && card.baseBlock > 1;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TEXT[0] + cardName + TEXT[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (setBaseVar) {
            return rawDescription;
        }
        return rawDescription + String.format(TEXT[2], amount);
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
}
