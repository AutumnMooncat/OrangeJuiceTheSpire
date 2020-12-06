package Moonworks.actions;

import Moonworks.powers.NormaPower;
import Moonworks.relics.Homemark;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

public class NormaBreakAction extends AbstractGameAction {

    private final boolean breakHomemark;
    public NormaBreakAction(AbstractCreature target) {
        this(target, false);
    }

    public NormaBreakAction(AbstractCreature target, boolean breakHomemark) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
        this.breakHomemark = breakHomemark;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_MED) {
            this.addToTop(new NormaBreakPreambleAction(target));
            tickDuration();
        } else {
            this.addToBot(new NormaBreakEffectAction(target, breakHomemark));
            this.isDone = true;
        }
    }
}