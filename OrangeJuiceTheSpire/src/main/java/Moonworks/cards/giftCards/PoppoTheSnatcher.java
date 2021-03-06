package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.cards.interfaces.OnEnemyBuffCard;
import Moonworks.characters.TheStarBreaker;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class PoppoTheSnatcher extends AbstractGiftCard implements OnEnemyBuffCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(PoppoTheSnatcher.class.getSimpleName());
    public static final String IMG = makeCardPath("PoppoTheSnatcher.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;

    private static final int USES = 3;
    private static final int UPGRADE_PLUS_USES = 1;

    // /STAT DECLARATION/


    public PoppoTheSnatcher() {
        this(USES, false);
    }
    public PoppoTheSnatcher(int currentUses, boolean checkedGolden) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden, true);
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_USES);
            //upgradeMagicNumber(UPGRADE_PLUS_POSITIVE_EFFECT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PoppoTheSnatcher(secondMagicNumber, checkedGolden);
    }

    @Override
    public void powerApplied(ApplyPowerAction action, AbstractCreature target, AbstractCreature source, AbstractPower pow, int amount, AbstractGameAction.AttackEffect effect) {
        AbstractPlayer p = AbstractDungeon.player;
        //If we have more than 1 copy of this gift, then this can occur, we use this to get an extra copy for each Gift after the first
        if (target == p && pow instanceof CloneablePowerInterface && isActive()) {
            this.addToBot(new ApplyPowerAction(p, p, ((CloneablePowerInterface) pow).makeCopy(), amount, effect));
            applyEffect();
        }
    }
}
