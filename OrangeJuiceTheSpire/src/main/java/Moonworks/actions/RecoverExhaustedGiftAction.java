package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import basemod.BaseMod;
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
    CardGroup giftCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

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
                this.isDone = true;*/

            } else {
                for (AbstractCard c : giftCards.group) {
                    c.stopGlowing();
                    c.unhover();
                    c.unfadeOut();
                }

                AbstractDungeon.gridSelectScreen.open(giftCards, Math.min(amount, giftCards.size()), TEXT[0], upgrade);
                this.tickDuration();

            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    c.unhover();
                    AbstractGiftCard.restoreAllGiftUses((AbstractGiftCard) c);
                    //this.p.drawPile.addToRandomSpot(c);
                    //this.p.exhaustPile.moveToDeck(c, true);
                    //this.p.exhaustPile.removeCard(c);
                    if (this.upgrade && c.canUpgrade()) {
                        c.upgrade();
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();

                for (AbstractCard c : giftCards.group) {
                    c.target_y = 0.0F;
                    c.target_x = CardGroup.DISCARD_PILE_X;
                    c.unhover();
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
