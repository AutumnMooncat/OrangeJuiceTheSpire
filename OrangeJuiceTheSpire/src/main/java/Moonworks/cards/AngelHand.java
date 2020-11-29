package Moonworks.cards;

import Moonworks.actions.FindAndReplaceCardAction;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class AngelHand extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(AngelHand.class.getSimpleName());
    public static final String IMG = makeCardPath("AngelHand.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int BLOCK = 10;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    // /STAT DECLARATION/


    public AngelHand() {
        this(true);
    }
    public AngelHand(final boolean setPreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = BLOCK;
        this.selfRetain = true;
        if (setPreview) {
            this.cardsToPreview = new DevilHand(false);
        }
        //this.cardsToPreview = new DevilHand();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void onRetained() {
        super.onRetained();
        //int index = AbstractDungeon.player.hand.group.indexOf(this);
        //this.addToBot(new TransformCardInHandAction(index, cardsToPreview.makeStatEquivalentCopy()));
        this.addToBot(new FindAndReplaceCardAction(this, cardsToPreview.makeStatEquivalentCopy()));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if(cardsToPreview != null) {
                cardsToPreview.upgrade();
            }
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
