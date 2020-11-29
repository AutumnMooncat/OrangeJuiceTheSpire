package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractTransformativeCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MiracleWalker extends AbstractTransformativeCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MiracleWalker.class.getSimpleName());
    public static final String IMG = makeCardPath("MiracleWalker.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;

    // /STAT DECLARATION/

    public MiracleWalker() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    //Transform when drawn. We can also transform under different circumstances if we want.
    @Override
    public void triggerWhenDrawn() {
        transformedThisTurn = false;
        temporaryTransform();
        super.triggerWhenDrawn();
    }

    //The special feature of this card. It is transformative, but we also want it to always be a 0 cost when a new transformation happens.
    @Override
    public void reinitialize(AbstractCard card) {
        super.reinitialize(card);
        this.costForTurn = 0;
        this.isCostModified = true;
    }

    //If this card was generated, or otherwise failed to transform, fix this when we mouse over it.
    @Override
    public void hover() {
        try {
            if (isInHand() && !transformedThisTurn && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                temporaryTransform();
            }
        } catch (NullPointerException ignored) {}
        super.hover();
    }

    //Don't try to transform on hover if we are not expressly looking at it in our hand.
    private boolean isInHand() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card == this) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void upgrade() {
        //This should not normally happen in combat. If this card is generated in combat then it wont have a temp card yet, but we should still upgrade it.
        //The out of combat version should still work fine though
        if(!transformedThisCombat) {
            if (!upgraded) {
                upgradeName();
                rawDescription = UPGRADE_DESCRIPTION;
                initializeDescription();
            }
        } else {
            tempCard.upgrade();
        }
    }
}
