package Moonworks.util;

import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.NormaPower;
import Moonworks.util.interfaces.NormaAttentiveObject;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import static Moonworks.OrangeJuiceMod.*;

public class NormaHelper {
    //Holds how many skills we need to play for a Norma level
    private static final int NORMAL_DENOMINATOR = 10;
    //Hows how many cards of ANY type we need to play for a Normal level in addition to the normal denominator
    private static final int UPGRADE_PLUS_DENOMINATOR = 2;
    //How high our Norma can go normally
    public static final int MAX_NORMA = 5;

    @SpirePatch(
            clz= AbstractPlayer.class,
            method=SpirePatch.CLASS
    )
    private static class NormaVars {

        //Used to hold the current charges to the next Norma Level
        public static SpireField<Integer> numerator = new SpireField<>(() -> 0);

        //Used to hold the charges needed to get the next Norma Level
        public static SpireField<Integer> denominator = new SpireField<>(() -> NORMAL_DENOMINATOR);

        //Used to hold the Norma Level we should start combat with
        public static SpireField<Integer> baseNorma = new SpireField<>(() -> 0);

        //Used to hold the boolean if numerator is increased by skills only or anything
        public static SpireField<Boolean> skillsOnly = new SpireField<>(() -> Boolean.TRUE);
    }

    //TODO Patch AbstractPlayer onUseCard to increase numerator if applicable
    @SpirePatch(
            clz = AbstractPlayer.class, //This is the class you're patching.
            method = "useCard" //This is the name of the method of that class that you're patching.
    )
    public static class UseCardPrefixPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> useCardReader(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse)
        {
            if (isViable(__instance, c) && shouldApply(__instance)) {
                gainNormaCharge(__instance);
            }

