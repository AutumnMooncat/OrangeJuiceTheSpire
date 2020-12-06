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

public class NormaBreakEffectAction extends AbstractGameAction {

    private final boolean breakHomemark;

    public NormaBreakEffectAction(AbstractCreature target) {
        this(target, false);
    }

    public NormaBreakEffectAction(AbstractCreature target, boolean breakHomemark) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
        this.breakHomemark = breakHomemark;
    }

    public void update() {

        NormaPower norma = (NormaPower) target.getPower(NormaPower.POWER_ID);
        Homemark homemark = (Homemark) AbstractDungeon.player.getRelic(Homemark.ID);

        if (norma != null) {
            norma.breakNorma();
        }
        if (homemark != null && breakHomemark) {
            homemark.breakHomemark();
        }

        this.isDone = true;
    }
}