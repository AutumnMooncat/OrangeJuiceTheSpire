package Moonworks.actions;

import Moonworks.powers.NormaPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class NormaCheckUnblockedDamageHealAction extends AbstractGameAction {

    private final AbstractCreature source;
    private final int normaCheck;

    public NormaCheckUnblockedDamageHealAction(AbstractCreature target, AbstractCreature source, int normaRequired, int healAmount) {
        this.actionType = ActionType.HEAL;
        this.target = target;
        this.source = source;
        this.normaCheck = normaRequired;
        this.amount = healAmount;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (target.lastDamageTaken > 0 && source.hasPower(NormaPower.POWER_ID) && source.getPower(NormaPower.POWER_ID).amount >= normaCheck) {
            this.addToTop(new HealAction(source, source, amount));
        }
        this.isDone = true;
    }
}