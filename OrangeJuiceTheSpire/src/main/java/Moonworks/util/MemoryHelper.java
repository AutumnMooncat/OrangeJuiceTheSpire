package Moonworks.util;

import Moonworks.OrangeJuiceMod;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.getRandomMonster;

public class MemoryHelper {

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class MemoryAssociation
    {
        public static final SpireField<ArrayList<AbstractPower>> associations = new SpireField<>(ArrayList::new);
    }

    @SpirePatch(
            clz= AbstractCard.class,
            method=SpirePatch.CLASS
    )
    private static class MemoryVars
    {
        //Used to hold the current cooldown value of a Memory
        public static SpireField<Integer> cooldown = new SpireField<>(() -> -1);

        //Used to hold the max cooldown value of a Memory. Will be set equal to it's base cost + 1, or Energy Max + 1 For X Costs
        public static SpireField<Integer> baseCooldown = new SpireField<>(() -> -1);

        //Used to ensure a Memory can only be pulled once per turn
        public static SpireField<Boolean> active = new SpireField<>(()-> Boolean.FALSE);

        //Used to denote an X-Cost Memory
        public static SpireField<Boolean> xCost = new SpireField<>(() -> Boolean.FALSE);

        //Used for now to denote an exhausted Memory. Hacky workaround for now
        public static SpireField<Boolean> exhaustedMemory = new SpireField<>(() -> Boolean.FALSE);

    }

    /**
     * Initialize the cooldown values.
     * Defaults:
     * cooldown: 0 (ready)
     * active: true (ready)
     * baseCooldown: cost+1 (will require 1 turn of downtime per cost of the card), or EnergyMax+1 For X Costs
     * @param c - The card to initialize cooldowns values for
     */
    public static void convertMemory(AbstractCard c) {
        if (c.cost == -1) {
            MemoryVars.xCost.set(c, true);
            MemoryVars.baseCooldown.set(c, EnergyPanel.totalCount+1);
        } else {
            MemoryVars.baseCooldown.set(c, c.cost+1);
        }
        MemoryVars.cooldown.set(c, 0);
        MemoryVars.active.set(c, true);
    }

    /**
     * Get the current cooldown value of a card
     * @param c - The card to get the value of
     * @return - Returns the current cooldown value
     */
    public static Integer getCooldown(AbstractCard c) {
        return MemoryVars.cooldown.get(c);
    }

    /**
     * Gerts the base cooldown of a card
     * @param c - The card in question
     * @return - Returns the base cooldown, which is cost+1 by default
     */
    public static Integer getBaseCooldown(AbstractCard c) {
        return MemoryVars.baseCooldown.get(c);
    }

    /**
     * Checks if a Memory is cooldown ready
     * @param c - Card to check
     * @return - Returns true if this Memory is cooldown ready (cooldown is 0)
     */
    public static boolean cooldownReady(AbstractCard c) {
        return getCooldown(c) == 0;
    }

    /**
     * Checks if a Memory is active ready
     * @param c - Card to check
     * @return - Returns true if this Memory is active (true)
     */
    public static boolean isActive(AbstractCard c) {
        return MemoryVars.active.get(c);
    }

    /**
     * Used to set the active Flag on a card
     * @param c - The card to set
     * @param isActive - The cards active flag is set to this
     */
    public static void setActive(AbstractCard c, boolean isActive) {
        MemoryVars.active.set(c, isActive);
    }

    /**
     * Checks if this Memory is ready to be used
     * @param c - The card to check
     * @return - Returns true if the Memory is cooldown ready and active and not exhausted
     */
    public static boolean isReadyToUse(AbstractCard c) {
        return isActive(c) && cooldownReady(c) && !isExhausted(c);
    }

    /**
     * Checks if a Memory is exhausted (forgotten)
     * @param c - The card to check
     * @return - Returns the exhaustedMemory MemoryVar.
     */
    public static boolean isExhausted(AbstractCard c) {
        return MemoryVars.exhaustedMemory.get(c);
    }

    public static void setExhausted(AbstractCard c, boolean isExhausted) {
        MemoryVars.exhaustedMemory.set(c, isExhausted);
    }

    //Used to set cooldowns to 0
    public static void refreshCooldown(AbstractCard c) {
        MemoryVars.cooldown.set(c, 0);
        setActive(c, true);
    }

    //Used to reset a counter to it's max value
    public static void resetCooldown(AbstractCard c) {
        MemoryVars.cooldown.set(c, getBaseCooldown(c));
    }

    //Count a cooldown down
    public static void decrementCooldown(AbstractCard c) {
        MemoryVars.cooldown.set(c, Math.max(0, getCooldown(c) - 1));
    }

