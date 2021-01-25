package Moonworks.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PowerBypassHelper {

    public static final String BYPASS_ALL = "BYPASS_ALL_POWERS";

    @SpirePatch(
            clz= AbstractCreature.class,
            method=SpirePatch.CLASS
    )
    public static class BypassPowerField
    {
        public static final SpireField<ArrayList<String>> bypassArray = new SpireField<>(ArrayList::new);
    }

    /**
     *
     * @param function - The function to call after bypassing the provided powers
     * @param powersToBypass - The list of powers to bypass for all provided creatures
     * @param creatureToBypass - The (single) creature to bypass the listed powers of. If you need to bypass more than 1 creature, use the array version
     * @return - Returns the generic result of the function provided. You will have to cast this to the return type of the function.
     */
    public static Object performBypass(Supplier<Object> function, ArrayList<String> powersToBypass, AbstractCreature creatureToBypass) {
        return performBypass(function, powersToBypass, new AbstractCreature[]{creatureToBypass});
    }

    /**
     *
     * @param function - The function to call after bypassing the provided powers
     * @param powersToBypass - The list of powers to bypass for all provided creatures
     * @param creaturesToBypass - The creatures to bypass the listed powers of. If you need to bypass a single creature, the singular version
     * @return - Returns the generic result of the function provided. You will have to cast this to the return type of the function.
     */
    public static Object performBypass(Supplier<Object> function, ArrayList<String> powersToBypass, AbstractCreature[] creaturesToBypass) {
        //Define a map for what creature had what powers originally
        HashMap<AbstractCreature, ArrayList<AbstractPower>> backupMap = new HashMap<>();

        //Loop through every creature...
        for (AbstractCreature c : creaturesToBypass) {
            //Make a copy of the powers they had (not currently making a copy, just testing what happens)
            backupMap.put(c, c.powers);

            //Replace the current power list with a filtered one that doesn't have any of the bypassed powers.
            c.powers = c.powers.stream().filter(p -> !powersToBypass.contains(p.ID)).collect(Collectors.toCollection(ArrayList::new));

            //Add the powers removed to each creatures list of bypassed powers, useful if you need to know what was removed in your function call
            //This can be used to say a message or something, or throw a conditional like "If more than 3 powers were ignored..."
            BypassPowerField.bypassArray.get(c).addAll(powersToBypass);
        }

        //Actually evaluate our function. We currently don't have support for action calls, as the actions will be evaluated after we already restore the powers
        Object retVal = function.get();

        //Loop through every creature again...
        for (AbstractCreature c : creaturesToBypass) {
            //Remove all powers we just added from the bypassed powers list. This can get messy with actions, and needs revisiting
            BypassPowerField.bypassArray.get(c).removeAll(powersToBypass);

            //Restore the creatures powers to what they were before
            c.powers = backupMap.get(c);
        }

        //Clear the map, we are no longer using it.
        backupMap.clear();

        //Return whatever the function originally returned, could be a bool, could be void, could be null.
        //It's up to the user to know what to do with it.
        return retVal;
    }

    @SpirePatch(clz = AbstractCreature.class, method = "hasPower")
    public static class hasPowerBypass
    {
        @SpirePrefixPatch
        public static SpireReturn<?> hasPowerReader(AbstractCreature __instance, String targetID)
        {
            if (BypassPowerField.bypassArray.get(__instance).contains(targetID) || BypassPowerField.bypassArray.get(__instance).contains(BYPASS_ALL)) {
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }
}
