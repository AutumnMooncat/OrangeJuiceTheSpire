package Moonworks.augments;

import CardAugments.cardmods.AbstractAugment;
import CardAugments.cardmods.DynvarCarrier;
import Moonworks.OrangeJuiceMod;
import Moonworks.powers.BlastingLightPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlastingMod extends AbstractAugment implements DynvarCarrier {
    public static final String ID = OrangeJuiceMod.makeID("BlastingMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    private static final String KEY = "!" + ID + "!";

    private int amount = 0;

    @Override
    public boolean validCard(AbstractCard card) {
        return OrangeJuiceMod.enableChimeraCrossover && card.cost != -2 && card.baseDamage > 6 && card.type == AbstractCard.CardType.ATTACK && cardCheck(card, c -> usesEnemyTargeting(c));
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        amount = (int) Math.ceil(damage * (1 - MAJOR_DEBUFF));
        return damage * MAJOR_DEBUFF;
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
        if (target != null && !target.isDeadOrEscaped()) {
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new BlastingLightPower(target, amount)));
        }
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.RARE;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BlastingMod();
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
        return abstractCard.upgradedDamage;
    }
}
