package Moonworks.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class NormaBreakAction extends AbstractGameAction {

    private final boolean upgraded;
    public NormaBreakAction(AbstractCreature target) {
        this(target, false);
    }

    public NormaBreakAction(AbstractCreature target, boolean upgraded) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
        this.upgraded = upgraded;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_MED) {
            this.addToTop(new NormaBreakPreambleAction(target));
            tickDuration();
        } else {
            this.addToBot(new NormaBreakEffectAction(target, upgraded));
            this.isDone = true;
        }
    }
}