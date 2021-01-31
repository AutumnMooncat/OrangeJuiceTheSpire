package Moonworks.cards.magicalCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.HealPercentileDamageAction;
import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.cards.interfaces.RevengeAttack;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.MeltingMemoriesPower;
import Moonworks.powers.NormaPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

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

    private static final Integer[] NORMA_LEVELS = {-1};

    private boolean appliedRevengePower = false;

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

        //Calculate damage based on missing hp
        //int dmg = MathUtils.floor((p.maxHealth - p.currentHealth)*damage/100f);

        //Do the damage plus heal action
        this.addToBot(new HealPercentileDamageAction(p, m, new DamageInfo(p, secondMagicNumber, damageTypeForTurn), magicNumber, AbstractGameAction.AttackEffect.FIRE));

        //Regain 1 Norma
        this.addToBot(new ApplyPowerAction(p, p, new NormaPower(p, NORMA_RECHARGE)));
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (RevengePower.lastAttacker == mo) {
            damage = MathUtils.floor(damage*1.5f);
            isDamageModified = damage != baseDamage;
            //this.flash(Color.RED.cpy());
            RevengeVFXContainer.flashVFX(this);
        }
        secondMagicNumber = MathUtils.floor((AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth)*damage/100f);
        isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
    }

    @Override
    public void update() {
        if(!appliedRevengePower && AbstractDungeon.player != null) {
            if (!AbstractDungeon.player.hasPower(RevengePower.POWER_ID)) {
                applyRevengePower();
            }
        }
        super.update();
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


    @Override
    public void applyRevengePower() {
        if(!appliedRevengePower) {
            AbstractDungeon.player.powers.add(new RevengePower(AbstractDungeon.player));
            appliedRevengePower = true;
        }
    }
}