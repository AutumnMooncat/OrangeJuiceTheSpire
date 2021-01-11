package Moonworks.actions;

import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.relics.GoldenDie;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CheckGoldenAction extends AbstractGameAction {
    private final AbstractGiftCard giftCard;

    public CheckGoldenAction(AbstractGiftCard giftCard) {
        this.giftCard = giftCard;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_XFAST) {
            if (!giftCard.ignoreGolden && !giftCard.checkedGolden) {
                boolean goldenDie = AbstractDungeon.player.hasRelic(GoldenDie.ID);
                giftCard.secondMagicNumber += (goldenDie ? AbstractGiftCard.GOLDEN_BUFF : 0);
                //this.defaultBaseSecondMagicNumber += (goldenDie ? GOLDEN_BUFF : 0);
                if (goldenDie) {
                    giftCard.isSecondMagicNumberModified = true;
                }
                giftCard.checkedGolden = true;
                giftCard.initializeDescription();
            }
            this.isDone = true;
            tickDuration();
        }
    }
}