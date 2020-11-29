package Moonworks.patches;

import Moonworks.characters.TheStarBreaker;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

//Alchyr#3696
//This goes in its own file. Call it NoBlockPatch.java or something.
@SpirePatch(
        clz = AbstractCreature.class, //This is the class you're patching.
        method = "addBlock" //This is the name of the method of that class that you're patching.
)
public class AddBlockPrefixPatch
{
    //public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
    @SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
    public static SpireReturn<?> BlockReader(AbstractCreature __instance, @ByRef int[] blockAmount) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
    {
        //To make sure it doesn't interfere with other mods
        if(AbstractDungeon.player.chosenClass == TheStarBreaker.Enums.THE_STARBREAKER) {
            if (__instance instanceof AbstractMonster) {
                float tmp = blockAmount[0];
                //logger.info("Intro block:" + tmp);
                for (AbstractPower p : __instance.powers) {
                    //logger.info("Checking power:" + p);
                    tmp = p.modifyBlock(tmp);
                    //logger.info("Current block:" + tmp);
                }
                //logger.info("Final block:" + tmp);
                blockAmount[0] = MathUtils.floor(tmp);
            }
        }
        return SpireReturn.Continue();
    }
        /*
        if (__instance.hasPower(Heat300PercentPower.POWER_ID))
        {
            AbstractPower p = __instance.getPower(Heat300PercentPower.POWER_ID);
            p.flash();
            p.amount--;
            p.updateDescription();
            if (p.amount == 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p.owner, p.owner, Heat300PercentPower.POWER_ID));
            }
            blockAmount[0] = 0;
            //return SpireReturn.Return(null); //Patches can have a return type of void or SpireReturn<return type of the method>
        }
        if(__instance.hasPower(DexterityPower.POWER_ID)) {
            AbstractPower p = __instance.getPower(DexterityPower.POWER_ID);
            if (p.amount < 0) {
                blockAmount[0] -= p.amount;
            }

        }
        if(__instance.hasPower(FrailPower.POWER_ID)) {
            blockAmount[0] *= 0.75F;
        }
        if(blockAmount[0] < 0) {
            blockAmount[0] = 0;
        }*/
        //return SpireReturn.Continue(); //This allows you to either return early, change the value returned, or just continue like this.
}