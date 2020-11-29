package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ReproductionOfRecordsAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private AbstractPlayer p;
    private final boolean upgrade;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public ReproductionOfRecordsAction(int amount) {
        this(amount, false);
    }

    public ReproductionOfRecordsAction(int amount, boolean upgrade) {
        this.upgrade = upgrade;
        this.amount = amount;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, this.p, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        //logger.info("Start ROR.");
        ArrayList<AbstractCard> cards = new ArrayList<>();
        //If discard isnt empty, and we actually want cards
        if (!this.p.discardPile.isEmpty() && amount > 0) {
            //If discard size less than amount
            if (this.p.discardPile.size() < amount) {
                //...set loop size to discard size
                amount = this.p.discardPile.size();
            }
            //For loop size...
            for (int i = 0 ; i < amount ; i++) {
                //Grab nth top card
                cards.add(this.p.discardPile.getNCardFromTop(i).makeStatEquivalentCopy());
            }
            for(AbstractCard card : cards) {
                //Check Master Reality or upgrade
                if ((upgrade || this.p.hasPower("MasterRealityPower")) && card.canUpgrade()) {
                    card.upgrade();
                }
                if (this.p.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    //Add copy of card to hand
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    //this.p.hand.addToHand(card);
                    this.p.hand.refreshHandLayout();
                } else {
                    //OR draw pile if hand full
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, false, false));
                    //this.p.drawPile.addToTop(card);
                }
            }
        }
        this.isDone = true;
        this.tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BetterToHandAction");
        TEXT = uiStrings.TEXT;
    }
}
