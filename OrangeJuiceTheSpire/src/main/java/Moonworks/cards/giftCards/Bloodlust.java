package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.cards.interfaces.OnDealDamageCard;
import Moonworks.cards.interfaces.OnLoseHPLastCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporaryStrengthPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Bloodlust extends AbstractGiftCard implements OnDealDamageCard, OnLoseHPLastCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Bloodlust.class.getSimpleName());
    public static final String IMG = makeCardPath("Bloodlust.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;

    private static final int TEMP_STR = 2;
    private static final int UPGRADE_PLUS_TEMP_STR = 1;

    private static final int USES = 4;
    private static final int UPGRADE_PLUS_USES = 1; // Maybe this?

    private boolean activatedThisTurn;

    //private static final int UPGRADE_PLUS_RETAINS = 1;

    // /STAT DECLARATION/

    //TODO balance?
    public Bloodlust() {
        this(USES, false);
    }

    public Bloodlust(int currentUses, boolean checkedGolden) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        this.magicNumber = this.baseMagicNumber = TEMP_STR;
    }

    public void applyStrength() {
        if (isActive()) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TemporaryStrengthPower(AbstractDungeon.player, magicNumber)));
            if (!activatedThisTurn) {
                activatedThisTurn = true;
            }
            //If it already activated this turn, we still get the damage negation but we don't consume a use
            flash();
        }
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if (activatedThisTurn) {
            activatedThisTurn = false;
            applyEffect();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_TEMP_STR);
            //upgradeSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Bloodlust(secondMagicNumber, checkedGolden);
    }

    @Override
    public int onDealDamage(AbstractMonster m, DamageInfo info, int damageAmount) {
        applyStrength();
        return damageAmount;
    }

    @Override
    public int onLoseHpLast(DamageInfo info, int damageAmount) {
        /*if (info.owner == AbstractDungeon.player) {
            applyStrength();
        }*/
        return damageAmount;
    }
}
