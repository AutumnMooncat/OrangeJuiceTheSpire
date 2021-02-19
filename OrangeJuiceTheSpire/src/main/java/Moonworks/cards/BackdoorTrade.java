package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.NormaPower;
import Moonworks.util.NormaHelper;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class BackdoorTrade extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(BackdoorTrade.class.getSimpleName());
    public static final String IMG = makeCardPath("BackdoorTrade.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NORMA5DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[1];
    public static final String NORMAL_DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    private static final int CARDS = 2;
    private static final int UPGRADE_PLUS_CARDS = 1;

    private static final int NORMA_UP = 1;

    private static final Integer[] NORMA_LEVELS = {5};

    // /STAT DECLARATION/

    public BackdoorTrade() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        magicNumber = baseMagicNumber = CARDS;
        secondMagicNumber = baseSecondMagicNumber = NORMA_UP;
        this.exhaust = true;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new LoseHPAction(p, p, magicNumber));
        NormaHelper.applyNormaPower(p);
        if (getNormaLevel() >= NORMA_LEVELS[0]) {
            this.addToBot(new DrawCardAction(magicNumber));
        }
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        this.initializeDescription();
        super.applyPowers();
    }

    @Override
    public void initializeDescription() {
        this.exhaust = getNormaLevel() < NORMA_LEVELS[0];
        DESCRIPTION = getNormaLevel() >= NORMA_LEVELS[0] ? NORMA5DESCRIPTION : NORMAL_DESCRIPTION;
        super.initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            //this.upgradeMagicNumber(UPGRADE_PLUS_CARDS);
            this.upgradeBaseCost(UPGRADE_COST);
            this.initializeDescription();
        }
    }
}