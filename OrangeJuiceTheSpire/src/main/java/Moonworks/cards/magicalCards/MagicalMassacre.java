package Moonworks.cards.magicalCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.BlastingLoseHPAction;
import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.NormaPower;
import Moonworks.powers.SteadyPower;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MagicalMassacre extends AbstractMagicalCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MagicalMassacre.class.getSimpleName());
    public static final String IMG = makeCardPath("MagicalMassacre.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int HP_LOSS = 8;
    private static final int UPGRADE_PLUS_HP_LOSS = 4;

    private static final int STEADY = 5;
    private static final int UPGRADE_PLUS_STEADY = 2;

    private static final Integer[] NORMA_LEVELS = {-1};
    // /STAT DECLARATION/


    public MagicalMassacre() {
        this(0);
    }

    public MagicalMassacre(int normaLevel) {
        this(normaLevel, normaLevel);
    }

    public MagicalMassacre(int currentCharges, int maxCharges) {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET, currentCharges, maxCharges);
        damage = baseDamage = HP_LOSS;
        secondMagicNumber = baseSecondMagicNumber = STEADY;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Grab a random target
        AbstractMonster target = AbstractDungeon.getRandomMonster();

        //Check if there is a healthier target
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped() && mon.currentHealth > target.currentHealth) {
                target = mon;
            }
        }

        //Blast em
        this.addToBot(new BlastingLoseHPAction(p, target, damage, AbstractGameAction.AttackEffect.FIRE));

        //Apply Steady
        this.addToBot(new ApplyPowerAction(p, p, new SteadyPower(p, secondMagicNumber)));

        //Regain 1 Norma
        this.addToBot(new ApplyPowerAction(p, p, new NormaPower(p, NORMA_RECHARGE)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_HP_LOSS);
            upgradeSecondMagicNumber(UPGRADE_PLUS_STEADY);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicalMassacre(ExhaustiveField.ExhaustiveFields.exhaustive.get(this), ExhaustiveField.ExhaustiveFields.baseExhaustive.get(this));
    }
}