            return SpireReturn.Continue();
        }
    }

    //TODO patch AbstractPlayer atBattleStart to apply Norma if we have a Norma card in our hand or just put that on StarB
    @SpirePatch(
            clz = AbstractPlayer.class, //This is the class you're patching.
            method = "applyStartOfCombatPreDrawLogic" //This is the name of the method of that class that you're patching.
    )
    public static class PreBattlePrepPrefixPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> preBattlePrepReader(AbstractPlayer __instance)
        {
            if (shouldApply(__instance)) {
                initializeNorma(__instance, NormaVars.baseNorma.get(__instance));
            }
            return SpireReturn.Continue();
        }
    }

    //Reset and save norma vars when we die or otherwise delete our save file
    @SpirePatch(
            clz = SaveAndContinue.class, //This is the class you're patching.
            method = "deleteSave" //This is the name of the method of that class that you're patching.
    )
    public static class OnDeleteSavePrefixPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> DeleteSaveReader(AbstractPlayer p)
        {
            resetVars(p);
            saveNormaVars(p);
            return SpireReturn.Continue();
        }
    }

    //Save norma vars
    @SpirePatch(
            clz = SaveAndContinue.class, //This is the class you're patching.
            method = "save" //This is the name of the method of that class that you're patching.
    )
    public static class OnSavePrefixPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> saveReader(SaveFile save)
        {
            saveNormaVars(AbstractDungeon.player);
            return SpireReturn.Continue();
        }
    }

    //Load our norma vars
    @SpirePatch(
            clz = SaveAndContinue.class, //This is the class you're patching.
            method = "loadSaveFile", //This is the name of the method of that class that you're patching.
            paramtypez = {AbstractPlayer.PlayerClass.class}
    )
    public static class OnLoadPrefixPatch
    {
        @SpirePrefixPatch
        public static SpireReturn<?> loadReader(AbstractPlayer.PlayerClass c)
        {
            loadNormaVars(AbstractDungeon.player);
            return SpireReturn.Continue();
        }
    }
    //Save norma values. Called when the game is saved or when the savefile is deleted
    private static void saveNormaVars(AbstractPlayer p) {
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", normaBackupVars);
            config.setInt(NORMA_NUMERATOR, NormaVars.numerator.get(p));
            config.setInt(NORMA_DENOMINATOR, NormaVars.denominator.get(p));
            config.setInt(NORMA_BASE, NormaVars.baseNorma.get(p));
            config.setBool(NORMA_SKILLS_ONLY, NormaVars.skillsOnly.get(p));
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Load norma values. Called on loading saved game
    private static void loadNormaVars(AbstractPlayer p) {
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", normaBackupVars);
            config.load();
            NormaVars.numerator.set(p, config.getInt(NORMA_NUMERATOR));
            NormaVars.denominator.set(p, config.getInt(NORMA_DENOMINATOR));
            NormaVars.baseNorma.set(p, config.getInt(NORMA_BASE));
            NormaVars.skillsOnly.set(p, config.getBool(NORMA_SKILLS_ONLY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Hack an update by changing the values by 0
        for (AbstractRelic r : p.relics) {
            if (r instanceof NormaAttentiveObject) {
                ((NormaAttentiveObject) r).onGainNormaCharge(NormaVars.numerator.get(p), 0);
            }
        }
        for (AbstractPower power : p.powers) {
            if (power instanceof NormaAttentiveObject) {
                ((NormaAttentiveObject) power).onGainNormaCharge(NormaVars.numerator.get(p), 0);
            }
        }
    }

    //TODO patch something to apply the Norma power if we gain a Norma card on a different character

    public static boolean isViable(AbstractPlayer p, AbstractCard c) {
        return !NormaVars.skillsOnly.get(p) || c.type == AbstractCard.CardType.SKILL;
    }

    public static boolean shouldApply(AbstractPlayer p) {
        return (p instanceof TheStarBreaker) || hasNormaCard(p) || alreadyHasNorma(p);
    }

    public static boolean alreadyHasNorma(AbstractPlayer p) {
        return p.hasPower(NormaPower.POWER_ID);
    }

    public static boolean hasNormaCard(AbstractPlayer p) {
        for (AbstractCard c : p.masterDeck.group) {
            if (c instanceof AbstractNormaAttentiveCard) {
                return true;
            }
        }
        return false;
    }

    public static boolean canUpgradeBase(AbstractPlayer p) {
        return NormaVars.baseNorma.get(p) < MAX_NORMA;
    }

    public static void upgradeBase(AbstractPlayer p) {
        NormaVars.baseNorma.set(p, NormaVars.baseNorma.get(p) + 1);
    }

    public static void resetVars(AbstractPlayer p) {
        //Reset all values to the initial amounts
        NormaVars.skillsOnly.set(p, Boolean.TRUE);
        NormaVars.numerator.set(p, 0);
        NormaVars.denominator.set(p, NORMAL_DENOMINATOR);
        NormaVars.baseNorma.set(p, 0);
    }

    public static void upgradeNormaConditions(AbstractPlayer p) {
        //Ensure this can only be applied once, in case we somehow try to upgrade twice
        if (NormaVars.skillsOnly.get(p)) {
            NormaVars.skillsOnly.set(p, Boolean.FALSE);
            modifyNormaDenominator(p, UPGRADE_PLUS_DENOMINATOR);
        }
    }

    public static void modifyNormaDenominator(AbstractPlayer p, int amount) {
        //Modfy denominator, but dont let it go below 1;
        int d = NormaVars.denominator.get(p);
        //Check if this would make us gain a normal level, if so, set our numerator to 1 card away instead
        NormaVars.denominator.set(p, Math.max(1, d + amount));
        int n = NormaVars.numerator.get(p);
        if (n >= NormaVars.denominator.get(p)) {
            NormaVars.numerator.set(p, NormaVars.denominator.get(p)-1);
        }
        //Hack an update by changing the values by 0
        for (AbstractRelic r : p.relics) {
            if (r instanceof NormaAttentiveObject) {
                ((NormaAttentiveObject) r).onGainNormaCharge(NormaVars.numerator.get(p), 0);
            }
        }
        for (AbstractPower power : p.powers) {
            if (power instanceof NormaAttentiveObject) {
                ((NormaAttentiveObject) power).onGainNormaCharge(NormaVars.numerator.get(p), 0);
            }
        }
    }

    public static void gainNormaCharge(AbstractPlayer p) {
        gainNormaCharges(p, 1);
    }

    public static void gainNormaCharges(AbstractPlayer p, int i) {
        int n = NormaVars.numerator.get(p);
        //If we passed negative charges, we don't go below 0.
        NormaVars.numerator.set(p, Math.max(0, n + i));
        checkRollover(p);
        for (AbstractRelic r : p.relics) {
            if (r instanceof NormaAttentiveObject) {
                ((NormaAttentiveObject) r).onGainNormaCharge(NormaVars.numerator.get(p), i);
            }
        }
        for (AbstractPower power : p.powers) {
            if (power instanceof NormaAttentiveObject) {
                ((NormaAttentiveObject) power).onGainNormaCharge(NormaVars.numerator.get(p), i);
            }
        }
    }

    public static void checkRollover(AbstractPlayer p) {
        if (NormaVars.numerator.get(p) >= NormaVars.denominator.get(p)) {
            NormaVars.numerator.set(p, NormaVars.numerator.get(p) - NormaVars.denominator.get(p));
            applyNormaPower(p);
        }
    }

    public static void initializeNorma(AbstractPlayer p, int i) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NormaPower(p, i)));
    }

    public static void applyNormaPower(AbstractPlayer p) {
        applyNormaPower(p, 1);
    }

    public static void applyNormaPower(AbstractPlayer p, int i) {
        //First assume we dont have Norma
        int current = 0;
        //Then grab the power and the amount we actually have if we do have the power
        AbstractPower pow = p.getPower(NormaPower.POWER_ID);
        if (pow instanceof NormaPower) {
            current = pow.amount;
        }
        //Ensure we wont overflow
        i = Math.min(i, MAX_NORMA - current);
        //Add 1 norma level at a time
        if (i > 0) {
            for (int j = 0 ; j < i ; j++) {
                //Update relics
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        for (AbstractRelic r : p.relics) {
                            if (r instanceof NormaAttentiveObject) {
                                ((NormaAttentiveObject) r).onGainNorma(NormaVars.numerator.get(p)+1, 1);
                            }
                        }
                        this.isDone = true;
                    }
                });
                //Update powers
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        for (AbstractPower power : p.powers) {
                            if (power instanceof NormaAttentiveObject) {
                                ((NormaAttentiveObject) power).onGainNorma(NormaVars.numerator.get(p)+1, 1);
                            }
                        }
                        this.isDone = true;
                    }
                });
                //Actually apply the Norma level
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NormaPower(p, i)));
            }
        }
    }

    public static int getBaseNorma(AbstractPlayer p) {
        return NormaVars.baseNorma.get(p);
    }
    public static int getNumerator(AbstractPlayer p) {
        return NormaVars.numerator.get(p);
    }
    public static int getDenominator(AbstractPlayer p) {
        return NormaVars.denominator.get(p);
    }
    public static boolean getSkillsOnly(AbstractPlayer p) {
        return NormaVars.skillsOnly.get(p);
    }
}
