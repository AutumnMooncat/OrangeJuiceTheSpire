package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WitherExhaustImmediatelyAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final AbstractCard toExhaust;

    public WitherExhaustImmediatelyAction(AbstractCard toExhaust) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.toExhaust = toExhaust;
        //this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        //logger.info("Attempting to exhaust card: "+toExhaust+", Card class: "+toExhaust.getClass());
        boolean found = false;
        for (AbstractCard card : AbstractDungeon.player.hand.group)
        {
            if (card == toExhaust) {
                found = true;
                break;
            }
        }
        if (found) {
            AbstractDungeon.player.hand.moveToExhaustPile(toExhaust);
            this.isDone = true;
            return;
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group)
        {
            if (card == toExhaust) {
                found = true;
                break;
            }
        }
        if (found) {
            AbstractDungeon.player.drawPile.moveToExhaustPile(toExhaust);
            this.isDone = true;
            return;
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group)
        {
            if (card == toExhaust) {
                found = true;
                break;
            }
        }
        if (found) {
            AbstractDungeon.player.discardPile.moveToExhaustPile(toExhaust);
            this.isDone = true;
            return;
        }
        //If we havnt found it yet, then its already in the exhaust pile or god knows where, so just do nothing
        this.isDone = true;
    }
}