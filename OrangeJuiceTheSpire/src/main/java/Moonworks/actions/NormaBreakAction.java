package Moonworks.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class NormaBreakAction extends AbstractGameAction {

    private final boolean upgraded;

    public NormaBreakAction(AbstractPlayer target, boolean upgraded) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
        this.upgraded = upgraded;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (target instanceof AbstractPlayer) {
            if (duration == Settings.ACTION_DUR_MED) {
                this.addToTop(new NormaBreakPreambleAction((AbstractPlayer) target));
                tickDuration();
            } else {
                this.addToBot(new NormaBreakEffectAction(target, upgraded));
                this.isDone = true;
            }
        }
    }
}