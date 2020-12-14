package Moonworks.actions;

import Moonworks.powers.BlastingLightPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BlastingLoseHPAction extends AbstractGameAction {
    private DamageInfo info;
    private final AttackEffect attackEffect;
    private static final float DURATION = 0.1F;

    public BlastingLoseHPAction(AbstractCreature source, AbstractCreature target, int amount, AttackEffect attackEffect) {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.attackEffect = attackEffect;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
        } else {
            if (this.duration == 0.33F && this.target.currentHealth > 0) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.target.currentHealth > 0) {
                    this.target.tint.color = Color.CHARTREUSE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS));
                }

                AbstractPower p = this.target.getPower(BlastingLightPower.POWER_ID);
                if (p != null) {
                    ++p.amount;
                    int i = AbstractDungeon.cardRandomRng.random(1, 10);
                    if (p.amount > i) {
                        this.target.powers.remove(p);
                    } else {
                        p.updateDescription();
                    }
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                this.addToTop(new WaitAction(0.1F));
            }

        }
    }
}
