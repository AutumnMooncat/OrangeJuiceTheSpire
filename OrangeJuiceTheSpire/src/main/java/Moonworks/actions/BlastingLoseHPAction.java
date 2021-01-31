package Moonworks.actions;

import Moonworks.powers.BlastingLightPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
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
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT || target == null) {
            this.isDone = true;
        } else {
            if (this.duration == 0.1F && this.target.currentHealth > 0) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
                /*int bonusHits = Math.max(1, MathUtils.floor(amount/5f)) - 1;
                for (int i = 0 ; i < bonusHits ; i++) {
                    int dx = MathUtils.random(-100, 100);
                    int dy = MathUtils.random(-100, 100);
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX+dx, target.hb.cY+dy, attackEffect, true));
                }*/

            }

            this.tickDuration();
            if (this.isDone) {
                if (this.target.currentHealth > 0) {
                    this.target.tint.color = Color.ORANGE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.HP_LOSS));
                    this.target.decreaseMaxHealth(this.amount);
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                this.addToTop(new WaitAction(0.1F));
            }

        }
    }
}
