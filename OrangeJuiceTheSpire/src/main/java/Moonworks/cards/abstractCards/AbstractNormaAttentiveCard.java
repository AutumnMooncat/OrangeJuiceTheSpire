package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.powers.NormaPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractNormaAttentiveCard extends AbstractDynamicCard {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    public static final String upgradeGreen = "[#7fff00]"; //For text purposes, the upgrade green is [#7fff00] and downgrade red is [#efc851]
    public CardStrings cardStrings;
    public String DESCRIPTION; //The main description of the card
    public String UPGRADE_DESCRIPTION; //The upgrade description of the card, if applicable
    public String[] EXTENDED_DESCRIPTION; //The Norma Effects of the card

    protected static ArrayList<TooltipInfo> NormaTooltip;

    public ArrayList<Integer> normaLevels = new ArrayList<>();
    public HashMap<Integer, Boolean> normaChecks = new HashMap<>();
    public ArrayList<Integer> normaCheckBuffer = new ArrayList<>();
    public int lastXChecked, tempXChecked;

    public AbstractNormaAttentiveCard(final String id,
                                      final String img,
                                      final int cost,
                                      final CardType type,
                                      final CardColor color,
                                      final CardRarity rarity,
                                      final CardTarget target) {

        this(id, img, cost, type, color, rarity, target, null);
    }

    public AbstractNormaAttentiveCard(final String id,
                                      final String img,
                                      final int cost,
                                      final CardType type,
                                      final CardColor color,
                                      final CardRarity rarity,
                                      final CardTarget target,
                                      final Integer[] normaLevels) {

        super(id, img, cost, type, color, rarity, target);
        if (normaLevels != null) {
            this.normaLevels.addAll(Arrays.asList(normaLevels));
        }
        cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
        initializeDescription();
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        if (normaLevels.size() > 0) {
            tags.add(BaseMod.getKeywordTitle("moonworks:norma"));
        }
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (NormaTooltip == null)
        {
            NormaTooltip = new ArrayList<>();
            NormaTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:norma"), BaseMod.getKeywordDescription("moonworks:norma")));
        }
        return normaLevels.size() > 0 ? NormaTooltip : null;
    }

    public int getNormaLevel() {
        if(hasNormaPower()) {
            return getNormaPower().amount;
        }
        return 0;
    }

    public boolean hasNormaPower() {
        if (AbstractDungeon.player == null) return false;
        return AbstractDungeon.player.hasPower(NormaPower.POWER_ID);
    }

    public AbstractPower getNormaPower() {
        if(hasNormaPower()) {
            return AbstractDungeon.player.getPower(NormaPower.POWER_ID);
        }
        return null;
    }

    @Override
    public void applyPowers() {
        applyNormaEffects(); //Empty hook on applyPowers if wanted for things that the NormaDynvarModifier cant do
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void initializeDescription() {
        applyNormaDescriptions(); //Empty hook, if wanted. Should really use the INFOMOD option of NormaDynvarModifier, but if that wont work for whatever reason
        super.initializeDescription();
    }

    /**
     * Empty hook that calls prior to initDescription, if wanted for anything.
     * Should really use the INFOMOD option of NormaDynvarModifier, but if that wont work for whatever reason, there is this
     */
    public void applyNormaDescriptions(){} //Empty hook, if wanted

    /**
     * Empty hook that calls prior to applyPowers.
     * Most things can simply be done with a NormaDynvarModifier, but this is here if it is needed
     */
    public void applyNormaEffects(){} //Empty hook, if wanted
}