package Moonworks.augments;

import CardAugments.cardmods.AbstractAugment;
import Moonworks.OrangeJuiceMod;
import Moonworks.powers.Heat300PercentPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HeatedMod extends AbstractAugment {
    public static final String ID = OrangeJuiceMod.makeID("HeatedMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int AMOUNT = 1;

    @Override
    public boolean validCard(AbstractCard card) {
        return OrangeJuiceMod.enableChimeraCrossover && card.cost != -2 && card.baseDamage >= 8 && card.type == AbstractCard.CardType.ATTACK && cardCheck(card, c -> usesEnemyTargeting());
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage * MODERATE_DEBUFF;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        String[] nameParts = removeUpgradeText(cardName);
        return TEXT[0] + nameParts[0] + TEXT[1] + nameParts[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], AMOUNT);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (target != null && !target.isDeadOrEscaped()) {
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new Heat300PercentPower(target, AbstractDungeon.player, AMOUNT, 30)));
        }
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.UNCOMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new HeatedMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
