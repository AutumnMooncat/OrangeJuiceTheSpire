package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.defaultcards.AbstractTransformativeCard;
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
    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

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
    //private float duration = 0;
    //private boolean done = false;

    // /STAT DECLARATION/

    public MiracleWalker() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //this.exhaust = true;
        //this.purgeOnUse = true;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }
    @Override
    public void triggerWhenDrawn() {
        transformedThisTurn = false;
        temporaryTransform();
        super.triggerWhenDrawn();
    }

    @Override
    public void reinitialize(AbstractCard card) {
        super.reinitialize(card);
        this.costForTurn = 0;
        this.isCostModified = true;
    }


    @Override
    public void hover() {
        try {
            if (isInHand() && !transformedThisTurn && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                temporaryTransform();
            }
        } catch (NullPointerException ignored) {}
        super.hover();
    }

    private boolean isInHand() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card == this) {
                return true;
            }
        }
        return false;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if(!transformedThisCombat) {
            if (!upgraded) {
                upgradeName();
                rawDescription = UPGRADE_DESCRIPTION;
                //upgradeBaseCost(UPGRADE_COST);
                initializeDescription();
            }
        }
    }
}
