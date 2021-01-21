package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMagicalCard extends AbstractNormaAttentiveCard {

    private static ArrayList<TooltipInfo> MagicalTooltip;

    public AbstractMagicalCard(String id, String img, int cost, CardType type, CardColor color, CardTarget target) {
        this(id, img, cost, type, color, target, null);
    }

    public AbstractMagicalCard(String id, String img, int cost, CardType type, CardColor color, CardTarget target, Integer[] normaLevels) {
        super(id, img, cost, type, color, CardRarity.SPECIAL, target, normaLevels);
        this.purgeOnUse = true;
        if (type == CardType.ATTACK) {
            setBackgroundTexture(OrangeJuiceMod.TEMP_ATTACK_WHITE_ICE, OrangeJuiceMod.TEMP_ATTACK_WHITE_ICE_PORTRAIT);
        } else { //We dont support temp powers because oops
            setBackgroundTexture(OrangeJuiceMod.TEMP_SKILL_WHITE_ICE, OrangeJuiceMod.TEMP_SKILL_WHITE_ICE_PORTRAIT);
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

    //Inverse the Norma effects here
    public void flashNormaColor(boolean increase){
        if (increase) {
            this.flash(new Color(-16776961)); //COLOR.RED
        } else {
            this.flash(new Color(16711935)); //COLOR.GREEN

        }
    }
}