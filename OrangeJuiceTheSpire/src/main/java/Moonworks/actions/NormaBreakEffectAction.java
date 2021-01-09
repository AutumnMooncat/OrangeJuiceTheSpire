package Moonworks.actions;

import Moonworks.powers.NormaPower;
import Moonworks.relics.Homemark;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class NormaBreakEffectAction extends AbstractGameAction {

    private final boolean upgraded;

    public NormaBreakEffectAction(AbstractCreature target) {
        this(target, false);
    }

    public NormaBreakEffectAction(AbstractCreature target, boolean upgraded) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
        this.upgraded = upgraded;
    }

    public void update() {

        NormaPower norma = (NormaPower) target.getPower(NormaPower.POWER_ID);
        //Homemark homemark = (Homemark) AbstractDungeon.player.getRelic(Homemark.ID);

        if (norma != null) {
            norma.breakNorma(upgraded);
        }

        this.isDone = true;
    }
}