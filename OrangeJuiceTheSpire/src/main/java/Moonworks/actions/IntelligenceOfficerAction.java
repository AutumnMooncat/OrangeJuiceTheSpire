package Moonworks.actions;

import Moonworks.powers.SteadyPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import java.util.Iterator;


public class IntelligenceOfficerAction extends AbstractGameAction {
    private final float startingDuration;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final int draw;
    private final int vigorsteady;

    public IntelligenceOfficerAction(int numCards, AbstractCreature target, int vigorsteady, int draw) {
        this.amount = numCards;
        this.vigorsteady = vigorsteady;
        this.draw = draw;
        this.target = target;
        if (AbstractDungeon.player.hasRelic("GoldenEye")) {
            AbstractDungeon.player.getRelic("GoldenEye").flash();
            this.amount += 2;
        }

        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public void update() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
        } else {
            Iterator<?> var1;
            AbstractCard c;
            if (this.duration == this.startingDuration) {

                var1 = AbstractDungeon.player.powers.iterator();
                while(var1.hasNext()) {
                    AbstractPower p = (AbstractPower)var1.next();
                    p.onScry();
                }

                if (AbstractDungeon.player.drawPile.isEmpty()) {
                    this.isDone = true;
                    return;
                }

                CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                if (this.amount != -1) {
                    for(int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
                        tmpGroup.addToTop(AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
                    }
                } else {

                    for (AbstractCard abstractCard : AbstractDungeon.player.drawPile.group) {
                        c = abstractCard;
                        tmpGroup.addToBottom(c);
                    }
                }

                for (AbstractCard card : tmpGroup.group) {
                    switch (card.type) {
                        case ATTACK:
                            this.addToBot(new ApplyPowerAction(target, target, new VigorPower(target, vigorsteady)));
                            break;
                        case SKILL:
                            this.addToBot(new ApplyPowerAction(target, target, new SteadyPower(target, vigorsteady)));
                            break;
                        default:
                            this.addToBot(new DrawCardAction(draw));
                            break;
                    }
                }
                AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
            } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    c = (AbstractCard)var1.next();
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

            var1 = AbstractDungeon.player.discardPile.group.iterator();

            while(var1.hasNext()) {
                c = (AbstractCard)var1.next();
                c.triggerOnScry();
            }

            this.tickDuration();
        }
    }
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
        TEXT = uiStrings.TEXT;
    }
}