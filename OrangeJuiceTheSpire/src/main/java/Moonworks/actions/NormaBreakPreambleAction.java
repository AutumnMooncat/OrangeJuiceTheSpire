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

public class NormaBreakPreambleAction extends AbstractGameAction {


    public NormaBreakPreambleAction(AbstractCreature target) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToTop(new VFXAction(target, new ScreenOnFireEffect(), 0.0F));
            for (int i = 0 ; i < 20 ; i++) {
                this.addToTop(new ApplyPowerAction(target, target, new NormaPower(target, 1)));
            }
        }
        this.isDone = true;
    }
}