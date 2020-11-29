package Moonworks.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Donu;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class CookingTimeAction extends AbstractGameAction {
    private final AttackEffect attackEffect;
    private static final float DURATION = 0.1F;

    public CookingTimeAction(AbstractCreature source, AbstractCreature target, int healAmount) {
        this(source, target, healAmount, AttackEffect.NONE);
    }

    public CookingTimeAction(AbstractCreature source, AbstractCreature target, int healAmount, AttackEffect attackEffect) {
        this.attackEffect = attackEffect;
        this.setValues(target, source, healAmount);
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            int healSum = 0;
            healSum += Math.min((source.maxHealth - source.currentHealth), amount);
            this.addToBot(new HealAction(source, source, amount));
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped()) {
                    healSum += Math.min((aM.maxHealth - aM.currentHealth), amount);
                    this.addToBot(new HealAction(aM, source, amount));
                }
            }
            this.addToBot(new DamageAction(target, new DamageInfo(source, healSum, DamageInfo.DamageType.NORMAL), attackEffect));
        }
        this.tickDuration();
    }
}
