package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.RecoverExhaustedGiftAction;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MixPhenomenon extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MixPhenomenon.class.getSimpleName());
    public static final String IMG = makeCardPath("MixPhenomenon.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int RESTORE = 1;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public MixPhenomenon() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = RESTORE;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = DRAW;
        this.exhaust = true;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            AbstractGiftCard.recoverSpecificExhaustedGift(magicNumber);
        } else {
            AbstractGiftCard.recoverRandomExhaustedGift(magicNumber);
        }

        this.addToBot(new DrawCardAction(defaultSecondMagicNumber));
        //this.addToBot(new DiscardPileToTopOfDeckAction());
        //this.addToBot(new ExhumeAction());
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            //upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
        }
    }
}
