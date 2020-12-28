package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.CorruptedModifier;
import Moonworks.cards.LeapThroughSpace;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporalAnchorPower;
import Moonworks.relics.WarpPanel;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class LeapThroughTime extends AbstractNormaAttentiveCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(LeapThroughTime.class.getSimpleName());
    public static final String IMG = makeCardPath("LeapThroughSpaceMark.png"); //We actually use the one initially for marking, lol

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static String TALK_TEXT;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int INITIAL_PERCENT = LeapThroughSpace.DIVERGENCE;
    private static final int UPGRADE_PLUS_PERCENT = LeapThroughSpace.UPGRADE_PLUS_DIVERGENCE;

    // /STAT DECLARATION/


    // STORED INFORMATION

    public int currentHP;
    public int maxHP;
    public int currentEnergy;
    public int currentBlock;
    public ArrayList<AbstractCard> handCards = new ArrayList<>(),
                                         drawCards = new ArrayList<>(),
                                         discardCards = new ArrayList<>(),
                                         exhaustCards = new ArrayList<>();
    public HashMap<AbstractCard, UUID> masterUUIDMap = new HashMap<>();
    public ArrayList<AbstractPower> powerList = new ArrayList<>();
    public ArrayList<AbstractPotion> potionList = new ArrayList<>();
    public int maxOrbs;
    public ArrayList<AbstractOrb> orbList = new ArrayList<>();

    public AbstractCard cardToIgnore;
    public boolean previewOnly;

    private int divergenceStacks;
    private int divergenceTier;
    private boolean anchorExists;

    private boolean spawnedRelic = false;

    public ArrayList<CorruptedModifier.CorruptionEffects> tier0Effects = new ArrayList<>();
    public ArrayList<CorruptedModifier.CorruptionEffects> tier1Effects = new ArrayList<>();
    public ArrayList<CorruptedModifier.CorruptionEffects> tier2Effects = new ArrayList<>();
    public ArrayList<CorruptedModifier.CorruptionEffects> tier3Effects = new ArrayList<>();
    public ArrayList<ArrayList<CorruptedModifier.CorruptionEffects>> masterEffectList = new ArrayList<>();


    // /STORED INFORMATION/

    public LeapThroughTime() {
        //We call this one if we only want a preview of the card without the functionality
        this(null, false);
    }


    public LeapThroughTime(AbstractCard cardToIgnore, boolean previewOnly) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //AutoplayField.autoplay.set(this, true);
        this.purgeOnUse = true;
        this.previewOnly = previewOnly;
        this.magicNumber = this.baseMagicNumber = INITIAL_PERCENT;
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        setBackgroundTexture(OrangeJuiceMod.TEMP_SKILL_WHITE_ICE, OrangeJuiceMod.TEMP_SKILL_WHITE_ICE_PORTRAIT);

        if(AbstractDungeon.player != null && !previewOnly) {
            saveData(cardToIgnore);
        }
        defineArrayLists();
        initializeDescription();
    }

    public void defineArrayLists() {
        //Defines the Corruption effects possible for t0 through t3
        tier0Effects.add(CorruptedModifier.CorruptionEffects.BLANK); //Blank is twice as likely to happen
        tier0Effects.add(CorruptedModifier.CorruptionEffects.BLANK);
        tier0Effects.add(CorruptedModifier.CorruptionEffects.SNECKO);

        tier1Effects.add(CorruptedModifier.CorruptionEffects.ETHEREAL);
        tier1Effects.add(CorruptedModifier.CorruptionEffects.EXHAUST);
        tier1Effects.add(CorruptedModifier.CorruptionEffects.REDUCE);
        tier1Effects.add(CorruptedModifier.CorruptionEffects.VOIDING);
        tier1Effects.add(CorruptedModifier.CorruptionEffects.FRAGILE);

        tier2Effects.add(CorruptedModifier.CorruptionEffects.DEGRADE);
        tier2Effects.add(CorruptedModifier.CorruptionEffects.PURGING);
        tier2Effects.add(CorruptedModifier.CorruptionEffects.WITHER);
        tier2Effects.add(CorruptedModifier.CorruptionEffects.HIDEINFO);

        tier3Effects.add(CorruptedModifier.CorruptionEffects.CORRUPTDESC);
        tier3Effects.add(CorruptedModifier.CorruptionEffects.CORRUPTFULL);

        //Each tier can also roll the tier 1 lower
        tier3Effects.addAll(tier2Effects);
        tier2Effects.addAll(tier1Effects);
        tier1Effects.addAll(tier0Effects);

        //Add each tier to the masterEffectList
        masterEffectList.add(tier0Effects);
        masterEffectList.add(tier1Effects);
        masterEffectList.add(tier2Effects);
        masterEffectList.add(tier3Effects);
    }

    public void saveData(AbstractCard cardToIgnore) {
        logger.info("Leap Through Time created. Saving all data. If you get a crash here then it's my fault. Im so sorry.");
        //Time to save a shit load of information
        this.cardToIgnore = cardToIgnore;
        this.currentHP = AbstractDungeon.player.currentHealth;
        this.maxHP = AbstractDungeon.player.maxHealth;
        this.currentEnergy = EnergyPanel.totalCount;
        this.currentBlock = AbstractDungeon.player.currentBlock;

        //Loop through every card in your hand
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            //Dont store any copies of LTS and LTT
            if(!(c instanceof LeapThroughTime) && !(c instanceof LeapThroughSpace) && !(c == cardToIgnore)){
                //Make a copy of the card, since we dont want to store the original cards and have them be manipulated between now and when we reload them
                AbstractCard copy = c.makeStatEquivalentCopy();
                //Associate the new copy with the original card's UUID for masterDeck lookup
                this.masterUUIDMap.put(copy, c.uuid);
                //Store our copy in our own array to recovery when we rewind
                this.handCards.add(copy);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if(!(c instanceof LeapThroughTime) && !(c instanceof LeapThroughSpace) && !(c == cardToIgnore)){
                AbstractCard copy = c.makeStatEquivalentCopy();
                this.masterUUIDMap.put(copy, c.uuid);
                this.drawCards.add(copy);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if(!(c instanceof LeapThroughTime) && !(c instanceof LeapThroughSpace) && !(c == cardToIgnore)){
                AbstractCard copy = c.makeStatEquivalentCopy();
                this.masterUUIDMap.put(copy, c.uuid);
                this.discardCards.add(copy);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if(!(c instanceof LeapThroughTime) && !(c instanceof LeapThroughSpace) && !(c == cardToIgnore)){
                AbstractCard copy = c.makeStatEquivalentCopy();
                this.masterUUIDMap.put(copy, c.uuid);
                this.exhaustCards.add(copy);
            }
        }
        //We dont want to copy any Temporal Anchors. Not now at least. Maybe functionality for multiple anchors will come later
        //this.powerList.addAll(AbstractDungeon.player.powers);
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (!(p instanceof TemporalAnchorPower)) {
                /*try { //Reflection works here, but there is an easier way to do it
                    //Grab a copy if we can
                    this.powerList.add((AbstractPower) p.getClass().getMethod("makeCopy").invoke(p));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    //Failed to grab a copy, add it manually
                    this.powerList.add(p);
                }*/
                if (p instanceof CloneablePowerInterface) {
                    this.powerList.add(((CloneablePowerInterface)p).makeCopy());
                    //do stuff with it
                } else {
                    logger.info("Power class: ("+p.getClass()+") does not implement CloneablePowerInterface and could not be copied. This wasn't my fault :(");
                    logger.info("Power description: "+p);
                    this.powerList.add(p);
                }
            }
        }

        this.potionList.addAll(AbstractDungeon.player.potions);
        this.orbList.addAll(AbstractDungeon.player.orbs);
        this.maxOrbs = AbstractDungeon.player.maxOrbs;
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add("Special");
        return tags;
    }

    private static ArrayList<TooltipInfo> specialTooltip;
    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (specialTooltip == null)
        {
            specialTooltip = new ArrayList<>();
            specialTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Special"), BaseMod.getKeywordDescription("moonworks:Special")));
        }
        return specialTooltip;
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        this.invertedNumber = Math.min(this.invertedNumber+magicNumber, 100);
        this.isInvertedNumberModified = this.invertedNumber != this.baseInvertedNumber;
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        //----------This needs to be put in an action for goodness' sake---------//

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                //First, grab our values and clear all the data
                TemporalAnchorPower anchor = (TemporalAnchorPower) p.getPower(TemporalAnchorPower.POWER_ID);
                anchorExists = anchor != null; //Store this, as we can use it later for extra stuff if we lost our anchor somehow
                divergenceStacks = anchorExists ? anchor.amount2 : 100; //If we somehow lost our Anchor, the end result will be as bad as possible, even if we have WarpPanel
                divergenceTier =
                        divergenceStacks <= LeapThroughSpace.DIVERGENCET0LIMIT ? 0 :
                                divergenceStacks <= LeapThroughSpace.DIVERGENCET1LIMIT ? 1 :
                                        divergenceStacks <= LeapThroughSpace.DIVERGENCET2LIMIT ? 2 : 3;

                //Clear all the arraylists

                AbstractDungeon.player.powers.clear(); //Do this first and reload it last
                AbstractDungeon.player.hand.group.clear();
                AbstractDungeon.player.drawPile.group.clear();
                AbstractDungeon.player.discardPile.group.clear();
                AbstractDungeon.player.exhaustPile.group.clear();
                AbstractDungeon.player.potions.clear();
                AbstractDungeon.player.orbs.clear();

                this.isDone = true;
            }
        });

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                //Add some Talk Text for flavour
                TALK_TEXT = cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(0, 2)];
                this.addToBot(new TalkAction(true, TALK_TEXT, 4.0f, 2.0f));

                // Play some VFX/SFX as well
                CardCrawlGame.sound.playA("POWER_ENTANGLED", -0.65F);
                CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
                CardCrawlGame.sound.play("HEART_BEAT", 0.05F);
                AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
                AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
                this.addToBot(new VFXAction(p, new IntenseZoomEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, false), 0.25F));
                this.isDone = true;
            }
        });

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                //Reload everything. Also, mess stuff up here with high divergence, lol

                // At T2(T3), lose 1(2) to 2(8) max hp, to no less than 1
                AbstractDungeon.player.maxHealth = Math.max(1, maxHP - Math.max(0, divergenceTier -1) * AbstractDungeon.cardRandomRng.random(1, 4));
                // At T1 and up, lose 2-6 current hp times the tier, to no less than 1
                AbstractDungeon.player.currentHealth = Math.max(1, Math.min(currentHP - divergenceTier * AbstractDungeon.cardRandomRng.random(2, 6), AbstractDungeon.player.maxHealth));
                // Not currently messing with orb slots
                AbstractDungeon.player.maxOrbs = maxOrbs;
                // At T1 and up, lose 2-5 block times the tier, to no less than 0
                AbstractDungeon.player.currentBlock = Math.max(0, currentBlock - (AbstractDungeon.cardRandomRng.random(2, 5) * divergenceTier * AbstractDungeon.cardRandomRng.random(0, 1)));
                // Update the HP bar to the correct amount once weve changed the values
                AbstractDungeon.player.healthBarUpdatedEvent();
                // At T1 and up, lost up to your tier in energy
                EnergyPanel.totalCount = Math.max(0, currentEnergy - AbstractDungeon.cardRandomRng.random(0, divergenceTier));
                // Not currently messing with max energy, or even saving it

                //For each card in your hand, draw pile, etc.
                for (AbstractCard c : handCards) {
                    //Make sure we arent ignoring it, a bit redundant
                    if (c != cardToIgnore) {
                        //Randomly add a corrupted modifier, then add it to your actual hand, draw pile, etc.
                        randomlyAddCardModifier(c);
                        AbstractDungeon.player.hand.addToHand(c);
                    }
                }
                for (AbstractCard c : drawCards) {
                    if (c != cardToIgnore) {
                        randomlyAddCardModifier(c);
                        AbstractDungeon.player.drawPile.addToHand(c);
                    }
                }
                for (AbstractCard c : discardCards) {
                    if (c != cardToIgnore) {
                        randomlyAddCardModifier(c);
                        AbstractDungeon.player.discardPile.addToHand(c);
                    }
                }
                for (AbstractCard c : exhaustCards) {
                    if (c != cardToIgnore) {
                        randomlyAddCardModifier(c);
                        AbstractDungeon.player.exhaustPile.addToHand(c);
                    }
                }
                //Not currently messing with orbs
                AbstractDungeon.player.orbs.addAll(orbList);
                //Not currently messing with potions
                AbstractDungeon.player.potions.addAll(potionList);
                //Mess around with the powers randomly
                //AbstractDungeon.player.powers.addAll(this.powerList);
                //Load the powers last, we really dont want to proc any on X effects from the powers
                for (AbstractPower pow : powerList) {
                    randomlyMessUpPower(pow);
                    AbstractDungeon.player.powers.add(pow);
                }

                this.isDone = true;
            }
        });

        // If you were insane and allowed yourself to reach max divergence, there is a 25% chance you pick up a WarpPanel relic
        if (divergenceTier == 3 && !spawnedRelic && !AbstractDungeon.player.hasRelic(WarpPanel.ID) && AbstractDungeon.cardRandomRng.random(0, 3) == 0) {
            AbstractDungeon.getCurrRoom().addRelicToRewards(new WarpPanel());
            spawnedRelic = true;
        }
    }

    private void randomlyAddCardModifier(AbstractCard card) {
        //Apply it to our actual card we are putting back
        if (AbstractDungeon.cardRandomRng.random(0, (3 - this.divergenceTier)) == 0) {
            int index = AbstractDungeon.cardRandomRng.random(0, masterEffectList.get(this.divergenceTier).size()-1);
            CardModifierManager.addModifier(card, new CorruptedModifier(masterEffectList.get(this.divergenceTier).get(index)));

            //Bonus time, apply it to our master deck if we are at very high Divergence.
            if (this.divergenceTier >= 2 && AbstractDungeon.cardRandomRng.random(1, LeapThroughSpace.DIVERGENCEMAX) <= this.divergenceStacks) {
                //Check if it exists in master
                for (AbstractCard c :  AbstractDungeon.player.masterDeck.group) {
                    //Grab the original UUID that we associated with the copy and compare it to the master deck cards
                    if (c.uuid.equals(masterUUIDMap.get(card))) {
                        CardModifierManager.addModifier(c, new CorruptedModifier(masterEffectList.get(this.divergenceTier).get(index)));
                        break;
                    }
                }
            }
        }
    }

    private void randomlyMessUpPower(AbstractPower p) {
        if (this.divergenceTier >= 1 && AbstractDungeon.cardRandomRng.random(1, LeapThroughSpace.DIVERGENCEMAX) <= this.divergenceStacks) {
            if (p.type == AbstractPower.PowerType.BUFF) {
                p.amount = Math.min(0, p.amount * (int)(1 - 0.25F*this.divergenceTier));
            } else {
                p.amount *= (int)(1 + 0.25F*this.divergenceStacks);
            }
            p.updateDescription();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_PERCENT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        //We really really shouldnt be making copies of this card, or else all goes to heck
        return new LeapThroughTime(cardToIgnore, previewOnly);
    }
}