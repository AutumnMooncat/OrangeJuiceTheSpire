package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.variables.DefaultSecondMagicNumber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class BeyondHell extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(BeyondHell.class.getSimpleName());
    public static final String IMG = makeCardPath("BeyondHell.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int MULTIPLE = 2;
    private static final int UPGRADE_PLUS_MULTIPLE = 1;

    private static final int DIVISOR = 5;
    private static final int NORMA_DIVISOR = 3;

    // /STAT DECLARATION/


    public BeyondHell() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = MULTIPLE;
        this.invertedNumber = this.baseInvertedNumber = DIVISOR;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = ((p.maxHealth - p.currentHealth) / invertedNumber) * magicNumber;
        if (effect > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, effect)));
        }
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
        this.invertedNumber = this.baseInvertedNumber;
        if (getNormaLevel() >= 5) {
            this.invertedNumber = NORMA_DIVISOR;
            this.isInvertedNumberModified = true;
        }
        initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_MULTIPLE);
            initializeDescription();
        }
    }
}