    //Get a random Memory, even if it is not ready. It still cant be exhausted though
    public static AbstractCard getRandom(CardGroup cards) {
        HashSet<AbstractCard> set = cards.group.stream().filter(c -> !isExhausted(c)).collect(Collectors.toCollection(HashSet::new));
        if (set.size() == 0) {
            return null;
        }
        return (AbstractCard) set.toArray()[MathUtils.random(set.size()-1)];
    }

    //Get all ready Memories
    public static HashSet<AbstractCard> getAllReady(CardGroup cards) {
        return cards.group.stream().filter(MemoryHelper::isReadyToUse).collect(Collectors.toCollection(HashSet::new));
    }

    //Get all ready Memories of same type
    public static HashSet<AbstractCard> getAllReady(CardGroup cards, AbstractCard.CardType type) {
        return cards.getCardsOfType(type).group.stream().filter(MemoryHelper::isReadyToUse).collect(Collectors.toCollection(HashSet::new));
    }
    //Get a random ready Memory, null if none are available
    public static AbstractCard getRandomReady(CardGroup cards) {
        HashSet<AbstractCard> set = getAllReady(cards);
        if (set.size() == 0) {
            return null;
        }
        return (AbstractCard) set.toArray()[MathUtils.random(set.size()-1)];
    }

    //Get a random ready Memory of same type, null if none are available
    public static AbstractCard getRandomReady(CardGroup cards, AbstractCard.CardType type) {
        HashSet<AbstractCard> set = getAllReady(cards, type);
        if (set.size() == 0) {
            return null;
        }
        return (AbstractCard) set.toArray()[MathUtils.random(set.size()-1)];
        /*AbstractCard[] array = (AbstractCard[]) cards.stream().filter(MemoryHelper::isReadyToUse).filter(c -> c.type == type).toArray();
        if (array.length == 0) {
            return null;
        } else if (array.length == 1) {
            return array[0];
        } else {
            return array[MathUtils.random(array.length-1)];
        }*/
    }

    //Play a random ready Memory
    public static void useRandomReady(CardGroup cards) {
        AbstractCard c = getRandomReady(cards);
        if (c != null) {
            useMemory(c);
        }
    }

    //Play a random ready Memory of same type
    public static void useRandomReadyOfType(CardGroup cards, AbstractCard.CardType type) {
        AbstractCard c = getRandomReady(cards, type);
        if (c != null) {
            useMemory(c);
        }
    }

    //Play the effect of a Memory, handle ensuring it is active elsewhere
    public static void useMemory(AbstractCard c) {
        //Get a random monster
        AbstractMonster t = getRandomMonster();

        //Use it on them
        useMemory(c, t);
    }

    //Play the effect of a Memory on a specific monster, handle ensuring it is active elsewhere
    public static void useMemory(AbstractCard c, AbstractMonster t) {

        //If it isn't null, which will hopefully be the case
        if (t != null) {

            //OrangeJuiceMod.logger.info("Using Memory: "+c);
            //Get the current energy, so we cant go below this (else we clearly failed to play it no cost)
            int originalEnergy = EnergyPanel.getCurrentEnergy();

            //Calculate the stuff
            c.applyPowers();
            c.calculateCardDamage(t);

            //Unused, since the cards wont be in the hand now
            //Flash the card the color of a Power
            //c.flash(OrangeJuiceMod.POWER_BLUE.cpy());
            //card.flash(Color.PURPLE.cpy()); //Purple is nice too

            //Set the card as a no cost flag, should hopefully cover any X cost or lose Energy code. TODO it wont, lol
            c.freeToPlayOnce = true;

            //Use the card on the target
            c.use(AbstractDungeon.player, t);

            //Sure hope this works for Exhaustive cards, lol
            if(ExhaustiveField.ExhaustiveFields.baseExhaustive.get(c) != -1) {
                ExhaustiveVariable.increment(c);
            }

            //Exhaust it if it would have exhausted when played
            if (c.exhaust || c.exhaustOnUseOnce || c.purgeOnUse) {
                setExhausted(c, true);
                //This needs to be something different, since the Memories are stored elsewhere
                //this.addToBot(new WitherExhaustImmediatelyAction(c));
            }

            //Reset the cooldown back to the base value
            resetCooldown(c);

            //Set the card as not active, since we used it. This will be freed at the end of the turn
            setActive(c, false);

            //Recover our energy if we lost any
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    if (originalEnergy < EnergyPanel.getCurrentEnergy()) {
                        //lol this wont worth because use is an action and we cant predict how many layers deep it does to delay our own recover action
                        EnergyPanel.setEnergy(originalEnergy);
                    }
                    this.isDone = true;
                }
            });

            c.initializeDescription();
        }
    }
}
