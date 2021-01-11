package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsumePurgeImmediatelyAction extends AbstractGameAction {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final AbstractCard toPurge;

    public ConsumePurgeImmediatelyAction(AbstractCard toPurge) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.toPurge = toPurge;
        //this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        //logger.info("Attempting to exhaust card: "+toExhaust+", Card class: "+toExhaust.getClass());
        for (AbstractCard card : AbstractDungeon.player.hand.group)
        {
            if (card == toPurge) {
                AbstractDungeon.player.hand.group.remove(card);
                break;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group)
        {
            if (card == toPurge) {
                AbstractDungeon.player.drawPile.group.remove(card);
                break;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group)
        {
            if (card == toPurge) {
                AbstractDungeon.player.discardPile.group.remove(card);
                break;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group)
        {
            if (card == toPurge) {
                AbstractDungeon.player.exhaustPile.group.remove(card);
                break;
            }
        }
        AbstractDungeon.effectList.add(new ExhaustCardEffect(toPurge));
        if (AbstractDungeon.player.limbo.contains(toPurge)) {
            AbstractDungeon.player.limbo.removeCard(toPurge);
        }
        AbstractDungeon.player.cardInUse = null;
        //If we havnt found it yet, then its already in the exhaust pile or god knows where, so just do nothing
        this.isDone = true;
    }
}