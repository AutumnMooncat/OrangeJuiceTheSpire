package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.WeakPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class ForcedRevival extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ForcedRevival.class.getSimpleName());
    public static final String IMG = makeCardPath("ForcedRevival.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int HEAL = 4;
    private static final int UPGRADE_PLUS_HEAL = 2;

    private static final int ENERGY = 1;
    private static final int UPGRADE_PLUS_ENERGY = 1;

    private static final int DEBUFF = 2;

    private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/

    public ForcedRevival() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);

        magicNumber = baseMagicNumber = HEAL;
        secondMagicNumber = baseSecondMagicNumber = ENERGY;
        invertedNumber = baseInvertedNumber = DEBUFF;
        exhaust = true;
        this.tags.add(CardTags.HEALING);
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INVERTEDMOD, -1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new HealAction(p, p, this.magicNumber));
        this.addToBot(new GainEnergyAction(this.secondMagicNumber));
        if (invertedNumber > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new WeakPower(p, this.invertedNumber, false)));
        }

    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            this.upgradeSecondMagicNumber(UPGRADE_PLUS_ENERGY);
            this.initializeDescription();
        }
    }
}