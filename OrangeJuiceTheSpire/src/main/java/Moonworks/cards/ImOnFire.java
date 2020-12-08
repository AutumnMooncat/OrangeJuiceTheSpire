package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.DoubleDamagePower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class ImOnFire extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ImOnFire.class.getSimpleName());
    public static final String IMG = makeCardPath("ImOnFire.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int VIGOR = 10;
    private static final int UPGRADE_PLUS_VIGOR = 4;

    private static final int VULNERABLE = 1;
    //private static final int UPGRADE_PLUS_VULNERABLE = 1;

    // /STAT DECLARATION/


    public ImOnFire() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //this.block = this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = VIGOR;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = VULNERABLE;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (getNormaLevel() >= 3) {
            this.addToBot(new ApplyPowerAction(p, p, new DoubleDamagePower(p, 1, false)));
            if (upgraded) {
                this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, UPGRADE_PLUS_VIGOR)));
            }
        } else {
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
        }
        this.addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, defaultSecondMagicNumber, true)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_VIGOR);
            //upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_VULNERABLE);
            initializeDescription();
        }
    }
}
