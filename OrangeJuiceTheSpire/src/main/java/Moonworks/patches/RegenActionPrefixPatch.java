package Moonworks.patches;

import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.RegenAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SpirePatch(
        clz = RegenAction.class, //This is the class you're patching.
        method = "update" //This is the name of the method of that class that you're patching.
)
public class RegenActionPrefixPatch
{
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    @SpireInsertPatch(locator = RegenActionPrefixPatch.Locator.class)
    public static SpireReturn<?> RegenPatch(RegenAction __instance)
    {
        //To make sure it doesn't interfere with other mods
        if(AbstractDungeon.player.chosenClass == TheStarBreaker.Enums.THE_STARBREAKER) {
            //logger.info("Setting up Regen");
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                if(!__instance.target.isPlayer) {
                    AbstractPower p = __instance.target.getPower("Regeneration");
                    if (p != null) {
                        --p.amount;
                        if (p.amount <= 0) {
                            __instance.target.powers.remove(p);
                        } else {
                            p.updateDescription();
                        }
                        __instance.isDone = true;
                    }
                }
            }
        }
        return SpireReturn.Continue();
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(RegenAction.class, "tickDuration");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}