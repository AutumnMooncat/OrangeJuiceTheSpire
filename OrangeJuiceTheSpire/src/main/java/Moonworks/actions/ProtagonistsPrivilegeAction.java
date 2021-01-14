package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ProtagonistsPrivilegeAction extends AbstractGameAction {

    private static final ArrayList<AbstractCard> cardCheckArray = new ArrayList<>();
    private final AbstractGameAction successAction;
    private final AbstractGameAction failAction;
    private boolean autoPass = false;

    public ProtagonistsPrivilegeAction(int drawAmount, AbstractGameAction successAction, boolean autoPass) {
        this(drawAmount, successAction, null);
        this.autoPass = autoPass;
    }
    public ProtagonistsPrivilegeAction(int drawAmount, AbstractGameAction successAction) {
        this(drawAmount, successAction, null);
    }

    public ProtagonistsPrivilegeAction(int drawAmount, AbstractGameAction successAction, AbstractGameAction failAction) {
        this.successAction = successAction;
        this.failAction = failAction;
        this.amount = drawAmount;
    }

    @Override
    public void update() {

        OrangeJuiceMod.logger.info("Starting Protag Action");
        //If we cant draw, stop. If we nothing nothing to draw, stop
        if (AbstractDungeon.player.hasPower("No Draw")) {
            AbstractDungeon.player.getPower("No Draw").flash();
            this.isDone = true;
            return;
        } else if (amount <= 0) {
            this.isDone = true;
            return;
        }

        //Get the size of our potential draw amounts
        int drawSize = AbstractDungeon.player.drawPile.size();
        int discardSize = AbstractDungeon.player.discardPile.size();

        //If we have none in the draw pile, but some in discard, shuffle.
        //IF we have none in draw and none in discard, we cant draw, stop

        //Draws is out amount, or what will fit in our hand, or what we have to draw, whichever is smallest
        final boolean[] outOfCards = {false};

        for (int i = 0; i < amount ; i++) {
            OrangeJuiceMod.logger.info("Draw #: "+(i+1)+"/"+(amount));

            //Shuffle if we need to
            this.addToBot(new AbstractGameAction() {
                boolean checkShuffle = false;
                boolean shuffled = false;
                public void update() {
                    if(!checkShuffle) {
                        OrangeJuiceMod.logger.info("Do we need to shuffle?");
                        if (AbstractDungeon.player.drawPile.size() == 0) {
                            OrangeJuiceMod.logger.info("No cards in draw pile.");
                            if (AbstractDungeon.player.discardPile.size() == 0) {
                                OrangeJuiceMod.logger.info("No cards in discard either, stopping.");
                                outOfCards[0] = true;
                            } else {
                                OrangeJuiceMod.logger.info("Shuffling.");
                                this.addToTop(new EmptyDeckShuffleAction());
                                shuffled = true;
                            }
                        }
                        checkShuffle = true;
                    }
                    OrangeJuiceMod.logger.info("Waiting for shuffle check.");
                    this.isDone = !shuffled || !SoulGroup.isActive();
                }});

            if (outOfCards[0]) {
                OrangeJuiceMod.logger.info("No cards left to draw");
                break;
            }

            // Draw the top card, save it. We use an action to help with timing.
            this.addToBot(new AbstractGameAction() {
                public void update() {
                    if (!outOfCards[0]) {
                        OrangeJuiceMod.logger.info("Cards in Draw: "+AbstractDungeon.player.drawPile.size()+". Cards in Discard: "+AbstractDungeon.player.discardPile.size());
                        cardCheckArray.add(AbstractDungeon.player.drawPile.getTopCard());
                    }
                    this.isDone = true;
                }});

            //Actually draw the card
            this.addToBot(new AbstractGameAction() {
                public void update() {
                    if (!outOfCards[0]) {
                        OrangeJuiceMod.logger.info("Card Draw: " + AbstractDungeon.player.drawPile.getTopCard() +". Type: "+AbstractDungeon.player.drawPile.getTopCard().type);
                        this.addToTop(new DrawCardAction(1));
                    }
                    this.isDone = true;
                }});
        }

        //Send an action to the top to tally out cards. We use an action so it happens after we finish our draws
        this.addToBot(new AbstractGameAction() {
            public void update() {
                if ((autoPass || checkAddedCards()) && successAction != null) {
                    OrangeJuiceMod.logger.info("Passed check, applying action: "+successAction);
                    this.addToTop(successAction);
                } else if (failAction != null) {
                    OrangeJuiceMod.logger.info("Failed check, applying action: "+failAction);
                    this.addToTop(failAction);
                }
                this.isDone = true;
            }});

        this.isDone = true;
    }

    private boolean checkAddedCards() {
        OrangeJuiceMod.logger.info("Check size: "+cardCheckArray.size());
        boolean botherChecking = cardCheckArray.size() > 1; //We cant have 2 cards be the same if we drew less than 2 cards
        if (botherChecking) {
            AbstractCard.CardType firstType = cardCheckArray.get(0).type; //Get the type of the first card
            OrangeJuiceMod.logger.info("First Type: "+firstType);
            for (AbstractCard c : cardCheckArray) {
                //Loop through all the cards, if we find 2 are different break
                OrangeJuiceMod.logger.info("Does "+firstType+" match "+c.type+"?");
                if (firstType != c.type) {
                    OrangeJuiceMod.logger.info("No");
                    botherChecking = false;
                    break;
                }
            }

        }
        OrangeJuiceMod.logger.info("Passed check? "+botherChecking);
        cardCheckArray.clear();
        return botherChecking;
    }
}
