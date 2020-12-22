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
        super(id, img, cost, type, color, rarity, target);
        setBackgroundTexture(OrangeJuiceMod.TRAP_WHITE_ICE, OrangeJuiceMod.TRAP_WHITE_ICE_PORTRAIT);
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add("Trap");
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (TrapTooltip == null)
        {
            TrapTooltip = new ArrayList<>();
            TrapTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Trap"), BaseMod.getKeywordDescription("moonworks:Trap")));
        }
        return TrapTooltip;
    }
}
