package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.defaultcards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.Heat300PercentPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;
//@AutoAdd.Ignore
public class Heat300Percent extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Heat300Percent.class.getSimpleName());
    public static final String IMG = makeCardPath("Heat300Percent.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int UPGRADE_REDUCED_COST = 0;
    private static final int STACKS = 1;

    // /STAT DECLARATION/


    public Heat300Percent() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = STACKS;
        setBackgroundTexture(OrangeJuiceMod.TRAP_WHITE_ICE, OrangeJuiceMod.TRAP_WHITE_ICE_PORTRAIT);
        //this.retain = true;
        //this.exhaust = true;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bonus = 0;
        switch (getNormaLevel()) {
            case 5:
            case 4:
            case 3:
            case 2: bonus += 1;
            case 1:
            default:
        }
        this.addToBot(new ApplyPowerAction(m, p, new Heat300PercentPower(m, this.magicNumber+bonus)));
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add("Trap");
        return tags;
    }
    private static ArrayList<TooltipInfo> TrapTooltip;
    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (TrapTooltip == null)
        {
            TrapTooltip = new ArrayList<>();
            TrapTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Trap"), BaseMod.getKeywordDescription("moonworks:Trap")));
        }
        return TrapTooltip;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_REDUCED_COST);
            initializeDescription();
        }
    }
}
