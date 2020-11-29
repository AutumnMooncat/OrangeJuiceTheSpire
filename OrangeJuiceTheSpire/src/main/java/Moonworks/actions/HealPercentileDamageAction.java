package Moonworks.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Donu;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class HealPercentileDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private int healPercentile;
    private final AttackEffect attackEffect;
    private static final float DURATION = 0.1F;

    public HealPercentileDamageAction(AbstractCreature source, AbstractCreature target, DamageInfo info, int healPercentile) {
        this(source, target, info, healPercentile, AttackEffect.NONE);
    }
    public HealPercentileDamageAction(AbstractCreature source, AbstractCreature target, DamageInfo info, int healPercentile, AttackEffect attackEffect) {
        this.info = info;
        this.source = source;
        this.attackEffect = attackEffect;
        this.healPercentile = healPercentile;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }

    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, attackEffect));
            this.target.damage(this.info);
            AbstractDungeon.actionManager.addToBottom(new HealAction(source, source, (int)(target.lastDamageTaken*healPercentile/100F)));
        }
        this.tickDuration();
    }
}
