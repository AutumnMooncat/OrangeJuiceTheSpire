package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
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

public class ExhaustToDrawPileAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final AbstractPlayer p;
    private final boolean upgrade;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public ExhaustToDrawPileAction(int amount) {
        this(amount, false);
    }

    public ExhaustToDrawPileAction(int amount, boolean upgrade) {
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
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.exhaustPile.isEmpty()) {
                this.isDone = true;
            } else if (this.p.exhaustPile.size() <= amount) {
                //logger.info("Exhaust Size: " + this.p.exhaustPile.size() + ", Draw Amount: " + amount);
                while (!this.p.exhaustPile.isEmpty()){
                    AbstractCard card = this.p.exhaustPile.getTopCard();
                    logger.info("Card: " + card.toString());
                    card.unfadeOut();
                    //this.p.drawPile.addToRandomSpot(card);
                    this.p.exhaustPile.moveToDeck(card, true);
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

            } else {
                c = this.p.exhaustPile.group.iterator();

                while(c.hasNext()) {
                    derp = c.next();
                    derp.stopGlowing();
                    derp.unhover();
                    derp.unfadeOut();
                }

                //c = this.p.exhaustPile.group.iterator();

                /*
                while(c.hasNext()) {
                    derp = (AbstractCard)c.next();
                    if (derp.cardID.equals("Exhume")) {
                        c.remove();
                        this.exhumes.add(derp);
                    }
                }*/

                AbstractDungeon.gridSelectScreen.open(this.p.exhaustPile, amount, TEXT[0], upgrade);
                this.tickDuration();

            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for(c = AbstractDungeon.gridSelectScreen.selectedCards.iterator(); c.hasNext(); derp.unhover()) {
                    derp = c.next();
                    this.p.exhaustPile.moveToDeck(derp, true);
                    if (AbstractDungeon.player.hasPower("Corruption") && derp.type == CardType.SKILL) {
                        derp.setCostForTurn(-9);
                    }

                    this.p.exhaustPile.removeCard(derp);
                    if (this.upgrade && derp.canUpgrade()) {
                        derp.upgrade();
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();

                for(c = this.p.exhaustPile.group.iterator(); c.hasNext(); derp.target_y = 0.0F) {
                    derp = c.next();
                    derp.unhover();
                    derp.target_x = (float)CardGroup.DISCARD_PILE_X;
                }
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhumeAction");
        TEXT = uiStrings.TEXT;
    }
}
