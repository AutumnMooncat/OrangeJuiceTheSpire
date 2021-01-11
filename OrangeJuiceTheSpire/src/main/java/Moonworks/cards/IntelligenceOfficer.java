package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.IntelligenceOfficerAction;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class IntelligenceOfficer extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(IntelligenceOfficer.class.getSimpleName());
    public static final String IMG = makeCardPath("IntelligenceOfficer.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int VIGORSTEADY = 3;
    private static final int UPGRADE_PLUS_VIGOR = 2;

    private static final int CARDS = 1;
    private static final int UPGRADE_PLUS_CARDS = 1;

    private static final int SCRY = 3;
    private static final int UPGRADE_PLUS_SCRY = 2;

    // /STAT DECLARATION/

    public IntelligenceOfficer() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = SCRY;
        secondMagicNumber = baseSecondMagicNumber = VIGORSTEADY;
    }
    @Override
    public float getTitleFontSize() {
        return 21F;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //AbstractDungeon.actionManager.addToBottom(new SunderAction(m, new DamageInfo(p, damage, damageTypeForTurn),defaultSecondMagicNumber));
        //AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        //AbstractDungeon.actionManager.addToBottom(new DrawCardAction(magicNumber));
        //this.addToBot(new ScryAction(defaultSecondMagicNumber));
        this.addToBot(new IntelligenceOfficerAction(magicNumber, p, secondMagicNumber, CARDS));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            //this.upgradeDamage(UPGRADE_PLUS_DMG);
            //this.upgradeBlock(UPGRADE_PLUS_BLOCK);
            this.upgradeMagicNumber(UPGRADE_PLUS_SCRY);
            //this.upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_CARDS);
            this.initializeDescription();
        }
    }
}