package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

public class RecoverExhaustedGiftAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final AbstractPlayer p;
    private final boolean upgrade;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public RecoverExhaustedGiftAction(int amount) {
        this(amount, false);
    }

    public RecoverExhaustedGiftAction(int amount, boolean upgrade) {
        this.upgrade = upgrade;
        this.amount = amount;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        Iterator <AbstractCard>c;
        AbstractCard derp;
        CardGroup giftCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard exhaustedCard : this.p.exhaustPile.group){
            if (exhaustedCard instanceof AbstractGiftCard) {
                giftCards.addToRandomSpot(exhaustedCard);
            }
        }
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (giftCards.isEmpty()) {
                this.isDone = true;
            /*} else if (giftCards.size() <= amount) {
                //logger.info("Exhaust Size: " + this.p.exhaustPile.size() + ", Draw Amount: " + amount);
                while (!giftCards.isEmpty()){
                    AbstractCard card = giftCards.getTopCard();
                    //logger.info("Card: " + card.toString());
                    AbstractGiftCard.refreshGiftUses((AbstractGiftCard) card);
                    card.unfadeOut();
                    //this.p.drawPile.addToRandomSpot(card);
                    this.p.exhaustPile.moveToDeck(card, true); //Actually pull it from exhaust here
                    this.p.exhaustPile.removeCard(card);
                    if (AbstractDungeon.player.hasPower("Corruption") && card.type == CardType.SKILL) {
                        card.setCostForTurn(-9);
                    }
                    if (this.upgrade && card.canUpgrade()) {
                        card.upgrade();
                    }
                    card.unhover();
                    card.fadingOut = false;
                }
                this.isDone = true;
*/
            } else {
                c = giftCards.group.iterator();

                while(c.hasNext()) {
                    derp = c.next();
                    derp.stopGlowing();
                    derp.unhover();
                    derp.unfadeOut();
                }

                AbstractDungeon.gridSelectScreen.open(giftCards, Math.min(amount, giftCards.size()), TEXT[0], upgrade);
                this.tickDuration();

            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for(c = AbstractDungeon.gridSelectScreen.selectedCards.iterator(); c.hasNext(); derp.unhover()) {
                    derp = c.next();
                    AbstractGiftCard.refreshGiftUses((AbstractGiftCard) derp);
                    this.p.exhaustPile.moveToDeck(derp, true); //Actually pull from exhaust
                    if (AbstractDungeon.player.hasPower("Corruption") && derp.type == CardType.SKILL) {
                        derp.setCostForTurn(-9);
                    }

                    this.p.exhaustPile.removeCard(derp); //Same here
                    if (this.upgrade && derp.canUpgrade()) {
                        derp.upgrade();
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();

                for(c = giftCards.group.iterator(); c.hasNext(); derp.target_y = 0.0F) {
                    derp = c.next();
                    derp.unhover();
                    derp.target_x = (float)CardGroup.DISCARD_PILE_X;
                }
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("RestoreGift"));
        TEXT = uiStrings.TEXT;
    }
}
