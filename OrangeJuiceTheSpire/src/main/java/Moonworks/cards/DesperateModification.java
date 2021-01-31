package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.CookingTimeAction;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class DesperateModification extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(DesperateModification.class.getSimpleName());
    public static final String IMG = makeCardPath("DesperateModification.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    private static final int BLOCK = 0;
    private static final int DISPLAY_BLOCK = 12;
    private static final int UPGRADE_PLUS_DISPLAY_BLOCK = 4;

    private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/

    public DesperateModification() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DISPLAY_BLOCK;
        block = baseBlock = BLOCK;
        //this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (damage > 0) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
        if (block > 0) {
            this.addToBot(new GainBlockAction(p, p, block));
            this.exhaust = true;
        }
    }

    @Override
    public void applyPowers() {
        baseBlock = baseMagicNumber;
        super.applyPowers();
        magicNumber = block;
        baseBlock = block = 0;
        rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public float getTitleFontSize() {
        return 18F;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        boolean attacking = mo.getIntentBaseDmg() > 0;
        if (attacking) {
            applyPowers();
            block = magicNumber;
            damage = 0;
            rawDescription = EXTENDED_DESCRIPTION[0];
        } else {
            block = baseBlock = 0;
        }
        isBlockModified = block != baseBlock;
        isDamageModified = damage != baseDamage;
        initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_PLUS_DISPLAY_BLOCK);
            this.initializeDescription();
        }
    }
}