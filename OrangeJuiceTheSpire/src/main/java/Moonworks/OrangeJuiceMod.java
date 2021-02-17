package Moonworks;

import Moonworks.relics.*;
import Moonworks.variables.DefaultInvertedNumber;
import Moonworks.variables.ThirdMagicNumber;
import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Moonworks.characters.TheStarBreaker;
import Moonworks.potions.OneHundredPercentOrangeJuicePotion;
import Moonworks.util.IDCheckDontTouchPls;
import Moonworks.util.TextureLoader;
import Moonworks.variables.DefaultCustomVariable;
import Moonworks.variables.SecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 3 places.
// I comment those places below, under the place where you set your ID.

//Done: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class OrangeJuiceMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theStarBreakerDefaultSettings = new Properties();

    public static final String ENABLE_SELFDAMAGE_SETTING = "enableSelfDamage";
    public static boolean enableSelfDamage = false; // The boolean we'll be setting on/off (true/false)

    public static final String FIVE_STAR_WANTED_SETTING = "enableStrongerWantedEffect";
    public static boolean enableStrongerWantedEffect = false;

    public static final String DISABLE_GULL_VFX = "disableGullVfx";
    public static boolean disableGullVfx = false;

    public static final String CARD_BATTLE_TALK_SETTING = "enableCardBattleTalk";
    public static boolean enableCardBattleTalkEffect = true;

    public static final String CARD_BATTLE_TALK_PROBABILITY_SETTING = "cardTalkProbability";
    private static final int BASE_CARD_TALK_PROBABILITY = 25;
    public static int cardTalkProbability = BASE_CARD_TALK_PROBABILITY; //Out of 100

    public static final String DAMAGED_BATTLE_TALK_SETTING = "enableDamagedBattleTalk";
    public static boolean enableDamagedBattleTalkEffect = true;

    public static final String DAMAGED_BATTLE_TALK_PROBABILITY_SETTING = "damagedTalkProbability";
    private static final int BASE_DAMAGED_TALK_PROBABILITY = 50;
    public static int damagedTalkProbability = BASE_DAMAGED_TALK_PROBABILITY; //Out of 100

    public static final String PRE_BATTLE_TALK_SETTING = "enablePreBattleTalk";
    public static boolean enablePreBattleTalkEffect = true;

    public static final String PRE_BATTLE_TALK_PROBABILITY_SETTING = "preTalkProbability";
    private static final int BASE_PRE_TALK_PROBABILITY = 75;
    public static int preTalkProbability = BASE_PRE_TALK_PROBABILITY; //Out of 100

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "The Star Breaker";
    private static final String AUTHOR = "Mistress Alison";
    private static final String DESCRIPTION = "Adds Star Breaker (and cards) from 100% Orange Juice! NL See the Mod Options if you would like to change any configurations!";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    //public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);
    //public static final Color WHITE_ICE = CardHelper.getColor(221.0f, 249.0f, 241.0f);
    public static final Color WHITE_ICE = CardHelper.getColor(225.0f, 243.0f, 250.0f);
    public static final Color ATTACK_ORANGE = CardHelper.getColor(162.0f, 97.0f, 28.0f);
    public static final Color SKILL_GREEN = CardHelper.getColor(44.0f, 134.0f, 46.0f);
    public static final Color POWER_BLUE = CardHelper.getColor(46.0f, 91.0f, 138.0f);
    public static final Color TEMP_RED = CardHelper.getColor(145.0f, 33.0f, 31.0f);
    public static final Color GIFT_PINK = CardHelper.getColor(135.0f, 45.0f, 134.0f);
    public static final Color TRAP_PURPLE = CardHelper.getColor(87.0f, 42.0f, 182.0f);
    
    // Potion Colors in RGB
    public static final Color OJ_POTION_LIQUID = CardHelper.getColor(255.0f, 187.0f, 0.0f); // Orange
    public static final Color OJ_POTION_HYBRID = CardHelper.getColor(255.0f, 187.0f, 0.0f); // Darker Orange
    public static final Color OJ_POTION_SPOTS = CardHelper.getColor(255.0f, 187.0f, 0.0f); // Lighter Orange
    
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
  
    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_WHITE_ICE = "MoonworksResources/images/512/bg_attack_coloured.png"; //
    public static final String TEMP_ATTACK_WHITE_ICE = "MoonworksResources/images/512/bg_attack_coloured_temp.png"; //
    public static final String MAGIC_ATTACK_WHITE_ICE = "MoonworksResources/images/512/bg_attack_coloured_magic.png"; //
    private static final String SKILL_WHITE_ICE = "MoonworksResources/images/512/bg_skill_coloured.png"; //
    public static final String GIFT_WHITE_ICE = "MoonworksResources/images/512/bg_skill_coloured_gift.png"; //
    public static final String TRAP_WHITE_ICE = "MoonworksResources/images/512/bg_skill_coloured_trap.png"; //
    public static final String TEMP_SKILL_WHITE_ICE = "MoonworksResources/images/512/bg_skill_coloured_temp.png"; //
    public static final String MAGIC_SKILL_WHITE_ICE = "MoonworksResources/images/512/bg_skill_coloured_magic.png"; //
    private static final String POWER_WHITE_ICE = "MoonworksResources/images/512/bg_power_coloured.png"; //
    
    //private static final String ENERGY_ORB_WHITE_ICE = "MoonworksResources/images/512/card_orb.png"; //
    //private static final String CARD_ENERGY_ORB = "MoonworksResources/images/512/card_small_orb2.png"; //
    //private static final String ENERGY_ORB_WHITE_ICE = "MoonworksResources/images/512/card_orb_star.png"; //
    //private static final String CARD_ENERGY_ORB = "MoonworksResources/images/512/card_small_orb_star.png"; //
    private static final String ENERGY_ORB_WHITE_ICE = "MoonworksResources/images/512/card_orb5.png"; //
    private static final String CARD_ENERGY_ORB = "MoonworksResources/images/512/card_small_orb4.png"; //
    
    private static final String ATTACK_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_attack_coloured.png"; //
    public static final String TEMP_ATTACK_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_attack_coloured_temp.png"; //
    public static final String MAGIC_ATTACK_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_attack_coloured_magic.png"; //
    private static final String SKILL_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_skill_coloured.png"; //
    public static final String GIFT_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_skill_coloured_gift.png"; //
    public static final String TRAP_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_skill_coloured_trap.png"; //
    public static final String TEMP_SKILL_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_skill_coloured_temp.png"; //
    public static final String MAGIC_SKILL_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_skill_coloured_magic.png"; //
    private static final String POWER_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/bg_power_coloured.png"; //
    //private static final String ENERGY_ORB_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/card_orb.png"; //
    //private static final String ENERGY_ORB_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/card_orb_star.png"; //
    private static final String ENERGY_ORB_WHITE_ICE_PORTRAIT = "MoonworksResources/images/1024/card_orb5.png"; //
    
    // Character assets
    //private static final String THE_DEFAULT_BUTTON = "MoonworksResources/images/charSelect/DefaultCharacterButton.png";
    private static final String STARBREAKER_BUTTON = "MoonworksResources/images/charSelect/SBButton.png"; //
    //private static final String THE_DEFAULT_PORTRAIT = "MoonworksResources/images/charSelect/DefaultCharacterPortraitBG.png";
    //private static final String STARBREAKER_PORTRAIT = "MoonworksResources/images/charSelect/Starbreaker8.png";
    //private static final String STARBREAKER_PORTRAIT = "MoonworksResources/images/charSelect/Starbreaker8bg.png";
    private static final String STARBREAKER_PORTRAIT = "MoonworksResources/images/charSelect/StarbreakerScaled.png";
    public static final String STARBREAKER_SHOULDER_1 = "MoonworksResources/images/char/starBreaker/shoulderclean.png"; //
    public static final String STARBREAKER_SHOULDER_2 = "MoonworksResources/images/char/starBreaker/shoulder2clean.png";
    public static final String STARBREAKER_CORPSE = "MoonworksResources/images/char/starBreaker/SBKOSmall.png"; //
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "MoonworksResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "MoonworksResources/images/char/starBreaker/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "MoonworksResources/images/char/starBreaker/skeleton.json";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public OrangeJuiceMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("Moonworks");
        // cool
        // Done: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of theDefault with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        
        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + TheStarBreaker.Enums.COLOR_WHITE_ICE.toString());
        
        BaseMod.addColor(TheStarBreaker.Enums.COLOR_WHITE_ICE, WHITE_ICE, WHITE_ICE, WHITE_ICE,
                WHITE_ICE, WHITE_ICE, WHITE_ICE, WHITE_ICE,
                ATTACK_WHITE_ICE, SKILL_WHITE_ICE, POWER_WHITE_ICE, ENERGY_ORB_WHITE_ICE,
                ATTACK_WHITE_ICE_PORTRAIT, SKILL_WHITE_ICE_PORTRAIT, POWER_WHITE_ICE_PORTRAIT,
                ENERGY_ORB_WHITE_ICE_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theStarBreakerDefaultSettings.setProperty(ENABLE_SELFDAMAGE_SETTING, "FALSE"); // This is the default setting. It's actually set...
        //theStarBreakerDefaultSettings.setProperty(FIVE_STAR_WANTED_SETTING, "FALSE");
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableSelfDamage = config.getBool(ENABLE_SELFDAMAGE_SETTING);
            //enableStrongerWantedEffect = config.getBool(FIVE_STAR_WANTED_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theStarBreakerDefaultSettings.setProperty(FIVE_STAR_WANTED_SETTING, "FALSE");
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableStrongerWantedEffect = config.getBool(FIVE_STAR_WANTED_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theStarBreakerDefaultSettings.setProperty(DISABLE_GULL_VFX, "FALSE");
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            disableGullVfx = config.getBool(DISABLE_GULL_VFX);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theStarBreakerDefaultSettings.setProperty(CARD_BATTLE_TALK_SETTING, "TRUE");
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableCardBattleTalkEffect = config.getBool(CARD_BATTLE_TALK_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theStarBreakerDefaultSettings.setProperty(CARD_BATTLE_TALK_PROBABILITY_SETTING, String.valueOf(BASE_CARD_TALK_PROBABILITY));
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            cardTalkProbability = config.getInt(CARD_BATTLE_TALK_PROBABILITY_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theStarBreakerDefaultSettings.setProperty(DAMAGED_BATTLE_TALK_SETTING, "TRUE");
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableDamagedBattleTalkEffect = config.getBool(DAMAGED_BATTLE_TALK_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theStarBreakerDefaultSettings.setProperty(DAMAGED_BATTLE_TALK_PROBABILITY_SETTING, String.valueOf(BASE_DAMAGED_TALK_PROBABILITY));
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            damagedTalkProbability = config.getInt(DAMAGED_BATTLE_TALK_PROBABILITY_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theStarBreakerDefaultSettings.setProperty(PRE_BATTLE_TALK_SETTING, "TRUE");
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePreBattleTalkEffect = config.getBool(PRE_BATTLE_TALK_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theStarBreakerDefaultSettings.setProperty(PRE_BATTLE_TALK_PROBABILITY_SETTING, PRE_BATTLE_TALK_PROBABILITY_SETTING);
        try {
            SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            preTalkProbability = config.getInt(PRE_BATTLE_TALK_PROBABILITY_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = OrangeJuiceMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = OrangeJuiceMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = OrangeJuiceMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    public static void initialize() {
        logger.info("========================= Initializing The Star Breaker. Hi. =========================");
        OrangeJuiceMod orangeJuiceMod = new OrangeJuiceMod();
        logger.info("========================= /The Star Breaker Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheStarBreaker.Enums.THE_STARBREAKER.toString());
        
        BaseMod.addCharacter(new TheStarBreaker("The Star Breaker", TheStarBreaker.Enums.THE_STARBREAKER),
                STARBREAKER_BUTTON, STARBREAKER_PORTRAIT, TheStarBreaker.Enums.THE_STARBREAKER);
        
        receiveEditPotions();
        logger.info("Added " + TheStarBreaker.Enums.THE_STARBREAKER.toString());
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        //Get the longest slider text for positioning
        ArrayList<String> labelStrings = new ArrayList<>();
        labelStrings.add(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigBattleTalkButton")).TEXT[0]);
        labelStrings.add(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigDamagedTalkButton")).TEXT[0]);
        labelStrings.add(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigPreBattleTalkButton")).TEXT[0]);
        float sliderOffset = getSliderPosition(labelStrings);
        labelStrings.clear();
        float currentYposition = 740f;
        float spacingY = 55f;
        
        // Create the on/off button:
        ModLabeledToggleButton enableSelfDamageButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigSeagulls")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableSelfDamage, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enableSelfDamage = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                config.setBool(ENABLE_SELFDAMAGE_SETTING, enableSelfDamage);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        currentYposition -= spacingY;
        ModLabeledToggleButton enableStrongerWantedButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigWanted")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableStrongerWantedEffect, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enableStrongerWantedEffect = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                        config.setBool(FIVE_STAR_WANTED_SETTING, enableStrongerWantedEffect);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        currentYposition -= spacingY;
        ModLabeledToggleButton disableGullVFXButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigGullVFXButton")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                disableGullVfx, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    disableGullVfx = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                        config.setBool(DISABLE_GULL_VFX, disableGullVfx);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        currentYposition -= spacingY;
        //Used for randomly talking when playing cards
        ModLabeledToggleButton enableCardBattleTalkButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigBattleTalkButton")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableCardBattleTalkEffect, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enableCardBattleTalkEffect = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                        config.setBool(CARD_BATTLE_TALK_SETTING, enableCardBattleTalkEffect);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        ModMinMaxSlider cardBattleTalkSlider = new ModMinMaxSlider("",
                enableCardBattleTalkButton.getX() + sliderOffset,
                enableCardBattleTalkButton.getY() + 20f,
                0, 100, cardTalkProbability, "%.0f",
                settingsPanel,
                slider -> {
                    cardTalkProbability = (int)slider.getValue();
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                        config.setInt(CARD_BATTLE_TALK_PROBABILITY_SETTING, (int) slider.getValue());
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        currentYposition -= spacingY;
        //Used for randomly talking when taking damage
        ModLabeledToggleButton enableDamagedBattleTalkButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigDamagedTalkButton")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableDamagedBattleTalkEffect, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enableDamagedBattleTalkEffect = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                        config.setBool(DAMAGED_BATTLE_TALK_SETTING, enableDamagedBattleTalkEffect);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        ModMinMaxSlider damagedBattleTalkSlider = new ModMinMaxSlider("",
                enableDamagedBattleTalkButton.getX() + sliderOffset,
                enableDamagedBattleTalkButton.getY() + 20f,
                0, 100, damagedTalkProbability, "%.0f",
                settingsPanel,
                slider -> {
                    damagedTalkProbability = (int)slider.getValue();
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                        config.setInt(DAMAGED_BATTLE_TALK_PROBABILITY_SETTING, (int) slider.getValue());
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        currentYposition -= spacingY;
        //Used for randomly talking when combat starts and ends
        ModLabeledToggleButton enablePreBattleTalkButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ModConfigPreBattleTalkButton")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePreBattleTalkEffect, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enablePreBattleTalkEffect = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                        config.setBool(PRE_BATTLE_TALK_SETTING, enablePreBattleTalkEffect);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        ModMinMaxSlider preBattleTalkSlider = new ModMinMaxSlider("",
                enablePreBattleTalkButton.getX() + sliderOffset,
                enablePreBattleTalkButton.getY() + 20f,
                0, 100, preTalkProbability, "%.0f",
                settingsPanel,
                slider -> {
                    preTalkProbability = (int)slider.getValue();
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("starbreakerMod", "StarbreakerConfig", theStarBreakerDefaultSettings);
                        config.setInt(PRE_BATTLE_TALK_PROBABILITY_SETTING, (int) slider.getValue());
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        //currentYposition -= spacingY;

        settingsPanel.addUIElement(enableSelfDamageButton); // Add the button to the settings panel. Button is a go.
        settingsPanel.addUIElement(enableStrongerWantedButton); // Add the button to the settings panel. Button is a go.
        settingsPanel.addUIElement(disableGullVFXButton);
        settingsPanel.addUIElement(enableCardBattleTalkButton);
        settingsPanel.addUIElement(cardBattleTalkSlider);
        settingsPanel.addUIElement(enableDamagedBattleTalkButton);
        settingsPanel.addUIElement(damagedBattleTalkSlider);
        settingsPanel.addUIElement(enablePreBattleTalkButton);
        settingsPanel.addUIElement(preBattleTalkSlider);
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        
        // =============== EVENTS =================
        
        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"

        //No events have been made yet, dont add the default event, that would be silly
        //BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        
        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    //Get the longest text so all sliders are centered
    private float getSliderPosition (ArrayList<String> stringsToCompare) {
        float longest = 0;
        for (String s : stringsToCompare) {
            longest = Math.max(longest, FontHelper.getWidth(FontHelper.charDescFont, s, 1f / Settings.scale));
        }
        return longest + 60f;
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(OneHundredPercentOrangeJuicePotion.class, OJ_POTION_LIQUID, OJ_POTION_HYBRID, OJ_POTION_SPOTS, OneHundredPercentOrangeJuicePotion.POTION_ID, TheStarBreaker.Enums.THE_STARBREAKER);
        
        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // Take a look at https://github.com/daviscook477/BaseMod/wiki/AutoAdd
        // as well as
        // https://github.com/kiooeht/Bard/blob/e023c4089cc347c60331c78c6415f489d19b6eb9/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L319
        // for reference as to how to turn this into an "Auto-Add" rather than having to list every relic individually.
        // Of note is that the bard mod uses it's own custom relic class (not dissimilar to our AbstractDefaultCard class for cards) that adds the 'color' field,
        // in order to automatically differentiate which pool to add the relic too.

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        //BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheDefault.Enums.COLOR_GRAY);
        //BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), TheDefault.Enums.COLOR_GRAY);
        //BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheDefault.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new BrokenBomb(), TheStarBreaker.Enums.COLOR_WHITE_ICE);
        BaseMod.addRelicToCustomPool(new SBBomb(), TheStarBreaker.Enums.COLOR_WHITE_ICE);
        BaseMod.addRelicToCustomPool(new LittleGull(), TheStarBreaker.Enums.COLOR_WHITE_ICE);
        BaseMod.addRelicToCustomPool(new GoldenDie(), TheStarBreaker.Enums.COLOR_WHITE_ICE);
        BaseMod.addRelicToCustomPool(new Homemark(), TheStarBreaker.Enums.COLOR_WHITE_ICE);
        BaseMod.addRelicToCustomPool(new WarpPanel(), TheStarBreaker.Enums.COLOR_WHITE_ICE);

        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);
        
        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        //UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        UnlockTracker.markRelicAsSeen(BrokenBomb.ID);
        UnlockTracker.markRelicAsSeen(SBBomb.ID);
        UnlockTracker.markRelicAsSeen(LittleGull.ID);
        UnlockTracker.markRelicAsSeen(GoldenDie.ID);
        UnlockTracker.markRelicAsSeen(Homemark.ID);
        UnlockTracker.markRelicAsSeen(WarpPanel.ID);
        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        BaseMod.addDynamicVariable(new DefaultInvertedNumber());
        
        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards inside the cards package, found under yourModName.cards.
        // For more specific info, including how to exclude classes from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        new AutoAdd("Moonworks")
            .packageFilter("Moonworks.cards")
            .setDefaultSeen(true)
            .cards();

        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE LOCALIZATION ===================

    private String loadLocalizationIfAvailable(String fileName) {
        if (!Gdx.files.internal(getModID() + "Resources/localization/" + Settings.language.toString().toLowerCase()+ "/" + fileName).exists()) {
            logger.info("Language: " + Settings.language.toString().toLowerCase() + ", not currently supported for" +fileName+".");
            return "eng" + "/" + fileName;
        } else {
            logger.info("Loaded Language: "+ Settings.language.toString().toLowerCase() + ", for "+fileName+".");
            return Settings.language.toString().toLowerCase() + "/" + fileName;
        }
    }

    // ================ /LOAD THE LOCALIZATION/ ===================

    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-Card-Strings.json"));
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-Power-Strings.json"));
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-Relic-Strings.json"));
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-Event-Strings.json"));
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-Potion-Strings.json"));
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-Character-Strings.json"));
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-Orb-Strings.json"));

        // UIStrings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-UI-Strings.json"));
        
        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID()+"Resources/localization/"+loadLocalizationIfAvailable("DefaultMod-Keyword-Strings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
