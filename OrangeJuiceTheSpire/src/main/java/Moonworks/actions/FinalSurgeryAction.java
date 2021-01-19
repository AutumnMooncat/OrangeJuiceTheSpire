package Moonworks.actions;

import Moonworks.powers.Heat300PercentPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FinalSurgeryAction extends AbstractGameAction {
    private DamageInfo info;

    public FinalSurgeryAction(AbstractCreature target, DamageInfo info) {
        this.actionType = ActionType.DAMAGE;
        this.target = target;
        this.info = info;
    }

    public void update() {
        if (this.target != null && (this.target.hasPower("Vulnerable") || this.target.hasPower(Heat300PercentPower.POWER_ID))) {
            this.addToTop(new DamageAction(this.target, this.info, AttackEffect.SLASH_HEAVY));
        }

        this.addToTop(new DamageAction(this.target, this.info, AttackEffect.SLASH_HEAVY));
        this.isDone = true;
    }
}