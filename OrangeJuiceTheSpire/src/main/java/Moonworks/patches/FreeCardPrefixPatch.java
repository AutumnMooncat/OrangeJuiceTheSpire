package Moonworks.patches;

import Moonworks.powers.FreeCardPower;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class FreeCardPrefixPatch {

    @SpirePatch(
            clz = EnergyPanel.class, //This is the class you're patching.
            method = "useEnergy" //This is the name of the method of that class that you're patching.
    )
    public static class OnUseEnergyPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> useEnergyReader(@ByRef int[] e)
        {
            if (e[0] > 0) {
                AbstractPower pow = AbstractDungeon.player.getPower(FreeCardPower.POWER_ID);
                if (pow instanceof FreeCardPower) {
                    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, pow, 1));
                    e[0] = 0;
                }
            }

            return SpireReturn.Continue();
        }
    }
}
