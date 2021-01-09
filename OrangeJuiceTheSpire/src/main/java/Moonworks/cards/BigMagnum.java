package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class BigMagnum extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(BigMagnum.class.getSimpleName());
    public static final String IMG = makeCardPath("BigMagnum.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String UPGRADE1NAME = cardStrings.EXTENDED_DESCRIPTION[1];
    public static final String UPGRADE2NAME = cardStrings.EXTENDED_DESCRIPTION[2];

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int HPLOSS = 5;
    private static final int UPGRADE_PLUS_HPLOSS = 1;

    private static final int VIGOR = 7;
    private static final int UPGRADE_PLUS_VIGOR = 3;

    private static final int STR = 0;
    private static final int UPGRADE_PLUS_STR = 1;

    private static final Integer[] NORMA_LEVELS = {-1};

    // /STAT DECLARATION/

    public BigMagnum() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        invertedNumber = baseInvertedNumber = HPLOSS;
        magicNumber = baseMagicNumber = VIGOR;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = STR;
        this.exhaust = true;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.SECONDMAGICMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new LoseHPAction(p, p, invertedNumber));
        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber), magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, defaultSecondMagicNumber), defaultSecondMagicNumber));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber;
        this.isDefaultSecondMagicNumberModified = false;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber + getNormaLevel();
        this.isDefaultSecondMagicNumberModified = getNormaLevel() != 0;
        initializeDescription();
    }

    @Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = UPGRADE1NAME;
        this.initializeTitle();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_VIGOR);
            this.upgradeInvertedNumber(UPGRADE_PLUS_HPLOSS);
            this.upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_STR);
            this.initializeDescription();
        }
    }
}