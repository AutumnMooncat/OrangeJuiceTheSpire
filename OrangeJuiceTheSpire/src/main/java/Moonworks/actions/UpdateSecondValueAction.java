package Moonworks.actions;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class UpdateSecondValueAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private final TwoAmountPower power;

    public UpdateSecondValueAction(AbstractCreature source, AbstractCreature target, TwoAmountPower power, int amount) {
        this.source = source;
        this.target = target;
        this.power = power;
        this.amount = amount;
        this.actionType = ActionType.POWER;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION && this.target != null) {
            power.amount2+=amount;
            power.updateDescription();
        }
        this.tickDuration();
    }
}
