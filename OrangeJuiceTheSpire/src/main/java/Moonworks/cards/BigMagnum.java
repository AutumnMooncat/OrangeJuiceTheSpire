package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.BigMagnumAction;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporaryStrengthPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class BigMagnum extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(BigMagnum.class.getSimpleName());
    public static final String IMG = makeCardPath("BigMagnum.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String UPGRADE1NAME = cardStrings.EXTENDED_DESCRIPTION[1];
    public static final String UPGRADE2NAME = cardStrings.EXTENDED_DESCRIPTION[2];
    public static final String UPGRADE3NAME = cardStrings.EXTENDED_DESCRIPTION[3];
    public static final String UPGRADE4NAME = cardStrings.EXTENDED_DESCRIPTION[4];
    public static final String UPGRADE5NAME = cardStrings.EXTENDED_DESCRIPTION[5];


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int VIGOR = 4;

    private static final int VIGOR_SCALE = 4;
    private static final int UPGRADE_PLUS_VIGOR_SCALE = 2;

    //private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/

    public BigMagnum() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET/*, NORMA_LEVELS*/);
        magicNumber = baseMagicNumber = VIGOR;
        secondMagicNumber = baseSecondMagicNumber = VIGOR_SCALE;
        //CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //boolean normaCheck = getNormaLevel() >= NORMA_LEVELS[0];
        //this.addToBot(new BigMagnumAction(this, normaCheck, EXTENDED_DESCRIPTION[6]));
        this.addToBot(new BigMagnumAction(this, false, EXTENDED_DESCRIPTION[6]));
    }

    @Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        switch (timesUpgraded) {
            case 1:
                this.name = UPGRADE1NAME;
                break;
            case 2:
                this.name = UPGRADE2NAME;
                break;
            case 3:
                this.name = UPGRADE3NAME;
                break;
            case 4:
                this.name = UPGRADE4NAME;
                break;
            case 5:
            default:
                this.name = UPGRADE5NAME;
                break;

        }

        this.initializeTitle();
    }

    @Override
    public boolean canUpgrade() {
        return this.timesUpgraded < 5;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (canUpgrade()) {
            //Reverse order so the first upgrade is 2+0, then 2+1, etc.
            this.upgradeSecondMagicNumber(UPGRADE_PLUS_VIGOR_SCALE + this.timesUpgraded);
            this.upgradeName();
            this.initializeDescription();
        }
    }
}