package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMagicalCard extends AbstractNormaAttentiveCard {

    private static ArrayList<TooltipInfo> MagicalTooltip;
    private static final int MAGICAL_COST = -2; //Ensure the card doesnt ave an energy icon
    public CardRarity displayRarity;

    public AbstractMagicalCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, type, color, rarity, target, null);
    }

    public AbstractMagicalCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target, Integer[] normaLevels) {
        super(id, img, MAGICAL_COST, type, color, CardRarity.SPECIAL, target, normaLevels);
        this.displayRarity = rarity;
        this.setDisplayBannerRarity(displayRarity);
        this.purgeOnUse = true;
        if (type == CardType.ATTACK) {
            setBackgroundTexture(OrangeJuiceMod.MAGIC_ATTACK_WHITE_ICE, OrangeJuiceMod.MAGIC_ATTACK_WHITE_ICE_PORTRAIT);
        } else if (type != CardType.POWER) { //Power is handled automatically, we just expressly want to do nothing
            setBackgroundTexture(OrangeJuiceMod.MAGIC_SKILL_WHITE_ICE, OrangeJuiceMod.MAGIC_SKILL_WHITE_ICE_PORTRAIT);
        }
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(BaseMod.getKeywordTitle("moonworks:magical"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }

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

    /*
    //Inverse the Norma effects here
    public void flashNormaColor(boolean increase){
        if (increase) {
            this.flash(new Color(-16776961)); //COLOR.RED
        } else {
            this.flash(new Color(16711935)); //COLOR.GREEN

        }
    }*/
}