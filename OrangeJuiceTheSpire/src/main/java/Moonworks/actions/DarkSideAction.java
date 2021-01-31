package Moonworks.actions;

import Moonworks.cards.magicalCards.MagicalInferno;
import Moonworks.cards.magicalCards.MagicalMassacre;
import Moonworks.cards.magicalCards.MagicalRevenge;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class DarkSideAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private final boolean upgrade;
    private final int charges;

    public DarkSideAction(int charges) {
        this(false, charges);
    }

    public DarkSideAction(boolean upgrade, int charges) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.charges = charges;
        this.upgrade = upgrade;
    }

    public void update() {
        ArrayList<AbstractCard> generatedCards;

        generatedCards = this.generateCardChoices();

        if (upgrade || AbstractDungeon.player.hasPower("MasterRealityPower")) {
            for (AbstractCard card : generatedCards) {
                card.upgrade();
            }
        }

        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], false);
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    disCard.setCostForTurn(0);
                    disCard.current_x = -1000.0F * Settings.scale;

                    for (int i = 0 ; i < this.amount ; i++) {
                        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        } else {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(disCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, false, false, false));
                        }
                    }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }
                this.retrieveCard = true;
            }

        }
        this.tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> cardList = new ArrayList<>();
        cardList.add(new MagicalInferno(charges));
        cardList.add(new MagicalMassacre(charges));
        cardList.add(new MagicalRevenge(charges));

        return cardList;
    }
}