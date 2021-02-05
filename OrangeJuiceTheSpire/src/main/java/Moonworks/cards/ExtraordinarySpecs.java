package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporaryDexterityPower;
import Moonworks.powers.TemporaryStrengthPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class ExtraordinarySpecs extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ExtraordinarySpecs.class.getSimpleName());
    public static final String IMG = makeCardPath("ExtraordinarySpecs.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    private static final int BUFFS = 4;
    private static final int UPGRADE_PLUS_BUFFS = 1;

    private static final int DRAW = 1;


    // /STAT DECLARATION/


    public ExtraordinarySpecs() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = BUFFS;
        this.secondMagicNumber = this.baseSecondMagicNumber = DRAW;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Apply Temp Str
        this.addToBot(new ApplyPowerAction(p, p, new TemporaryStrengthPower(p, magicNumber)));

        //Apply Temp Dex
        this.addToBot(new ApplyPowerAction(p, p, new TemporaryDexterityPower(p, magicNumber)));

        //Draw cards
        this.addToBot(new DrawCardAction(secondMagicNumber));
    }

    @Override
    public float getTitleFontSize() {
        return 22F;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            //upgradeMagicNumber(UPGRADE_PLUS_BUFFS);
            initializeDescription();
        }
    }
}
