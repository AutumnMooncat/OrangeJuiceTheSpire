package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.JonathanRush;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpgradeRushAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final AbstractCard card;

    public UpgradeRushAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
    }

    @Override
    public void update() {
        this.card.baseDamage += this.amount;
        this.card.applyPowers();

        for(AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof JonathanRush) {
                c.baseDamage += this.amount;
                c.applyPowers();
            }
        }

        for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
            if (c instanceof JonathanRush) {
                c.baseDamage += this.amount;
                c.applyPowers();
            }
        }

        for (AbstractCard c: AbstractDungeon.player.hand.group) {
            if (c instanceof JonathanRush) {
                c.baseDamage += this.amount;
                c.applyPowers();
            }
        }

        this.isDone = true;
    }
}
