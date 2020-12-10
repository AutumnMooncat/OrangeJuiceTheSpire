package Moonworks.characters;

import Moonworks.cards.*;
import Moonworks.relics.*;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Moonworks.OrangeJuiceMod;

import java.util.ArrayList;

import static Moonworks.OrangeJuiceMod.*;
import static Moonworks.characters.TheStarBreaker.Enums.COLOR_WHITE_ICE;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in DefaultMod-character-Strings.json in the resources

public class TheStarBreaker extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    // These are enums for your Characters color (both general color and for the card library) as well as
    // an enum for the name of the player class - IRONCLAD, THE_SILENT, DEFECT, YOUR_CLASS ...
    // These are all necessary for creating a character. If you want to find out where and how exactly they are used
    // in the basegame (for fun and education) Ctrl+click on the PlayerClass, CardColor and/or LibraryType below and go down the
    // Ctrl+click rabbit hole

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_STARBREAKER;
        @SpireEnum(name = "WHITE_ICE_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_WHITE_ICE; //Jet Stream, Opal, White Ice, Botticelli~, Gulf Stream~,
        @SpireEnum(name = "WHITE_ICE_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 60;//Nerfed from 75
    public static final int MAX_HP = 60;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 6;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("DefaultCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    //private static final String defaultAnim = "MoonworksResources/images/char/defaultCharacter/Spriter/theDefaultAnimation.scml";
    private static final String animSet = "MoonworksResources/images/char/defaultCharacter/Spriter/StarbreakerStuff.scml";

    //private static final SpriterAnimation defaultAnimation = new SpriterAnimation(defaultAnim);
    private static final SpriterAnimation idleAnimation = new SpriterAnimation(animSet);


    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "MoonworksResources/images/char/defaultCharacter/orb/layer1.png",
            "MoonworksResources/images/char/defaultCharacter/orb/layer2.png",
            "MoonworksResources/images/char/defaultCharacter/orb/layer3.png",
            "MoonworksResources/images/char/defaultCharacter/orb/layer4m.png", //
            "MoonworksResources/images/char/defaultCharacter/orb/layer5m.png", //
            "MoonworksResources/images/char/defaultCharacter/orb/layer6.png",
            "MoonworksResources/images/char/defaultCharacter/orb/layer1d.png",
            "MoonworksResources/images/char/defaultCharacter/orb/layer2d.png",
            "MoonworksResources/images/char/defaultCharacter/orb/layer3d.png",
            "MoonworksResources/images/char/defaultCharacter/orb/layer4d.png",
            "MoonworksResources/images/char/defaultCharacter/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public TheStarBreaker(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "MoonworksResources/images/char/defaultCharacter/orb/vfxm.png", null, idleAnimation);


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                STARBREAKER_SHOULDER_2, // campfire pose
                STARBREAKER_SHOULDER_1, // another campfire pose
                STARBREAKER_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        loadAnimation(
                THE_DEFAULT_SKELETON_ATLAS,
                THE_DEFAULT_SKELETON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        //logger.info("GetLoadout, crash?");
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        //* Main Deck

        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Ambush.ID);
        retVal.add(LongDistanceShot.ID);

        //*/

        //* Testing Cards

        //retVal.add(Accelerator.ID);
        //retVal.add(AccelHyper.ID);
        //retVal.add(AirStrike.ID);
        //retVal.add(AngelHand.ID);
        //retVal.add(AnotherUltimateWeapon.ID);
        //retVal.add(Assault.ID);
        //retVal.add(AwakeningOfTalent.ID);
        //retVal.add(BackdoorTrade.ID);
        //retVal.add(BeyondHell.ID);
        //retVal.add(BigBangBell.ID);
        //retVal.add(BigMagnum.ID);
        //retVal.add(BigRocketCannon.ID);
        //retVal.add(BindingChains.ID);
        //retVal.add(Blazing.ID);
        //retVal.add(Bloodlust.ID);
        //retVal.add(CastOff.ID);
        //retVal.add(CloudOfSeagulls.ID);
        //retVal.add(CompletionReward.ID);
        //retVal.add(CookingTime.ID);
        //retVal.add(CrystalBarrier.ID);
        //retVal.add(Dash.ID);
        //retVal.add(DeployBits.ID);
        //retVal.add(DevilHand.ID);
        //retVal.add(Dinner.ID);
        //retVal.add(EvilSpyWorkPreparation.ID);
        //retVal.add(ExtendedPhotonRifle.ID);
        //retVal.add(FinalBattle.ID);
        //retVal.add(FinalSurgery.ID);
        //retVal.add(Flamethrower.ID);
        //retVal.add(ForcedRevival.ID);
        //retVal.add(FullSpeedAlicianrone.ID);
        //retVal.add(Gamble.ID);
        //retVal.add(GoAway.ID);
        //retVal.add(Heat300Percent.ID);
        //retVal.add(HolyNight.ID);
        //retVal.add(ImmovableObject.ID);
        //retVal.add(ImOnFire.ID);
        //retVal.add(IndiscriminateFireSupport.ID);
        //retVal.add(IntelligenceOfficer.ID);
        //retVal.add(JonathanRush.ID);
        //retVal.add(LuckyCharm.ID);
        //retVal.add(LulusLuckyEgg.ID);
        //retVal.add(MagicalInferno.ID);
        //retVal.add(MagicalMassacre.ID);
        //retVal.add(MagicalRevenge.ID);
        //retVal.add(MeltingMemories.ID);
        //retVal.add(MetallicMonocoque.ID);
        //retVal.add(MiracleRedBeanIceCream.ID);
        //retVal.add(MiracleWalker.ID);
        //retVal.add(MixPhenomenon.ID);
        //retVal.add(NicePresent.ID);
        //retVal.add(OutOfAmmo.ID);
        //retVal.add(PlushieMaster.ID);
        //retVal.add(Poppoformation.ID);
        //retVal.add(PortablePudding.ID);
        //retVal.add(RagingMadness.ID);
        //retVal.add(RedAndBlue.ID);
        //retVal.add(ReflectiveShell.ID);
        //retVal.add(ReproductionOfRecords.ID);
        //retVal.add(RocketCannon.ID);
        //retVal.add(SakisCookie.ID);
        //retVal.add(ScrambledEve.ID);
        //retVal.add(SealedMemories.ID);
        //retVal.add(SelfDestruct.ID);
        //retVal.add(ShieldCounter.ID);
        //retVal.add(SinkOrSwim.ID);
        //retVal.add(StarBlastingFuse.ID);
        //retVal.add(StiffCrystal.ID);
        //retVal.add(SubspaceTunnel.ID);
        //retVal.add(TacticalRetreat.ID);
        //retVal.add(TreasureThief.ID);
        //retVal.add(Ubiquitous.ID);
        //retVal.add(UnluckyCharm.ID);
        //retVal.add(UnpaidWork.ID);
        //retVal.add(Wanted.ID);
        //retVal.add(WeAreWaruda.ID);
        //retVal.add(WindyEnchantment.ID);
        //retVal.add(x16BigRocket.ID);

        //*/

        //logger.info("Starter Deck, crash?");
        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(BrokenBomb.ID);
        //retVal.add(GoldenDie.ID);
        //retVal.add(PlaceholderRelic.ID);
        //retVal.add(PlaceholderRelic2.ID);
        //retVal.add(DefaultClickableRelic.ID);
        //retVal.add(LittleGull.ID);
        retVal.add(Homemark.ID);

        UnlockTracker.markRelicAsSeen(BrokenBomb.ID);
        //UnlockTracker.markRelicAsSeen(PlaceholderRelic.ID);
        //UnlockTracker.markRelicAsSeen(PlaceholderRelic2.ID);
        //UnlockTracker.markRelicAsSeen(DefaultClickableRelic.ID);
        //UnlockTracker.markRelicAsSeen(LittleGull.ID);
        UnlockTracker.markRelicAsSeen(Homemark.ID);

        //logger.info("Starter Relic, crash?");
        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_WHITE_ICE;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return OrangeJuiceMod.WHITE_ICE.cpy();
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new RedAndBlue();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new TheStarBreaker(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return OrangeJuiceMod.WHITE_ICE.cpy();
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return OrangeJuiceMod.WHITE_ICE;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    /* //Trying to learn how to animate the character
    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        //logger.info("Played Card?");
        //this.animation = defaultAnimation;
        super.useCard(c, monster, energyOnUse);
        //logger.info(idleAnimation.myPlayer.getBaseAnimation().toString());
        //this.animation = idleAnimation;
    }*/
}
