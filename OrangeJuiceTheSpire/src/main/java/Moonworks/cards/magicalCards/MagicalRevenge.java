package Moonworks.cards.magicalCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.HealPercentileDamageAction;
import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.cards.interfaces.RevengeAttack;
import Moonworks.characters.TheStarBreaker;
import Moonworks.util.RevengeHelper;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MagicalRevenge extends AbstractMagicalCard implements RevengeAttack {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MagicalRevenge.class.getSimpleName());
    public static final String IMG = makeCardPath("MagicalRevenge.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int HEAL_PERCENT = 33;
    private static final int UPGRADE_PLUS_HEAL_PERCENT = 17;

    private static final int DAMAGE_PERCENT = 25;
    private static final int UPGRADE_PLUS_DAMAGE_PERCENT = 8;

    private static final int DEFAULT_DAMAGE = 0;

    // /STAT DECLARATION/


    public MagicalRevenge() {
        this(0);
    }

    public MagicalRevenge(int normaLevel) {
        this(normaLevel, normaLevel);
    }

    public MagicalRevenge(int currentCharges, int maxCharges) {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET, currentCharges, maxCharges);
        damage = baseDamage = DAMAGE_PERCENT;
        magicNumber = baseMagicNumber = HEAL_PERCENT;
        secondMagicNumber = baseSecondMagicNumber = DEFAULT_DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        //Do the damage plus heal action
        this.addToBot(new HealPercentileDamageAction(p, m, new DamageInfo(p, secondMagicNumber, damageTypeForTurn), magicNumber, AbstractGameAction.AttackEffect.FIRE));

        //Regain 1 Norma
        restoreNorma();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        secondMagicNumber = MathUtils.floor((AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth)*damage/100f);
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE_PERCENT);
            upgradeMagicNumber(UPGRADE_PLUS_HEAL_PERCENT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicalRevenge(ExhaustiveField.ExhaustiveFields.exhaustive.get(this), ExhaustiveField.ExhaustiveFields.baseExhaustive.get(this));
    }
}