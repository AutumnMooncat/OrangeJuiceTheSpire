package Moonworks.patches;

import Moonworks.characters.TheStarBreaker;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpirePatch(
        clz = RegenPower.class, //This is the class you're patching.
        method = "updateDescription" //This is the name of the method of that class that you're patching.
)
public class RegenDescriptionPostfixPatch
{
    //public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
    @SpirePostfixPatch //This means this is going to be added at the "start" of the thing you're patching.
    public static SpireReturn<?> DescriptionPatch(RegenPower __instance)
    {
        //To make sure it doesn't interfere with other mods
        if(AbstractDungeon.player.chosenClass == TheStarBreaker.Enums.THE_STARBREAKER) {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                if(!__instance.owner.isPlayer) {
                    __instance.description = RegenerateMonsterPower.DESCRIPTIONS[0] + __instance.amount + RegenPower.DESCRIPTIONS[1];
                }
            }
        }
        return SpireReturn.Continue();
    }
}