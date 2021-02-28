package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.cards.interfaces.OnLoseHPLastCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MetallicMonocoque extends AbstractGiftCard implements OnLoseHPLastCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MetallicMonocoque.class.getSimpleName());
    public static final String IMG = makeCardPath("MetallicMonocoqueSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;

    private static final int DAMAGE_REDUCE = 5;
    private static final int UPGRADE_PLUS_DAMAGE_REDUCE = 1;

    private static final int USES = 3;
    private static final int UPGRADE_PLUS_USES = 1;

    private boolean activatedThisTurn;

    // /STAT DECLARATION/

    //TODO balance?
    public MetallicMonocoque() {
        this(USES, false);
    }

    public MetallicMonocoque(int currentUses, boolean checkedGolden) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        this.magicNumber = this.baseMagicNumber = DAMAGE_REDUCE;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_USES);
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_REDUCE);
            initializeDescription();
        }
    }

    @Override
    public int onLoseHpLast(DamageInfo info, int damageAmount) {
        if (isActive()) {
            //We care only if damage is greater than 0
            if (damageAmount > 0) {
                //Lower the damage, but don't let it go past 0
                damageAmount = Math.max(0, damageAmount - magicNumber);
                //If this is the first activation this turn, lower uses by 1
                if (!activatedThisTurn) {
                    activatedThisTurn = true;
                }
                //If it already activated this turn, we still get the damage negation but we don't consume a use
                flash();
            }
        }
        return damageAmount;
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if (activatedThisTurn) {
            applyEffect();
            activatedThisTurn = false;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MetallicMonocoque(secondMagicNumber, checkedGolden);
    }
}
