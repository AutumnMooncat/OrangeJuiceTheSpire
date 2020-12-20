package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.IndiscriminateFireSupportAction;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.FreeCardPower;
import Moonworks.variables.DefaultSecondMagicNumber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class IndiscriminateFireSupport extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(IndiscriminateFireSupport.class.getSimpleName());
    public static final String IMG = makeCardPath("IndiscriminateFireSupport.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -1;

    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 4;

    private static final int DAZED = 2;
    private static final int UPGRADE_PLUS_DAZED = 1;

    // /STAT DECLARATION/


    public IndiscriminateFireSupport() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        invertedNumber = baseInvertedNumber = DAZED;
        this.isMultiDamage = true;
        this.cardsToPreview = new Dazed();
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }
    @Override
    public float getTitleFontSize() {
        return 21F;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new IndiscriminateFireSupportAction(p, multiDamage, damageTypeForTurn, invertedNumber, AbstractGameAction.AttackEffect.FIRE, freeToPlayOnce, energyOnUse));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.invertedNumber = this.baseInvertedNumber;
        this.isInvertedNumberModified = false;
        initializeDescription();
    }
    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        this.invertedNumber = this.baseInvertedNumber;
        if (getNormaLevel() >= 5) {
            this.invertedNumber -= 1;
            this.isInvertedNumberModified = true;
        }
        initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            //upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_DAZED);
            initializeDescription();
        }
    }
}
