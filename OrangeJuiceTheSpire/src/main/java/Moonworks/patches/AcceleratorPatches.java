package Moonworks.patches;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.tempCards.LeapThroughTime;
import Moonworks.cards.magicalCards.MagicalInferno;
import Moonworks.cards.magicalCards.MagicalMassacre;
import Moonworks.cards.magicalCards.MagicalRevenge;
import Moonworks.powers.AcceleratorPower;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class AcceleratorPatches {

    @SpirePatch(
            clz=AbstractGameEffect.class,
            method=SpirePatch.CLASS
    )
    public static class AcceleratorDuplicateField
    {
        public static SpireField<Boolean> acceleratorDuplicate = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch(
            clz=ShowCardAndAddToDrawPileEffect.class,
            method=SpirePatch.CLASS
    )
    public static class ToBottomField
    {
        public static SpireField<Boolean> toBottom = new SpireField<>(() -> Boolean.FALSE);
    }

    //Thanks Jedi#3970 for the base patch code and Johnnydevo#5188 for the SpireField suggestion
    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "update")
    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "update")
    public static class InDraw
    {
        public static void Prefix(AbstractGameEffect __instance)
        {
            if (__instance.duration == (float)ReflectionHacks.getPrivateStatic(__instance.getClass(), "EFFECT_DUR"))
            {
                if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                    AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                    AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card");
                    if (c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE
                            && !(c instanceof MagicalInferno) && !(c instanceof MagicalRevenge) && !(c instanceof MagicalMassacre)
                            && !(c instanceof LeapThroughTime)
                            && AcceleratorDuplicateField.acceleratorDuplicate.get(__instance).equals(Boolean.FALSE)) {
                        for (int i = 0 ; i < p.amount ; i++) {
                            AbstractCard temp = c.makeStatEquivalentCopy();
                            /*temp.target_x = MathUtils.random((float) Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
                            temp.target_y = MathUtils.random((float)Settings.HEIGHT * 0.8F, (float)Settings.HEIGHT * 0.2F);
                            temp.current_x = temp.target_x;
                            temp.current_y = temp.target_y;*/
                            //final AbstractGameEffect[] e = new AbstractGameEffect[1];
                            if (__instance instanceof ShowCardAndAddToDiscardEffect) {
                                /*
                                e[0] = new ShowCardAndAddToDiscardEffect(temp, temp.target_x, temp.target_y);*/
                                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                                    public void update() {
                                        AbstractGameEffect e = new ShowCardAndAddToDiscardEffect(temp, temp.target_x, temp.target_y);
                                        AcceleratorDuplicateField.acceleratorDuplicate.set(e, Boolean.TRUE);
                                        AbstractDungeon.effectList.add(e);
                                        this.isDone = true;
                                    }
                                });
                            }
                            else if (__instance instanceof ShowCardAndAddToHandEffect) {
                                /*
                                if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE - 1) {
                                    e[0] = new ShowCardAndAddToHandEffect(temp, temp.target_x, temp.target_y);
                                } else {
                                    //Put it on the top of the draw pile, because I'm nice. It could go to the discard instead.
                                    e[0] = new ShowCardAndAddToDrawPileEffect(temp, temp.target_x, temp.target_y, false, false);
                                }*/
                                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                                    public void update() {
                                        AbstractGameEffect e;
                                        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE - 1) {
                                            e = new ShowCardAndAddToHandEffect(temp, temp.target_x, temp.target_y);
                                        } else {
                                            //Put it on the top of the draw pile, because I'm nice. It could go to the discard instead.
                                            e = new ShowCardAndAddToDrawPileEffect(temp, temp.target_x, temp.target_y, false, false);
                                        }
                                        AcceleratorDuplicateField.acceleratorDuplicate.set(e, Boolean.TRUE);
                                        AbstractDungeon.effectList.add(e);
                                        this.isDone = true;
                                    }
                                });
                            }
                            else if (__instance instanceof ShowCardAndAddToDrawPileEffect) {
                                /*
                                boolean randomSpot, cardOffset, toBottom;
                                randomSpot = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "randomSpot");
                                cardOffset = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "cardOffset");
                                toBottom = ToBottomField.toBottom.get(__instance);
                                //e[0] = new ShowCardAndAddToDrawPileEffect(temp, temp.target_x, temp.target_y, randomSpot, cardOffset, toBottom);
                                e[0] = new ShowCardAndAddToDrawPileEffect(temp, temp.target_x, temp.target_y, randomSpot, cardOffset, toBottom);
                                //e[0] = new ShowCardAndAddToDrawPileEffect(temp, randomSpot, toBottom);*/
                                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                                    public void update() {
                                        boolean randomSpot, cardOffset, toBottom;
                                        randomSpot = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "randomSpot");
                                        cardOffset = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "cardOffset");
                                        toBottom = ToBottomField.toBottom.get(__instance);
                                        AbstractGameEffect e = new ShowCardAndAddToDrawPileEffect(temp, temp.target_x, temp.target_y, randomSpot, cardOffset, toBottom);
                                        AcceleratorDuplicateField.acceleratorDuplicate.set(e, Boolean.TRUE);
                                        AbstractDungeon.effectList.add(e);
                                        this.isDone = true;
                                    }
                                });
                            } else {
                                OrangeJuiceMod.logger.info("We cloned a ShowCardAndAddEffect that was not a ShowCardAndAddEffect... somehow. Throwing NPE, bug me please.");
                                throw (new NullPointerException());
                            }
                            //I had this removed and much more streamlined, but it was putting every single card on the same spot, which was silly looking
                            /*
                            AcceleratorDuplicateField.acceleratorDuplicate.set(e[0], Boolean.TRUE);
                            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                                public void update() {
                                    AbstractDungeon.effectList.add(e[0]);
                                    this.isDone = true;
                                }
                            });*/
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = ShowCardAndAddToDrawPileEffect.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    AbstractCard.class,
                    float.class,
                    float.class,
                    boolean.class,
                    boolean.class,
                    boolean.class
            })
    public static class ShowCardAndAddToDrawPileEffectPatch1
    {
        @SpirePrefixPatch
        public static void SCAATDPEReaderPre(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom)
        {
            ToBottomField.toBottom.set(__instance, toBottom);
        }

        /*@SpirePostfixPatch
        public static void SCAATDPEReaderPost(ShowCardAndAddToDrawPileEffect __instance, @ByRef AbstractCard[] srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom)
        {
            AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card");
            if (AcceleratorDuplicateField.acceleratorDuplicate.get(srcCard[0]).equals(Boolean.TRUE)) {
                AcceleratorDuplicateField.acceleratorDuplicate.set(c, Boolean.TRUE);
            }
        }*/
    }
    @SpirePatch(
            clz = ShowCardAndAddToDrawPileEffect.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    AbstractCard.class,
                    boolean.class,
                    boolean.class
            })
    public static class ShowCardAndAddToDrawPileEffectPatch2
    {
        @SpirePrefixPatch
        public static void SCAATDPEReaderPre(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, boolean randomSpot, boolean toBottom)
        {
            ToBottomField.toBottom.set(__instance, toBottom);
        }

        /*@SpirePostfixPatch
        public static void SCAATDPEReaderPost(ShowCardAndAddToDrawPileEffect __instance, @ByRef AbstractCard[] srcCard, boolean randomSpot, boolean toBottom)
        {
            AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card");
            if (AcceleratorDuplicateField.acceleratorDuplicate.get(srcCard[0]).equals(Boolean.TRUE)) {
                AcceleratorDuplicateField.acceleratorDuplicate.set(c, Boolean.TRUE);
            }
        }*/
    }

    //Hell lies below, do not venture

    /*@SpirePatch(
            clz = DiscoveryAction.class, //This is the class you're patching.
            method = "update" //This is the name of the method of that class that you're patching.
    )
    public static class DiscoveryActionPatchOld
    {
        @SpirePrefixPatch
        public static SpireReturn<?> DiscoveryReaderOld(DiscoveryAction __instance) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
        {
            if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                //__instance.amount *= (1 + p.amount); //Amount holds the number of copies to make, so add 1 and multiply to make the correct amount.
                //This will almost certainly not work for Discovery, and should be replaced ASAP
                //For 1, we need an insert patch
            }
            return SpireReturn.Continue();
        }
    }
    @SpirePatch(
            clz = DiscoveryAction.class, //This is the class you're patching.
            method = "update" //This is the name of the method of that class that you're patching.
    )
    public static class DiscoveryActionPatch
    {
        public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

        @SpireInsertPatch(locator = DiscoveryActionPatch.Locator.class, localvars = {"disCard"})
        public static SpireReturn<?> DiscoveryReader(DiscoveryAction __instance, @ByRef AbstractCard[] disCard)
        {
            if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                disCard[0].current_x = -1000.0F * Settings.xScale;
            }
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(DiscoveryAction.class, "setCostForTurn");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = MakeTempCardInHandAction.class, //This is the class you're patching.
            method = "update" //This is the name of the method of that class that you're patching.
    )
    public static class MakeTempCardInHandActionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> MTCIHReader(MakeTempCardInHandAction __instance) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
        {
            int duration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardInHandAction.class, "duration");
            int startDuration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardInHandAction.class, "startDuration");
            if (duration == startDuration) {
                if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                    AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                    __instance.amount *= (1 + p.amount); //Amount holds the number of copies to make, so add 1 and multiply to make the correct amount.
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MakeTempCardInDrawPileAction.class, //This is the class you're patching.
            method = "update" //This is the name of the method of that class that you're patching.
    )
    public static class MakeTempCardInDrawPileActionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> MTCIDPReader(MakeTempCardInDrawPileAction __instance) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
        {
            int duration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardInDrawPileAction.class, "duration");
            int startDuration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardInDrawPileAction.class, "startDuration");
            if (duration == startDuration) {
                if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                    AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                    __instance.amount *= (1 + p.amount); //Amount holds the number of copies to make, so add 1 and multiply to make the correct amount.
                }
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = MakeTempCardAtBottomOfDeckAction.class, //This is the class you're patching.
            method = "update" //This is the name of the method of that class that you're patching.
    )
    public static class MakeTempCardAtBottomOfDeckActionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> MTCABODReader(MakeTempCardAtBottomOfDeckAction __instance) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
        {
            int duration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardAtBottomOfDeckAction.class, "duration");
            int startDuration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardAtBottomOfDeckAction.class, "startDuration");
            if (duration == startDuration) {
                if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                    AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                    __instance.amount *= (1 + p.amount); //Amount holds the number of copies to make, so add 1 and multiply to make the correct amount.
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MakeTempCardInDiscardAction.class, //This is the class you're patching.
            method = "update" //This is the name of the method of that class that you're patching.
    )
    public static class MakeTempCardInDiscardActionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> MTCIDReader(MakeTempCardInDiscardAction __instance) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
        {
            int duration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardInDiscardAction.class, "duration");
            int startDuration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardInDiscardAction.class, "startDuration");
            if (duration == startDuration) {
                if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                    AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                    __instance.amount *= (1 + p.amount); //Amount holds the number of copies to make, so add 1 and multiply to make the correct amount.
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MakeTempCardInDiscardAndDeckAction.class, //This is the class you're patching.
            method = "update" //This is the name of the method of that class that you're patching.
    )
    public static class MakeTempCardInDiscardAndDeckActionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> MTCIDReader(MakeTempCardInDiscardAndDeckAction __instance) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
        {
            int duration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardInDiscardAndDeckAction.class, "duration");
            int startDuration = (int) ReflectionHacks.getPrivate(__instance, MakeTempCardInDiscardAndDeckAction.class, "startDuration");
            if (duration == startDuration) {
                if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                    AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                    __instance.amount *= (1 + p.amount); //Amount holds the number of copies to make, so add 1 and multiply to make the correct amount.
                }
            }
            return SpireReturn.Continue();
        }
    }*/
}
