package Moonworks.patches;

import Moonworks.powers.FreeCardPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class FreeCardPrefixPatch {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    public static class FreeCardField {

        //Used to hold the boolean for if energy should be lost. Used only by Encore in the time of writing
        public static SpireField<Boolean> free = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch(
            clz = EnergyPanel.class, //This is the class you're patching.
            method = "useEnergy" //This is the name of the method of that class that you're patching.
    )
    public static class OnUseEnergyPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> useEnergyReader(@ByRef int[] e)
        {
            if (FreeCardField.free.get(AbstractDungeon.player)) {
                e[0] = 0;
            } else if (e[0] > 0) {
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
