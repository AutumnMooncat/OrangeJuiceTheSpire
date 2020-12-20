package Moonworks.cards;

import Moonworks.actions.ModifyCostThisCombatAction;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class NicePresent extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(NicePresent.class.getSimpleName());
    public static final String IMG = makeCardPath("NicePresent.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int DRAW = 2;
    private static final int INCREASE_COST = 1;
    private static final int MAX_COST = 3;
    private static final int UPGRADE_MAX_COST = -1;

    // /STAT DECLARATION/

    public NicePresent() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = MAX_COST;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(p, this.magicNumber));
        if(this.cost < this.defaultSecondMagicNumber) {
            this.addToBot(new ModifyCostThisCombatAction(this, INCREASE_COST));
        }
    }

    @Override
    public void applyPowers() {
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        if (getNormaLevel() >= 3 ) {
            this.magicNumber += 1;
            this.isMagicNumberModified = true;
        }
        initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDefaultSecondMagicNumber(UPGRADE_MAX_COST);
            this.initializeDescription();
        }
    }
}