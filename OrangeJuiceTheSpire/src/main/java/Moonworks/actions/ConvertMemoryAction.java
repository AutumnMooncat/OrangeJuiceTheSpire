package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.HashMap;

import static Moonworks.powers.BookOfMemoriesPower.getViability;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.getRandomMonster;

public class ConvertMemoryAction extends AbstractGameAction {
    private boolean appliedCards = false;
    private final boolean upgrade, handOnly, retainOnce;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final HashMap<AbstractCard, AbstractCard> copyMap = new HashMap<>();

    public ConvertMemoryAction(final int amount, final boolean upgrade) {
        this(amount, upgrade, false, false);
    }

    public ConvertMemoryAction(final int amount, final boolean upgrade, final boolean handOnly, final boolean retainOnce) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.upgrade = upgrade;
        this.handOnly = handOnly;
        this.retainOnce = retainOnce;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            //Create a group of cards
            CardGroup copyCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            //Fill it with the options
            getCardChoices(copyCards);

            //Determine the amount of choices
            int choices = Math.min(copyCards.size(), amount);

            //Show the screen and pick the cards
            AbstractDungeon.gridSelectScreen.open(copyCards, choices, true, TEXT[0]+choices+TEXT[1]);
        } else {
            if(!appliedCards) {
                //Make an arraylist of the original cards
                ArrayList<AbstractCard> cardsToAdd = new ArrayList<>();
                for (AbstractCard copyCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                    AbstractCard realCard = copyMap.get(copyCard);
                    if (upgrade && realCard.canUpgrade()) {
                        realCard.upgrade();
                    }
                    if (retainOnce) {
                        realCard.retain = true;
                    }
                    cardsToAdd.add(realCard);
                }
                //Add all the cards at once
                this.addToBot(new ApplyAndUpdateMemoriesAction(cardsToAdd));
                //Clear the cards
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                //Set the flag so this doesn't happen more than once before the action ends
                appliedCards = true;
            }
        }
        this.tickDuration();
    }

    private void getCardChoices(CardGroup group) {
        //Define a temporary group
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        //Add all viable cards in hand
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (getViability(c)) {
                AbstractCard copy = c.makeStatEquivalentCopy();
                copyMap.put(copy, c);
                temp.addToTop(copy);
            }
        }

        //Sort them
        temp.sortAlphabetically(true);

        //Add to the main list and clear the temp list
        group.group.addAll(temp.group);
        temp.clear();

        //If not only hand cards...
        if (!handOnly) {
            //Same dice for draw pile
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (getViability(c)) {
                    AbstractCard copy = c.makeStatEquivalentCopy();
                    copyMap.put(copy, c);
                    temp.addToTop(copy);
                }
            }
            temp.sortAlphabetically(true);
            group.group.addAll(temp.group);
            temp.clear();

            //And again for discard pile
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (getViability(c)) {
                    AbstractCard copy = c.makeStatEquivalentCopy();
                    copyMap.put(copy, c);
                    temp.addToTop(copy);
                }
            }
            temp.sortAlphabetically(true);
            group.group.addAll(temp.group);
            temp.clear();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ChooseMemory"));
        TEXT = uiStrings.TEXT;
    }
}
