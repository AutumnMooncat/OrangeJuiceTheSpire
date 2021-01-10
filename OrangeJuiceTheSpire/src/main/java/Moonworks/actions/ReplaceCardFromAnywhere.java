package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.SubspaceTunnel;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.HashMap;
import java.util.Map;

// Thank you Alchyr#3696
public class ReplaceCardFromAnywhere extends AbstractGameAction {
    private final AbstractPlayer p;
    private final boolean upgrade;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final Map<AbstractCard, CardGroup> map = new HashMap<>();
    private final SubspaceTunnel subspaceTunnel;
    private final int handIndex;
    private final Map<AbstractCard, Color> colorMap = new HashMap<>();
    private static final Color drawPileBlue = new Color(0.2F, 0.9F, 1.0F, 0.25F);
    private static final Color discardPileRed = new Color(-16776961);
    private static final Color exhaustPilePurple = new Color(-1608453889);
    private static final boolean doColors = false;

    public ReplaceCardFromAnywhere(SubspaceTunnel subspaceTunnel, int handIndex) {
        this(subspaceTunnel, handIndex, false);
    }

    public ReplaceCardFromAnywhere(SubspaceTunnel subspaceTunnel, int handIndex, boolean upgrade) {
        this.upgrade = upgrade;
        this.subspaceTunnel = subspaceTunnel;
        this.handIndex = handIndex;
        this.amount = 1;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.subspaceTunnel.success = !this.subspaceTunnel.purgeOnUse && !this.subspaceTunnel.exhaust && !this.subspaceTunnel.exhaustOnUseOnce;
    }

    public void update() {
        CardGroup allCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CardGroup tempCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (amount <= 0) {
                this.isDone = true;
            }
            //This should be randomized so the player cant figure out the upcoming card order
            for (AbstractCard card : p.drawPile.group) {
                if (doColors) {
                    colorMap.put(card, card.glowColor.cpy());
                    card.glowColor = new Color(drawPileBlue);
                    card.beginGlowing();
                }
                tempCards.addToRandomSpot(card);
                map.put(card, p.drawPile);
            }
            tempCards.sortAlphabetically(true);
            //tempCards.sortByRarity(true);
            //tempCards.sortByStatus(true);
            allCards.group.addAll(tempCards.group);
            tempCards.clear();
            //These should not be randomized to help the player correctly pick a card from the discard or exhaust piles
            //If they happened to have a duplicate card in the draw pile, they can now tell the difference hopefully
            for (AbstractCard card : p.discardPile.group) {
                if (doColors) {
                    colorMap.put(card, card.glowColor.cpy());
                    card.glowColor = new Color(discardPileRed);
                    card.beginGlowing();
                }
                tempCards.addToTop(card);
                map.put(card, p.discardPile);
            }
            tempCards.sortAlphabetically(true);
            //tempCards.sortByRarity(true);
            //tempCards.sortByStatus(true);
            allCards.group.addAll(tempCards.group);
            tempCards.clear();
            for (AbstractCard card : p.exhaustPile.group) {
                if (doColors) {
                    colorMap.put(card, card.glowColor.cpy());
                    card.glowColor = new Color(exhaustPilePurple);
                    card.beginGlowing();
                }
                tempCards.addToTop(card);
                map.put(card, p.exhaustPile);
            }
            tempCards.sortAlphabetically(true);
            //tempCards.sortByRarity(true);
            //tempCards.sortByStatus(true);
            allCards.group.addAll(tempCards.group);
            tempCards.clear();
            if (allCards.isEmpty() || p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(allCards, Math.min(allCards.size(),Math.min(amount, (BaseMod.MAX_HAND_SIZE - p.hand.size()))), TEXT[0], upgrade);
            }
            this.tickDuration();
        } else {
            if (doColors) {
                //Restore glow colors
                for (AbstractCard card : allCards.group) {
                    card.glowColor = colorMap.get(card);
                    card.stopGlowing();
                }
                colorMap.clear();
            }
            //Do our stuff
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard swappedCard : AbstractDungeon.gridSelectScreen.selectedCards) {
                    if (this.upgrade && swappedCard.canUpgrade()) {
                        swappedCard.upgrade();
                    }
                    swappedCard.costForTurn = 0;
                    swappedCard.isCostModifiedForTurn = true;
                    int pileIndex = map.get(swappedCard).group.indexOf(swappedCard); //Get index of the new card in its old pile

                    //Take the new card out of its old pile and put it in the hand now
                    if (handIndex >= 0 && handIndex <= p.hand.group.size())
                    {
                        p.hand.group.add(handIndex, swappedCard);
                    }
                    else
                    {
                        p.hand.addToTop(swappedCard);
                    }
                    map.get(swappedCard).removeCard(swappedCard);

                    //Reset the old card so its not glowy and stuff
                    resetCardActionsStuff();

                    //Put the old card where the new one was

                    CardGroup target = map.get(swappedCard);

                    if (pileIndex < 0)
                        pileIndex = 0;

                    if (target == AbstractDungeon.player.drawPile) {
                        subspaceTunnel.shrink();
                        subspaceTunnel.darken(false);
                        AbstractDungeon.getCurrRoom().souls.onToDeck(subspaceTunnel, true, true);
                    } else if (target == AbstractDungeon.player.discardPile) {
                        subspaceTunnel.shrink();
                        subspaceTunnel.darken(false);
                        AbstractDungeon.getCurrRoom().souls.discard(subspaceTunnel, true);
                    } else if (target == AbstractDungeon.player.exhaustPile) {
                        AbstractDungeon.effectList.add(new ExhaustCardEffect(subspaceTunnel));
                    } else {
                        //Something wrong happened, just continue on as usual
                        subspaceTunnel.success = false;
                    }
                    if(subspaceTunnel.success) {
                        target.group.add(pileIndex, subspaceTunnel);
                    }
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                map.clear();
            }
            this.tickDuration();
            this.isDone = true;
        }
    }

    private void resetCardActionsStuff() { //We already do this in the Patch, but was testing for good measure
        if (AbstractDungeon.player.hoveredCard == subspaceTunnel) {
            AbstractDungeon.player.releaseCard();
        }
        subspaceTunnel.unhover();
        subspaceTunnel.untip();
        subspaceTunnel.stopGlowing();
        AbstractDungeon.actionManager.removeFromQueue(subspaceTunnel);
        p.hand.group.remove(subspaceTunnel); //So we dont get a copy in hand
        p.drawPile.group.remove(subspaceTunnel); //So we dont get a copy in draw pile
        p.discardPile.group.remove(subspaceTunnel); //So we dont get a copy in discard pile
        p.limbo.group.remove(subspaceTunnel); //haha no floaty card
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("FetchFromAnywhere"));
        TEXT = uiStrings.TEXT;
    }
}