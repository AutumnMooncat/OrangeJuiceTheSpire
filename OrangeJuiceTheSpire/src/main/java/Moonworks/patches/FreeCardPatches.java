package Moonworks.patches;

import Moonworks.powers.FreeCardPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class FreeCardPatches {

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    private static class FreeCardField {

        //Used to hold the boolean for if energy should be lost. Used only by Encore in the time of writing
        public static final SpireField<Boolean> free = new SpireField<>(() -> Boolean.FALSE);
    }

    public static void setFreeField(AbstractPlayer p, boolean val) {
        FreeCardField.free.set(p, val);
    }

    public static boolean getFreeField(AbstractPlayer p) {
        return FreeCardField.free.get(p);
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
            if (FreeCardPatches.getFreeField(AbstractDungeon.player)) {
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

    @SpirePatch(
            clz = AbstractPlayer.class, //This is the class you're patching.
            method = "preBattlePrep" //This is the name of the method of that class that you're patching.
    )
    public static class PreBattlePatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> preBattlePrep(AbstractPlayer __instance)
        {
            FreeCardPatches.setFreeField(AbstractDungeon.player, false);
            return SpireReturn.Continue();
        }
    }
}
