package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.SinkOrSwimPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;
//@AutoAdd.Ignore
public class SinkOrSwim extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(SinkOrSwim.class.getSimpleName());
    public static final String IMG = makeCardPath("SinkOrSwim.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int UPGRADE_REDUCED_COST = 0;
    private static final int ENERGY = 2;
    private static final int DEBUFFS = 3;
    //private static final int UPGRADE_PLUS_DEBUFFS = -1;

    // /STAT DECLARATION/


    public SinkOrSwim() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = ENERGY;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = DEBUFFS;
        //this.exhaust = true; //Maybe?

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new SinkOrSwimPower(p, this.magicNumber, this.defaultSecondMagicNumber)));
        //this.addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, defaultSecondMagicNumber, false)));
        //this.addToBot(new ApplyPowerAction(p, p, new WeakPower(p, defaultSecondMagicNumber, false)));
        //this.addToBot(new ApplyPowerAction(p, p, new FrailPower(p, defaultSecondMagicNumber, false)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_REDUCED_COST);
            //upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_DEBUFFS);
            initializeDescription();
        }
    }
}
