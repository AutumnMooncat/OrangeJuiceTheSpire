package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporaryDexterityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class QuickRestoration extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(QuickRestoration.class.getSimpleName());
    public static final String IMG = makeCardPath("QuickRestoration.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int BASE_BLOCK = 3;

    private static final int BLOCKS = 2;
    private static final int UPGRADE_PLUS_BLOCKS = 1;

    // /STAT DECLARATION/


    public QuickRestoration() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //this.block = this.baseBlock = BLOCK;
        this.block = this.baseBlock = BASE_BLOCK;
        this.magicNumber = this.baseMagicNumber = BLOCKS;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Gain block !M! times.
        for (int i = 0 ; i < magicNumber ; i++) {
            this.addToBot(new GainBlockAction(p, p, block)); //Do our block action.
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_BLOCKS);
            initializeDescription();
        }
    }
}
