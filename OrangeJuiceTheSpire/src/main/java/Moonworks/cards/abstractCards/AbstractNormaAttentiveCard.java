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
            tags.add(BaseMod.getKeywordTitle("moonworks:Norma"));
        }
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (NormaTooltip == null)
        {
            NormaTooltip = new ArrayList<>();
            NormaTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Norma"), BaseMod.getKeywordDescription("moonworks:Norma")));
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
        super.applyPowers();
        applyNormaEffects();
        initializeDescription();
    }

    @Override
    public void initializeDescription() {
        applyNormaDescriptions();
        super.initializeDescription();
    }

    public void applyNormaDescriptions(){
        StringBuilder sb = new StringBuilder();
        boolean passedCheck, normaX;
        sb.append(upgraded && cardStrings.UPGRADE_DESCRIPTION != null ? UPGRADE_DESCRIPTION : DESCRIPTION);
        if(normaLevels != null && normaLevels.size() > 0) {
            for (int i = 0 ; i < normaLevels.size() ; i++) {
                normaX = normaLevels.get(i) == -1;
                passedCheck = getNormaLevel() >= (normaX ? 1 : normaLevels.get(i)); //Could also use absolute value here, but thats less intuitive to read
                sb.append(" NL ");
                sb.append(passedCheck ? upgradeGreen : "*");
                sb.append(BaseMod.getKeywordTitle("moonworks:Norma")).append(" ");
                sb.append(passedCheck ? upgradeGreen : "*");
                sb.append(normaX ? "X" : normaLevels.get(i));
                sb.append(": ");
                sb.append(EXTENDED_DESCRIPTION[i]);
            }
        }
        rawDescription = sb.toString();
    }

    public void applyNormaEffects(){
        updateApplicationChecks();
    }

    public void modifyDamage(int amount, int normaCheck) {
        boolean normaX = normaCheck == -1;
        if (normaX) { // handle normaX stuff here
            amount = getNormaLevel() - lastXChecked;
            tempXChecked = getNormaLevel();
        }
        if (((normaX && amount > 0) || !normaChecks.getOrDefault(normaCheck, false)) && getNormaLevel() >= normaCheck){
            damage = Math.max(0, damage + amount);
            baseDamage = Math.max(0, baseDamage + amount);
            isDamageModified = damage != baseDamage;
            normaCheckBuffer.add(normaCheck);
        }
    }
    public void modifyBlock(int amount, int normaCheck) {
        boolean normaX = normaCheck == -1;
        if (normaX) { // handle normaX stuff here
            amount = getNormaLevel() - lastXChecked;
            tempXChecked = getNormaLevel();
        }
        if (((normaX && amount > 0) || !normaChecks.getOrDefault(normaCheck, false)) && getNormaLevel() >= normaCheck){
            block = Math.max(0, block + amount);
            baseBlock = Math.max(0, baseBlock + amount);
            isBlockModified = block != baseBlock;
            normaCheckBuffer.add(normaCheck);
        }
    }
    public void modifyMagicNumber(int amount, int normaCheck) {
        boolean normaX = normaCheck == -1;
        if (normaX) { // handle normaX stuff here
            amount = getNormaLevel() - lastXChecked;
            tempXChecked = getNormaLevel();
        }
        if (((normaX && amount > 0) || !normaChecks.getOrDefault(normaCheck, false)) && getNormaLevel() >= normaCheck){
            magicNumber = Math.max(0, magicNumber + amount);
            baseMagicNumber = Math.max(0, baseMagicNumber + amount);
            isMagicNumberModified = magicNumber != baseMagicNumber;
            normaCheckBuffer.add(normaCheck);
        }
    }
    public void modifySecondMagic(int amount, int normaCheck) {
        boolean normaX = normaCheck == -1;
        if (normaX) { // handle normaX stuff here
            amount = getNormaLevel() - lastXChecked;
            tempXChecked = getNormaLevel();
        }
        if (((normaX && amount > 0) || !normaChecks.getOrDefault(normaCheck, false)) && getNormaLevel() >= normaCheck){
            defaultSecondMagicNumber = Math.max(0, defaultSecondMagicNumber + amount);
            defaultBaseSecondMagicNumber = Math.max(0, defaultBaseSecondMagicNumber + amount);
            isDefaultSecondMagicNumberModified = defaultSecondMagicNumber != defaultBaseSecondMagicNumber;
            normaCheckBuffer.add(normaCheck);
        }
    }
    public void modifyInvertedNumber(int amount, int normaCheck) {
        boolean normaX = normaCheck == -1;
        if (normaX) { // handle normaX stuff here
            amount = getNormaLevel() - lastXChecked;
            tempXChecked = getNormaLevel();
        }
        if (((normaX && amount > 0) || !normaChecks.getOrDefault(normaCheck, false)) && getNormaLevel() >= normaCheck){
            invertedNumber = Math.max(0, invertedNumber + amount);
            baseInvertedNumber = Math.max(0, baseInvertedNumber + amount);
            isInvertedNumberModified = invertedNumber != baseInvertedNumber;
            normaCheckBuffer.add(normaCheck);
        }
    }

    private void updateApplicationChecks() {
        for (Integer i : normaCheckBuffer) {
            normaChecks.put(i, true);
        }
        lastXChecked = tempXChecked;
        normaCheckBuffer.clear();
    }
}