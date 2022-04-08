package Moonworks.patches;
/*
import Moonworks.OrangeJuiceMod;
//import Moonworks.cards.cutCards.LeapThroughTime;
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

public class CleanAcceleratorPatches {

    @SpirePatch(clz=AbstractGameEffect.class, method=SpirePatch.CLASS)
    public static class AcceleratorDuplicateField
    {
        //We need this spire field so we know that our action was already duplicated and we should not create additional duplicates
        public static SpireField<Boolean> acceleratorDuplicate = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch(clz=ShowCardAndAddToDrawPileEffect.class, method=SpirePatch.CLASS)
    public static class ToBottomField
    {
        //ShowCardAndAddToDrawPileEffect does not have a toBottom variable, so we need to grab it manually
        public static SpireField<Boolean> toBottom = new SpireField<>(() -> Boolean.FALSE);
    }

    //Thanks Jedi#3970 for the base patch code and Johnnydevo#5188 for the SpireField suggestion
    //We patch the main 3 ShowCardAndAddTo... effects as mostly everything else should extend them
    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "update")
    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "update")
    public static class CardCreatedPatch
    {
        @SpirePrefixPatch
        public static void cardCreated(AbstractGameEffect __instance)
        {
            //We could do a postfix if(instance.isDone), but then we have to wait 1.5s for our duplicated cards to appear. Its better to make them at the start.
            //Grab the initial duration of the effect, then we can do if (instance.duration = startDuration)
            //It shouldn't matter which effect to pick since all 3 have the same static final EFFECT_DUR = 1.5f;
            float startDuration = ReflectionHacks.getPrivateStatic(ShowCardAndAddToDiscardEffect.class, "EFFECT_DUR");
            //If the action just started...
            if (__instance.duration == startDuration) {
                //Check if the player has Accelerator
                if(AbstractDungeon.player.hasPower(AcceleratorPower.POWER_ID)) {
                    //Grab it for reference, since we will need its amount later
                    AcceleratorPower p = (AcceleratorPower) AbstractDungeon.player.getPower(AcceleratorPower.POWER_ID);
                    //Grab the card that we are creating. For actions that make multiple cards, it always makes a single card multiple times, so this works fine
                    AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card");
                    //If its not already a duplicated action and not a card we wish to not duplicate, such as a curse or other problematic cards...
                    if (c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE
                            && !(c instanceof MagicalInferno) && !(c instanceof MagicalRevenge) && !(c instanceof MagicalMassacre)
                            && AcceleratorDuplicateField.acceleratorDuplicate.get(__instance).equals(Boolean.FALSE)) {
                        //Create a loop to make more actions. The limit of the loop is how many stacks of Accelerator we have
                        for (int i = 0 ; i < p.amount ; i++) {
                            //Make a stat equivalent copy of the card we are creating
                            AbstractCard temp = c.makeStatEquivalentCopy();
                            //Check to see if our action is or extends AddToDiscard
                            if (__instance instanceof ShowCardAndAddToDiscardEffect) {
                                //If it is, we need to add a new AddToDiscard effect to the action manager
                                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                                    public void update() {
                                        //We define the effect without adding it yet
                                        AbstractGameEffect e = new ShowCardAndAddToDiscardEffect(temp, temp.target_x, temp.target_y);
                                        //Very important: we set this effect as a duplicate, so we wont get picked up and duplicated again
                                        AcceleratorDuplicateField.acceleratorDuplicate.set(e, Boolean.TRUE);
                                        //Actually add it to the effect list
                                        AbstractDungeon.effectList.add(e);
                                        this.isDone = true;
                                    }
                                });
                            }
                            //Check to see if our action is or extends AddToHand
                            else if (__instance instanceof ShowCardAndAddToHandEffect) {
                                //If it is, we need to add a new AddToHand effect to the action manager
                                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                                    public void update() {
                                        //Define an effect, but we need to check if our hand will support the new cards or not!
                                        AbstractGameEffect e;
                                        //If our hand is smaller than the max hand size (minus 1! since there is still the card from the initial effect that needs to be added)
                                        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE - 1) {
                                            //Actually add it to our hand
                                            e = new ShowCardAndAddToHandEffect(temp, temp.target_x, temp.target_y);
                                        } else {
                                            //Put it on the top of the draw pile, because I'm nice. It could go to the discard instead, if you prefer.
                                            e = new ShowCardAndAddToDrawPileEffect(temp, temp.target_x, temp.target_y, false, false);
                                        }
                                        //Like before, make sure the duplicate field is set so this action doesn't get repeated in an infinite loop.
                                        AcceleratorDuplicateField.acceleratorDuplicate.set(e, Boolean.TRUE);
                                        //Actually add it to the effect list
                                        AbstractDungeon.effectList.add(e);
                                        this.isDone = true;
                                    }
                                });
                            }
                            //Check to see if our action is or extends AddToDraw
                            else if (__instance instanceof ShowCardAndAddToDrawPileEffect) {
                                //If it is, we need to add a new AddToDraw to the action manager
                                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                                    public void update() {
                                        //Slightly more complicated, we need to know if the card goes to a random spot, whether or not it uses offset, and if it goes to the bottom
                                        boolean randomSpot, cardOffset, toBottom;
                                        //We can grab randomSpot directly
                                        randomSpot = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "randomSpot");
                                        //Same with offset
                                        cardOffset = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "cardOffset");
                                        //We have to do this one manually, with the lower patches. This is because toBottom is not saved as a variable so we cant ReflectionHacks it
                                        toBottom = ToBottomField.toBottom.get(__instance);
                                        //Armed with these 3, set up anew action
                                        AbstractGameEffect e = new ShowCardAndAddToDrawPileEffect(temp, temp.target_x, temp.target_y, randomSpot, cardOffset, toBottom);
                                        //Like always, no infinite loops please
                                        AcceleratorDuplicateField.acceleratorDuplicate.set(e, Boolean.TRUE);
                                        //Actually add it to the effect list
                                        AbstractDungeon.effectList.add(e);
                                        this.isDone = true;
                                    }
                                });
                            } else {
                                //Something went wrong, lol. It should be impossible to get here unless some other mod patches my own patch
                                OrangeJuiceMod.logger.info("We cloned a ShowCardAndAddEffect that was not a ShowCardAndAddEffect... somehow. Throwing NPE, bug me please.");
                                throw (new NullPointerException());
                            }
                        }
                    }
                }
            }
        }
    }

    //Constructor patches take the list of arguments. There are 2 that have a toBottom argument, so we patch both of them
    //As for the constructors that don't take a toBottom argument, they default to false anyway, so its okay
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
        public static void ConstructorPatch1(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom)
        {
            //As a prefix, we set the toBottom field to whatever value was passed to the constructor
            ToBottomField.toBottom.set(__instance, toBottom);
        }
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
        public static void ConstructorPatch2(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, boolean randomSpot, boolean toBottom)
        {
            //As a prefix, we set the toBottom field to whatever value was passed to the constructor
            ToBottomField.toBottom.set(__instance, toBottom);
        }
    }
}

*/
