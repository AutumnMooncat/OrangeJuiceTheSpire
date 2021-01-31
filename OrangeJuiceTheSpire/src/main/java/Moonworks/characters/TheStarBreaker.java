package Moonworks.characters;

import Moonworks.CustomAnimationListener;
import Moonworks.CustomSpriterAnimation;
import Moonworks.OrangeJuiceMod;
import Moonworks.RandomChatterHelper;
import Moonworks.cards.Ambush;
import Moonworks.cards.Defend;
import Moonworks.cards.LongDistanceShot;
import Moonworks.cards.Strike;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.cards.abstractCards.AbstractTrapCard;
import Moonworks.cards.giftCards.RedAndBlue;
import Moonworks.cards.tempCards.LeapThroughTime;
import Moonworks.cards.tempCards.StarBlastingLight;
import Moonworks.cards.trapCards.StarBlastingFuse;
import Moonworks.relics.BrokenBomb;
import Moonworks.relics.Homemark;
import Moonworks.vfx.StarBreakerVictoryEffect;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.*;
import static Moonworks.characters.TheStarBreaker.Enums.COLOR_WHITE_ICE;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in DefaultMod-character-Strings.json in the resources

public class TheStarBreaker extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private Color lastCardColor = WHITE_ICE;

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
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;
    //public static final int TALK_PERCENT = 100; //We will make a mod setting for this

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("StarBreaker");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    //private static final String defaultAnim = "MoonworksResources/images/char/defaultCharacter/Spriter/theDefaultAnimation.scml";
    private static final String ANIM_FILE = "MoonworksResources/images/char/starBreaker/Spriter/StarbreakerStuff2.scml";

    //private static final SpriterAnimation defaultAnimation = new SpriterAnimation(defaultAnim);

    //Use a new Animation rather than the same one so we dont spawn in the dead animation if we lose a run and start a new one, lml
    //private static final CustomSpriterAnimation ANIM_SET = new CustomSpriterAnimation(ANIM_FILE);


    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "MoonworksResources/images/char/starBreaker/orb/layer1star4.png",
            "MoonworksResources/images/char/starBreaker/orb/layer2star.png",
            "MoonworksResources/images/char/starBreaker/orb/layer3star.png",
            "MoonworksResources/images/char/starBreaker/orb/layer4star4.png", //
            "MoonworksResources/images/char/starBreaker/orb/layer5star4.png", //
            "MoonworksResources/images/char/starBreaker/orb/layer6star.png",
            "MoonworksResources/images/char/starBreaker/orb/layer1star4d.png",
            "MoonworksResources/images/char/starBreaker/orb/layer2star.png",
            "MoonworksResources/images/char/starBreaker/orb/layer3star.png",
            "MoonworksResources/images/char/starBreaker/orb/layer4star4d.png",
            "MoonworksResources/images/char/starBreaker/orb/layer5star4d.png",};

    //public static final float[] layerSpeeds = {-20.0F, 20.0F, -40.0F, 40.0F, 360.0F};
    public static final float[] layerSpeeds = {-20.0F, 20.0F, -40.0F, 40.0F, 0.0F};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public TheStarBreaker(String name, PlayerClass setClass) {
        //super(name, setClass, orbTextures, "MoonworksResources/images/char/defaultCharacter/orb/vfxm.png", null, idleAnimation);
        super(name, setClass, orbTextures, "MoonworksResources/images/char/starBreaker/orb/vfxstar.png", layerSpeeds, new CustomSpriterAnimation(ANIM_FILE));

        Player.PlayerListener listener = new CustomAnimationListener(this);
        ((CustomSpriterAnimation)this.animation).myPlayer.addListener(listener);


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                STARBREAKER_SHOULDER_2, // campfire pose
                STARBREAKER_SHOULDER_1, // another campfire pose
                STARBREAKER_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        /*
        loadAnimation(THE_DEFAULT_SKELETON_ATLAS, THE_DEFAULT_SKELETON_JSON, 1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        */

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

        //*Gift Cards

        //retVal.add(Bloodlust.ID);
        //retVal.add(Flamethrower.ID);
        //retVal.add(LuckyCharm.ID);
        //retVal.add(MiracleRedBeanIceCream.ID);
        //retVal.add(MiracleRedBeanIceCream.ID);
        //retVal.add(RedAndBlue.ID);
        //retVal.add(UnluckyCharm.ID);
        //retVal.add(WindyEnchantment.ID);

        //*/

        //*Trap Cards

        //retVal.add(BigBangBell.ID);
        //retVal.add(EvilMastermind.ID);
        //retVal.add(GoAway.ID);
        //retVal.add(Heat300Percent.ID);
        //retVal.add(PlushieMaster.ID);
        //retVal.add(Poppoformation.ID);
        //retVal.add(StarBlastingFuse.ID);
        //retVal.add(TreasureThief.ID);
        //retVal.add(Wanted.ID);

        //*/

        //Normal Cards

        //retVal.add(Accelerator.ID);
        //retVal.add(AccelHyper.ID);
        //retVal.add(AirStrike.ID);
        //retVal.add(AngelHand.ID);
        //retVal.add(AnotherUltimateWeapon.ID);
        //retVal.add(Assault.ID);
        //retVal.add(AwakeningOfTalent.ID);
        //retVal.add(BackdoorTrade.ID);
        //retVal.add(BeyondHell.ID);
        //retVal.add(BigMagnum.ID);
        //retVal.add(BigRocketCannon.ID);
        //retVal.add(BindingChains.ID);
        //retVal.add(Blazing.ID);
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
        //retVal.add(ForcedRevival.ID);
        //retVal.add(FullSpeedAlicianrone.ID);
        //retVal.add(Gamble.ID);
        //retVal.add(HolyNight.ID);
        //retVal.add(ImmovableObject.ID);
        //retVal.add(ImOnFire.ID);
        //retVal.add(IndiscriminateFireSupport.ID);
        //retVal.add(IntelligenceOfficer.ID);
        //retVal.add(JonathanRush.ID);
        //retVal.add(LeapThroughSpace.ID);
        //retVal.add(LulusLuckyEgg.ID);
        //retVal.add(MagicalInferno.ID);
        //retVal.add(MagicalMassacre.ID);
        //retVal.add(MagicalRevenge.ID);
        //retVal.add(MeltingMemories.ID);
        //retVal.add(MetallicMonocoque.ID);
        //retVal.add(MiracleWalker.ID);
        //retVal.add(MixPhenomenon.ID);
        //retVal.add(NicePresent.ID);
        //retVal.add(OutOfAmmo.ID);
        //retVal.add(PortablePudding.ID);
        //retVal.add(RagingMadness.ID);
        //retVal.add(ReflectiveShell.ID);
        //retVal.add(ReproductionOfRecords.ID);
        //retVal.add(RocketCannon.ID);
        //retVal.add(SakisCookie.ID);
        //retVal.add(ScrambledEve.ID);
        //retVal.add(SealedMemories.ID);
        //retVal.add(SelfDestruct.ID);
        //retVal.add(ShieldCounter.ID);
        //retVal.add(SinkOrSwim.ID);
        //retVal.add(StiffCrystal.ID);
        //retVal.add(SubspaceTunnel.ID);
        //retVal.add(TacticalRetreat.ID);
        //retVal.add(Ubiquitous.ID);
        //retVal.add(UnpaidWork.ID);
        //retVal.add(WeAreWaruda.ID);
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
        return lastCardColor.cpy();
    }

    @Override
    public void applyEndOfTurnTriggers() {
        lastCardColor = WHITE_ICE;
        super.applyEndOfTurnTriggers();
    }

    @Override
    public void onVictory() {
        //OrangeJuiceMod.logger.info("Test");
        lastCardColor = WHITE_ICE;
        super.onVictory();
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
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE};
    }

    @Override
    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("MoonworksResources/images/scene/greyBg.jpg");
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel("MoonworksResources/images/scene/starbreaker1.png", "ATTACK_MAGIC_FAST_3"));
        panels.add(new CutscenePanel("MoonworksResources/images/scene/starbreaker2.png", "TURN_EFFECT"));
        panels.add(new CutscenePanel("MoonworksResources/images/scene/starbreaker3.png", "CEILING_BOOM_3"));
        return panels;
    }

    @Override
    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
        effects.add(new StarBreakerVictoryEffect());
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


    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        super.useCard(c, monster, energyOnUse);
        switch (c.type) {
            case ATTACK:
                RandomChatterHelper.showChatter(RandomChatterHelper.getAttackText(), cardTalkProbability, enableCardBattleTalkEffect);
                playAnimation("attack");
                lastCardColor = ATTACK_ORANGE;
                break;
            case SKILL:
                if (!(c instanceof StarBlastingLight) && !(c instanceof StarBlastingFuse) && !(c instanceof LeapThroughTime)) {
                    RandomChatterHelper.showChatter(RandomChatterHelper.getSkillText(), cardTalkProbability, enableCardBattleTalkEffect);
                }
                playAnimation("skill");
                lastCardColor = SKILL_GREEN;
                break;
            case POWER:
                RandomChatterHelper.showChatter(RandomChatterHelper.getPowerText(), cardTalkProbability, enableCardBattleTalkEffect);
                playAnimation("happy");
                lastCardColor = POWER_BLUE;
                break;
            case STATUS:
            case CURSE:
            default:
                RandomChatterHelper.showChatter(RandomChatterHelper.getSkillText(), cardTalkProbability, enableCardBattleTalkEffect);
                playAnimation("skill");
                lastCardColor = WHITE_ICE;
                break;
        }
        if (c instanceof AbstractTempCard) {
            lastCardColor = TEMP_RED;
        } else if (c instanceof AbstractGiftCard) {
            lastCardColor = GIFT_PINK;
        } else if (c instanceof AbstractTrapCard) {
            lastCardColor = TRAP_PURPLE;
        } else if (c instanceof AbstractMagicalCard) {
            lastCardColor = POWER_BLUE;
        }
    }

    public void damage(DamageInfo info) {
        boolean hadBlockBeforeSuper = this.currentBlock > 0;
        super.damage(info);
        boolean hasBlockAfterSuper = this.currentBlock > 0;
        boolean tookNoDamage = this.lastDamageTaken == 0;
        if (hadBlockBeforeSuper && (hasBlockAfterSuper || tookNoDamage)) {
            RandomChatterHelper.showChatter(RandomChatterHelper.getBlockedDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
            playAnimation("skill");
            //playAnimation("happy");
        } else {
            if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
                if (info.output >= 15) {
                    RandomChatterHelper.showChatter(RandomChatterHelper.getHeavyDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
                } else {
                    RandomChatterHelper.showChatter(RandomChatterHelper.getLightDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
                }
            } else if (info.type == DamageInfo.DamageType.THORNS && info.output > 0) {
                RandomChatterHelper.showChatter(RandomChatterHelper.getFieldDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
            }
            playAnimation("hurt");
        }
    }

    public void playAnimation(String name) {
        ((CustomSpriterAnimation)this.animation).myPlayer.setAnimation(name);
    }

    public void stopAnimation() {
        CustomSpriterAnimation anim = (CustomSpriterAnimation) this.animation;
        int time = anim.myPlayer.getAnimation().length;
        anim.myPlayer.setTime(time);
        anim.myPlayer.speed = 0;
    }

    public void resetToIdleAnimation() {
        playAnimation("idle");
    }

    @Override
    public void playDeathAnimation() {
        RandomChatterHelper.showChatter(RandomChatterHelper.getKOText(), preTalkProbability, enablePreBattleTalkEffect); // I don't think this works
        playAnimation("ko");
    }

    @Override
    public void heal(int healAmount) {
        if (healAmount > 0) {
            if (RandomChatterHelper.showChatter(RandomChatterHelper.getHealingText(), damagedTalkProbability, enableDamagedBattleTalkEffect)){ //Technically changes your hp, lol
                playAnimation("happy");
            }
        }
        super.heal(healAmount);
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        boolean bossFight = false;
        for (AbstractMonster mons : AbstractDungeon.getMonsters().monsters) {
            if (mons.type == AbstractMonster.EnemyType.BOSS) {
                bossFight = true;
                break;
            }
        }
        if (AbstractDungeon.getCurrRoom().eliteTrigger || bossFight) {
            RandomChatterHelper.showChatter(RandomChatterHelper.getBossFightText(), preTalkProbability, enablePreBattleTalkEffect);
        } else {
            if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth*0.5f) {
                RandomChatterHelper.showChatter(RandomChatterHelper.getLowHPBattleStartText(), preTalkProbability, enablePreBattleTalkEffect);
            } else {
                RandomChatterHelper.showChatter(RandomChatterHelper.getBattleStartText(), preTalkProbability, enablePreBattleTalkEffect);
            }
        }
    }

    //Maybe, currently just moved to Homemark relic
    /*
    @Override
    public void loseGold(int goldAmount) {
        super.loseGold(goldAmount);
    }*/
}
