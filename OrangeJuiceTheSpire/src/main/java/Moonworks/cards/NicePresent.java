package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ModifyCostThisCombatAction;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class NicePresent extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(NicePresent.class.getSimpleName());
    public static final String IMG = makeCardPath("NicePresent.png");

    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String SINGLEDRAWNAME = cardStrings.DESCRIPTION;
    public static final String MULTIDRAWNAME = cardStrings.EXTENDED_DESCRIPTION[1];
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int DRAW = 1;
    private static final int INCREASE_COST = 1;
    private static final int MAX_COST = 3;
    private static final int UPGRADE_MAX_COST = -1;

    private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/

    public NicePresent() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.secondMagicNumber = this.baseSecondMagicNumber = MAX_COST;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(p, this.magicNumber));
        //if (AbstractDungeon.cardRandomRng.random(0, 1) == 1) {
            this.baseMagicNumber++;
            this.magicNumber = this.baseMagicNumber;
            if(this.cost < this.secondMagicNumber) {
                this.addToBot(new ModifyCostThisCombatAction(this, INCREASE_COST));
            }
            initializeDescription();
        //}
    }

    @Override
    public void initializeDescription() {
        if (this.magicNumber > 1) {
            this.DESCRIPTION = MULTIDRAWNAME;
        } else {
            this.DESCRIPTION = SINGLEDRAWNAME;
        }
        super.initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeSecondMagicNumber(UPGRADE_MAX_COST);
            this.initializeDescription();
        }
    }
}