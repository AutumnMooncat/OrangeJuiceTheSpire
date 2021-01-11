package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class WindyEnchantment extends AbstractGiftCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(WindyEnchantment.class.getSimpleName());
    public static final String IMG = makeCardPath("WindyEnchantment.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;
    private static final int DRAW_POWER = 1;
    private static final int INITIAL_DRAW = 2;
    private static final int USES = 3;
    private static final int UPGRADE_PLUS_USES = 1;

    // /STAT DECLARATION/


    public WindyEnchantment() {
        this(USES, false);
    }

    public WindyEnchantment(int currentUses, boolean checkedGolden) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        magicNumber = baseMagicNumber = DRAW_POWER;
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if(active){
            AbstractPlayer p = AbstractDungeon.player;
            this.addToBot(new DrawCardAction(INITIAL_DRAW));
            this.applyEffect();
            //this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, magicNumber)));
        }
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if(active) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToBot(new DrawCardAction(magicNumber));
            this.applyEffect();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WindyEnchantment(secondMagicNumber, checkedGolden);
    }
}