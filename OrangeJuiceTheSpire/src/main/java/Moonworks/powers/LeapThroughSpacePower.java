package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.ClickableUIElement;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LeapThroughSpacePower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = OrangeJuiceMod.makeID("LeapThroughSpacePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PowerUIElement powerElement;

    public int drawCount;
    public final ArrayList<AbstractCard> determinedCards = new ArrayList<>();

    public int takenCards;

    private boolean alreadyClicked;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public LeapThroughSpacePower(final AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;
        powerElement = new PowerUIElement();

        // We load those txtures here.
        //this.loadRegion("flameBarrier");
        //this.loadRegion("master_reality");
        this.loadRegion("mantra");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        //Flash to indicate that less cards are being drawn
        if (takenCards > 0) {
            flash();
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();

        //Restore our drawing capability
        OrangeJuiceMod.logger.info("Reset draw capabilities");
        AbstractDungeon.player.gameHandSize += takenCards;
        takenCards = 0;
        //Clear our determined array
        //OrangeJuiceMod.logger.info("Cleared determined card array");
        //determinedCards.clear();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (takenCards > 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + takenCards + DESCRIPTIONS[4];
        } else if (takenCards == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + takenCards + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new LeapThroughSpacePower(owner);
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        c = new Color(0.0F, 1.0F, 0.0F, 1.0F);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.takenCards), x, y, this.fontScale, c);
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        //clickableHitbox.move(x, y);
        powerElement.move(x, y);
        powerElement.render(sb);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        powerElement.update();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        //We may not need to clear the array at any point if we can just remove them when they are drawn
        determinedCards.remove(card);
    }

    public void determineShuffledCards() {
        OrangeJuiceMod.logger.info("Determining cards");
        AbstractPlayer p = AbstractDungeon.player;

        //Remove any cards that are no longer in the discard pile
        flushDetermined();

        //Figure our how many cards to show
        determineDrawAmount();
        int temp = drawCount;

        //Check if our draw pile has enough, if amount is 0 or less we don't need to do anything
        temp -= p.drawPile.size();

        //Don't determine more than our discard pile has, or we run out of cards and keep looking
        temp = Math.min(temp, p.discardPile.size());

        //Don't determine cards if we already have enough
        temp -= determinedCards.size();

        //If we have cards to determine...
        if (temp > 0) {
            //Define 2 arrays because maps are not ordered and are difficult to reverse
            ArrayList<AbstractCard> removedCards = new ArrayList<>();
            ArrayList<Integer> cardIndexes = new ArrayList<>();

            //Remove all cards already determined
            for (AbstractCard c : determinedCards) {
                //Sanity check... It shouldn't be a determined card if it isn't in the discard pile, but safety first!
                if (p.discardPile.contains(c)) {
                    removedCards.add(c);
                    cardIndexes.add(p.discardPile.group.indexOf(c));
                    p.discardPile.removeCard(c);
                }
            }

            //Loop until we have added enough
            for (int i = 0 ; i < temp ; i++) {
                //In case we messed up, lol
                if (p.discardPile.size() > 0) {
                    //Get a random card
                    AbstractCard c = p.discardPile.getRandomCard(true);
                    OrangeJuiceMod.logger.info("Determined card: "+c);
                    //Add it to the list of removed cards (so they can be added back to the discard pile when we are done
                    removedCards.add(c);
                    //Get the index it was in at the time of removal
                    cardIndexes.add(p.discardPile.group.indexOf(c));
                    //Add the card to our set of determined cards
                    determinedCards.add(c);
                    //Remove it from the discard pile so we cant grab it twice, it gets added back next loop
                    p.discardPile.removeCard(c);
                }
            }
            //We need to add the cards back in the First-In Last-Out behaviour to keep ensure they go back in the same place
            //Reverse both lists and iterate through them to add them back
            Collections.reverse(cardIndexes);
            Collections.reverse(removedCards);
            for (int i = 0 ; i < removedCards.size() ; i++) {
                //Add it back where it was when we removed it
                p.discardPile.group.add(cardIndexes.get(i), removedCards.get(i));
            }
        }
        OrangeJuiceMod.logger.info("Done determining");
    }

    public void determineDrawAmount() {
        AbstractPlayer p = AbstractDungeon.player;
        int temp = p.gameHandSize;

        //Next Turn Draw
        AbstractPower ntd = p.getPower(DrawCardNextTurnPower.POWER_ID);
        if (ntd instanceof DrawCardNextTurnPower) {
            //This effect is additive
            temp += ntd.amount;
        }

        //Draw Reduction modifies gameHandsize, but we care if we have this power next turn instead
        AbstractPower dr =  p.getPower(DrawReductionPower.POWER_ID);
        if (dr instanceof DrawReductionPower) {
            //If it will wear off at the end of the turn...
            if (dr.amount == 1 && !((boolean) ReflectionHacks.getPrivate(dr, dr.getClass(), "justApplied"))) {
                temp += dr.amount;
            }
        }

        drawCount = temp;
    }

    public void flushDetermined() {
        determinedCards.removeIf(c -> !AbstractDungeon.player.discardPile.contains(c));
    }

    public class PowerUIElement extends ClickableUIElement {
        private static final float hitboxSize = 40f;
        private static final float moveDelta = hitboxSize/2;

        public PowerUIElement() {
            super(new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("48/" + ""),//carddraw
                    0, 0,
                    hitboxSize, hitboxSize);
        }

        public void move(float x, float y) {
            move(x, y, moveDelta);
        }

        public void move(float x, float y, float d) {
            this.setX(x-(d * Settings.scale));
            this.setY(y-(d * Settings.scale));
        }


        @Override
        protected void onHover() {}

        @Override
        protected void onUnhover() {}

        @Override
        protected void onClick() {
            if (!AbstractDungeon.actionManager.turnHasEnded) {
                //Dont let us mash the button when the screen is already up (but hidden)
                if(!alreadyClicked) {
                    //Set the flag so it doesn't reopen, release it in the final action
                    alreadyClicked = true;
                    OrangeJuiceMod.logger.info("Clicked Power, doing shenanigans");

                    //Flash power
                    flash();

                    //Define the player for shorthand
                    AbstractPlayer p = AbstractDungeon.player;

                    //Create a group to display. We don't bother to clone for now
                    CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                    //Define a map for where the card came from
                    HashMap<AbstractCard, CardGroup> cardLocationMap = new HashMap<>();

                    //Define a map for clone to original.
                    HashMap<AbstractCard, AbstractCard> cloneMap = new HashMap<>();

                    //Store any predetermined discard pile cards in the determinedCards array
                    determineShuffledCards();

                    //Determine if we have No Draw. If so, still show the cards, but we cant add any to our hand
                    boolean noDraw = p.hasPower(NoDrawPower.POWER_ID);

                    //Add either our draw amount, or the size of the draw pile, so we don't OOB
                    int drawPileAmount = Math.min (drawCount, p.drawPile.size());

                    //If the draw pile didnt have enough, add whatever we can from the discard pile. Again, don't OOB
                    int determinedAmount = Math.min(p.discardPile.size(), Math.max(0, drawCount - drawPileAmount));

                    //Placeholders
                    AbstractCard original, clone;

                    //Add the cards from the draw pile
                    for (int i = 0 ; i < drawPileAmount ; i++) {
                        original = p.drawPile.getNCardFromTop(i);
                        clone = original.makeStatEquivalentCopy();
                        cloneMap.put(clone, original);
                        cards.group.add(clone);
                        cardLocationMap.put(original, p.drawPile);
                    }

                    //Add the cards from the determinedCards array
                    for (int i = 0 ; i < determinedAmount ; i++) {
                        original = determinedCards.get(i);
                        clone = original.makeStatEquivalentCopy();
                        cloneMap.put(clone, original);
                        cards.group.add(clone);
                        cardLocationMap.put(original, p.discardPile);
                    }

                    //TODO determine the amount we are actually allowed to draw

                    //This will need to be an action to have the timing work
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            AbstractDungeon.gridSelectScreen.open(cards, cards.size(), true, DESCRIPTIONS[1]);
                            this.isDone = true;
                        }
                    });

                    //I hope this works
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            if (!noDraw) {
                                for (AbstractCard clone : AbstractDungeon.gridSelectScreen.selectedCards) {
                                    AbstractCard c = cloneMap.get(clone);
                                    CardGroup originalGroup = cardLocationMap.get(c);
                                    //Move the card from where it was to our hand
                                    drawHelper(c, originalGroup);
                                    //c.unfadeOut();
                                    originalGroup.removeCard(c);
                                    p.hand.addToTop(c);
                                    //Remove the card from our determined array
                                    determinedCards.remove(c);
                                    //Nerf our draw capability next turn
                                    takenCards++;
                                    p.gameHandSize--;
                                }
                                //This shouldnt happen, but just to be safe, draw power cant be negative
                                if (p.gameHandSize < 0) {
                                    OrangeJuiceMod.logger.warn("We ended up with negative draw amount. :(");
                                    p.gameHandSize = 0;
                                }
                            }
                            //Clear the selected cards
                            AbstractDungeon.gridSelectScreen.selectedCards.clear();

                            //Free the flag so it can be clicked again
                            alreadyClicked = false;

                            //Update description
                            updateDescription();

                            this.isDone = true;
                        }
                    });
                }
            }
        }
    }

    private void drawHelper(AbstractCard c, CardGroup group) {
        if (group == AbstractDungeon.player.drawPile) {
            c.current_x = CardGroup.DRAW_PILE_X;
            c.current_y = CardGroup.DRAW_PILE_Y;
        } else {
            c.current_x = CardGroup.DISCARD_PILE_X;
            c.current_y = CardGroup.DISCARD_PILE_Y;
        }

        c.setAngle(0.0F, true);
        c.lighten(false);
        c.drawScale = 0.12F;
        c.targetDrawScale = 0.75F;
        c.triggerWhenDrawn();

        for (AbstractPower p : AbstractDungeon.player.powers) {
            p.onCardDraw(c);
        }
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onCardDraw(c);
        }
    }
}
