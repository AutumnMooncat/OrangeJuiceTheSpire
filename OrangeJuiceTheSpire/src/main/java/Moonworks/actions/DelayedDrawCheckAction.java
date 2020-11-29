package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DelayedDrawCheckAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final AbstractPower power;


    public DelayedDrawCheckAction(AbstractPower power, int amount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = amount;
        this.power = power;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (emptyOrOnlyGiftCards() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.player.hasPower("No Draw") && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && (AbstractDungeon.player.discardPile.size() > 0 || AbstractDungeon.player.drawPile.size() > 0)) {
            power.flash();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, amount));
        }
        this.tickDuration();
        this.isDone = true;
        /*
        if (this.duration == Settings.ACTION_DUR_FAST) {


        } else {

            this.tickDuration();
        }*/
    }
    private boolean emptyOrOnlyGiftCards() {
        /*for(AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action.actionType == AbstractGameAction.ActionType.DRAW){
                return false;
            }
        }*/
        if (AbstractDungeon.player.hand.isEmpty()) {
            return true;
        }
        for(AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cost != -2) {
                return false;
            }
        }
        return true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhumeAction");
        TEXT = uiStrings.TEXT;
    }
}
