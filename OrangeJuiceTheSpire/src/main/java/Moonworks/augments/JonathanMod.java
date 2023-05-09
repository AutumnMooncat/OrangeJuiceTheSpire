/*
package Moonworks.augments;

import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;
import CardAugments.patches.MultiPreviewFieldPatches;
import Moonworks.OrangeJuiceMod;
import Moonworks.cards.JonathanRush;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class JonathanMod extends AbstractAugment {
    public static final String ID = OrangeJuiceMod.makeID("JonathanMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] EXTRA_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    private static final int CARDS = 1;

    @Override
    public boolean validCard(AbstractCard card) {
        return card.cost != -2 && card.baseDamage > 1 && card.type == AbstractCard.CardType.ATTACK;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        MultiPreviewFieldPatches.addPreview(card, new JonathanRush());
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage * MODERATE_DEBUFF;
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
        return insertAfterText(rawDescription , String.format(TEXT[2], CARDS));
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new MakeTempCardInHandAction(new JonathanRush(), CARDS));
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.UNCOMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new JonathanMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
*/
