package Moonworks.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ModifyCostThisCombatAction extends AbstractGameAction {
    private final AbstractCard card;

    public ModifyCostThisCombatAction(AbstractCard card, final int change) {
        this.card = card;
        this.amount = change;
    }

    public void update() {
        if (this.card != null) {
            card.modifyCostForCombat(this.amount);
            card.initializeDescription();
        }
        this.isDone = true;
    }
}