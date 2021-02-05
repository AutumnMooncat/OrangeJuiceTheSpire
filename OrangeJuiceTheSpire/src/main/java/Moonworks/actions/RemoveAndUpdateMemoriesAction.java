package Moonworks.actions;

import Moonworks.powers.BookOfMemoriesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.HashSet;

public class RemoveAndUpdateMemoriesAction extends AbstractGameAction {
    private final HashSet<AbstractCard> cardsToRemove = new HashSet<>();
    private final AbstractPlayer p = AbstractDungeon.player;

    public RemoveAndUpdateMemoriesAction(ArrayList<AbstractCard> cards) {
        cardsToRemove.addAll(cards);
    }

    public RemoveAndUpdateMemoriesAction(AbstractCard card) {
        cardsToRemove.add(card);
    }

    @Override
    public void update() {
        //See if we have the power
        AbstractPower pow = p.getPower(BookOfMemoriesPower.POWER_ID);

        //IF we do, remove the card in general
        if (pow instanceof BookOfMemoriesPower) {
            ((BookOfMemoriesPower) pow).removeCardsFromSet(cardsToRemove);
        }

        //Finish the action
        this.isDone = true;
    }
}
