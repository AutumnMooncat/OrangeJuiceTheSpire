package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.MemoryModifier;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.cards.magicalCards.MagicalInferno;
import Moonworks.cards.magicalCards.MagicalMassacre;
import Moonworks.cards.magicalCards.MagicalRevenge;
import Moonworks.patches.MemoryAssociationPatch;
import Moonworks.powers.MeltingMemoriesPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;

import java.util.HashMap;

public class MeltingMemoriesConvertMemoryAction extends AbstractGameAction {
    private boolean appliedCards = false;
    public final AbstractPlayer p;
    public final MeltingMemoriesPower power;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private HashMap<AbstractCard, AbstractCard> copyMap = new HashMap<>();

    public MeltingMemoriesConvertMemoryAction(MeltingMemoriesPower power, final int amount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.power = power;
        p = AbstractDungeon.player;
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
                for (AbstractCard cardToAdd : AbstractDungeon.gridSelectScreen.selectedCards) {
                    CardModifierManager.addModifier(copyMap.get(cardToAdd), new MemoryModifier(false));
                    MemoryAssociationPatch.MemoryAssociation.associations.get(cardToAdd).add(power);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                appliedCards = true;
                power.updateDescription();
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

    private boolean getViability(AbstractCard c) {
        return (c.type != AbstractCard.CardType.POWER && c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE && !(c instanceof AbstractGiftCard) && !(c instanceof AbstractTempCard) && c.cost >= 0);
    }

    /**
     * Empty hook for if a different card is added to this via a patch or something
     * @param card - The card that was selected
     */
    public void otherCardHook(AbstractCard card) {}

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ChooseMemory"));
        TEXT = uiStrings.TEXT;
    }
}
