package Moonworks.actions;

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

import java.util.Iterator;


public class IntelligenceOfficerAction extends AbstractGameAction {
    private final float startingDuration;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final int block;
    private final int draw;
    private final int vigor;

    public IntelligenceOfficerAction(int numCards, AbstractCreature target, int vigor, int block, int draw) {
        this.amount = numCards;
        this.block = block;
        this.vigor = vigor;
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
                            this.addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, vigor, DamageInfo.DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
                            break;
                        case SKILL:
                            this.addToBot(new GainBlockAction(AbstractDungeon.player, block));
                            break;
                        case POWER:
                            //this.addToBot(new GainEnergyAction(draw));
                            this.addToBot(new DrawCardAction(draw));
                            break;
                        case CURSE:
                            //Nothing
                        case STATUS:
                            //Nothing
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