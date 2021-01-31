package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.patches.TypeOverridePatch;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMagicalCard extends AbstractNormaAttentiveCard {

    private static ArrayList<TooltipInfo> MagicalTooltip;
    private static final int MAGICAL_COST = 0; //Always 0 cost
    protected static final int NORMA_RECHARGE = 1;
    public CardRarity displayRarity;

    public AbstractMagicalCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target, int currentCharges, int maxCharges) {
        this(id, img, type, color, rarity, target, currentCharges, maxCharges, null);
    }

    public AbstractMagicalCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target, int currentCharges, int maxCharges, Integer[] normaLevels) {
        super(id, img, MAGICAL_COST, type, color, CardRarity.SPECIAL, target, normaLevels);
        this.displayRarity = rarity;
        this.setDisplayBannerRarity(displayRarity);
        //this.purgeOnUse = true;
        if (type == CardType.ATTACK) {
            setBackgroundTexture(OrangeJuiceMod.MAGIC_ATTACK_WHITE_ICE, OrangeJuiceMod.MAGIC_ATTACK_WHITE_ICE_PORTRAIT);
        } else if (type != CardType.POWER) { //Power is handled automatically, we just expressly want to do nothing
            setBackgroundTexture(OrangeJuiceMod.MAGIC_SKILL_WHITE_ICE, OrangeJuiceMod.MAGIC_SKILL_WHITE_ICE_PORTRAIT);
        }
        TypeOverridePatch.TypeOverrideField.typeOverride.set(this, BaseMod.getKeywordTitle("moonworks:magical"));
        this.glowColor = Color.PURPLE.cpy();

        ExhaustiveVariable.setBaseValue(this, maxCharges);
        ExhaustiveField.ExhaustiveFields.exhaustive.set(this, currentCharges);
        initializeDescription();
    }

    /*
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(BaseMod.getKeywordTitle("moonworks:magical"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }*/

    /*public String getBaseDescriptor() {
        return BaseMod.getKeywordTitle("moonworks:magical");
    }*/

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (MagicalTooltip == null)
        {
            MagicalTooltip = new ArrayList<>();
            MagicalTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:magical"), BaseMod.getKeywordDescription("moonworks:magical")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(MagicalTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }
}