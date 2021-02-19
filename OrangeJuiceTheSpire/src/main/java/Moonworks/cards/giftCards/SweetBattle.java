package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class SweetBattle extends AbstractGiftCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(SweetBattle.class.getSimpleName());
    public static final String IMG = makeCardPath("SweetBattle.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.NONE;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    public static final AbstractCard.CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;
    private static final int CARDS = 1;

    private static final int USES = 3;
    private static final int UPGRADE_PLUS_USES = 1;

    private static final int HEAL = 2;
    private static final int UPGRADE_PLUS_HEAL = 1;

    // /STAT DECLARATION/

    public SweetBattle() {
        this(USES, false);
    }

    public SweetBattle(int currentUses, boolean checkedGolden) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        this.magicNumber = this.baseMagicNumber = HEAL;
        //this.cardsToPreview = new SakisCookie();
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if(isActive(true)) {
            this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
            //this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy()));
            //this.addToBot(new MakeTempCardInDrawPileAction(cardsToPreview.makeStatEquivalentCopy(), 1, true, true));
            this.applyEffect();
        }
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if (isActive()) {
            this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
            //this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy()));
            //this.addToBot(new MakeTempCardInDrawPileAction(cardsToPreview.makeStatEquivalentCopy(), 1, true, true));
            this.applyEffect();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //this.cardsToPreview.upgrade();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SweetBattle(secondMagicNumber, checkedGolden);
    }
}
