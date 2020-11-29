package Moonworks.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class AmbushAction extends AbstractGameAction{

    private DamageInfo info;

    public AmbushAction(AbstractCreature target, AbstractCreature source, DamageInfo info, int amount) {
        this.actionType = ActionType.DAMAGE;
        this.target = target;
        this.info = info;
        this.source = source;
        this.amount = amount;
    }

    public void update() {

        if(this.target != null && !this.target.hasPower("Vulnerable") && !this.target.hasPower("Artifact")){
            this.info.output *= AbstractDungeon.player.hasRelic("Paper Frog") ? 1.75F : 1.5F;
        }
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(this.target, this.source, new VulnerablePower(this.target, amount, false)));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(this.target, this.info, AttackEffect.BLUNT_LIGHT));
        this.isDone = true;
    }
}