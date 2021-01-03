package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTempCard extends AbstractNormaAttentiveCard {

    private static ArrayList<TooltipInfo> specialTooltip;

    public AbstractTempCard(String id, String img, int cost, CardType type, CardColor color, CardTarget target) {
        this(id, img, cost, type, color, target, null);
    }

    public AbstractTempCard(String id, String img, int cost, CardType type, CardColor color, CardTarget target, Integer[] normaLevels) {
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
        tags.add(BaseMod.getKeywordTitle("moonworks:Special"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (specialTooltip == null)
        {
            specialTooltip = new ArrayList<>();
            specialTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Special"), BaseMod.getKeywordDescription("moonworks:Special")));
        }
        specialTooltip.addAll(super.getCustomTooltipsTop());
        return specialTooltip;
    }
}