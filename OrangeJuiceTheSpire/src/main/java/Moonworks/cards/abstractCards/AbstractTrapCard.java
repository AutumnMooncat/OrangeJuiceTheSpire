package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTrapCard extends AbstractNormaAttentiveCard {

    private static ArrayList<TooltipInfo> TrapTooltip;

    public AbstractTrapCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, null);
    }

    public AbstractTrapCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, Integer[] normaLevels) {
        super(id, img, cost, type, color, rarity, target, normaLevels);
        setBackgroundTexture(OrangeJuiceMod.TRAP_WHITE_ICE, OrangeJuiceMod.TRAP_WHITE_ICE_PORTRAIT);
    }

    @Override
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(BaseMod.getKeywordTitle("moonworks:Trap"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (TrapTooltip == null)
        {
            TrapTooltip = new ArrayList<>();
            TrapTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Trap"), BaseMod.getKeywordDescription("moonworks:Trap")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(TrapTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }
}
