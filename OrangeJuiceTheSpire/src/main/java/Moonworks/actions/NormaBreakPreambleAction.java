package Moonworks.actions;

import Moonworks.util.NormaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

public class NormaBreakPreambleAction extends AbstractGameAction {


    public NormaBreakPreambleAction(AbstractPlayer target) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && target instanceof AbstractPlayer) {
            this.addToTop(new VFXAction(target, new ScreenOnFireEffect(), 0.0F));
            NormaHelper.applyNormaPowerNoTriggers((AbstractPlayer) target, 20);
        }
        this.isDone = true;
    }
}