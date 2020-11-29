package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class FetchCardFromAnywhereAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final AbstractPlayer p;
    private final boolean upgrade;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final Map<AbstractCard,CardGroup> map = new HashMap<>();

    public FetchCardFromAnywhereAction(int amount) {
        this(amount, false);
    }

    public FetchCardFromAnywhereAction(int amount, boolean upgrade) {
        this.upgrade = upgrade;
        this.amount = amount;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        CardGroup allCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (amount <= 0) {
                this.isDone = true;
            }
            for (AbstractCard card : p.drawPile.group) {
                allCards.addToRandomSpot(card);
                map.put(card, p.drawPile);
            }
            for (AbstractCard card : p.discardPile.group) {
                allCards.addToRandomSpot(card);
                map.put(card, p.discardPile);
            }
            for (AbstractCard card : p.exhaustPile.group) {
                allCards.addToRandomSpot(card);
                map.put(card, p.exhaustPile);
            }
            if (allCards.isEmpty() || p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(allCards, Math.min(allCards.size(),Math.min(amount, (BaseMod.MAX_HAND_SIZE - p.hand.size()))), TEXT[0], upgrade);
                /*if (allCards.size() < amount) {
                    //just do it manually
                    for (AbstractCard card : allCards.group) {
                        if (p.hand.size() < BaseMod.MAX_HAND_SIZE) {
                            map.get(card).moveToHand(card);
                            map.get(card).removeCard(card);
                        } else {
                            map.get(card).moveToDiscardPile(card);
                        }
                    }
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    map.clear();
                    this.isDone = true;
                } else {
                    AbstractDungeon.gridSelectScreen.open(allCards, this.amount, TEXT[0], upgrade);
                }*/
            }
            this.tickDuration();
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (this.upgrade && card.canUpgrade()) {
                        card.upgrade();
                    }
                    map.get(card).moveToHand(card);
                    map.get(card).removeCard(card);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                map.clear();
            }
            this.tickDuration();
            this.isDone = true;
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhumeAction");
        TEXT = uiStrings.TEXT;
    }
}
