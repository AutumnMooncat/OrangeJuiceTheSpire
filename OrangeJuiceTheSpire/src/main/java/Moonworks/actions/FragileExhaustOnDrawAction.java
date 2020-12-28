package Moonworks.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FragileExhaustOnDrawAction extends AbstractGameAction {

    private final AbstractCard toExhaust;

    public FragileExhaustOnDrawAction(AbstractCard toExhaust) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.toExhaust = toExhaust;
        //this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
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
        }
        this.isDone = true;
    }
}