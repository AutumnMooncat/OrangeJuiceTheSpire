package Moonworks.augments;

import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;
import Moonworks.OrangeJuiceMod;
import basemod.AutoAdd;

public class AugmentHelper {
    public static void register() {
        CardAugmentsMod.registerMod(OrangeJuiceMod.getModID(), OrangeJuiceMod.TEXT[6]);
        new AutoAdd(OrangeJuiceMod.getModID())
                .packageFilter("Moonworks.augments")
                .any(AbstractAugment.class, (info, abstractAugment) -> {
                    CardAugmentsMod.registerAugment(abstractAugment);});
    }
}
