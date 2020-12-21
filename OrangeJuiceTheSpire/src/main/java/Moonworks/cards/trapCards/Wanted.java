package Moonworks.cards.trapCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.WantedPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;
//@AutoAdd.Ignore
public class Wanted extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Wanted.class.getSimpleName());
    public static final String IMG = makeCardPath("Wanted.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1; //Cost 0 for testing
    private static final int UPGRADE_REDUCED_COST = 0;
    private static final int STACKS = 1;

    // /STAT DECLARATION/


    public Wanted() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = STACKS;
        this.exhaust = true; //This has to exhaust to not be super OP. It isnt exhausting currently for testing
        setBackgroundTexture(OrangeJuiceMod.TRAP_WHITE_ICE, OrangeJuiceMod.TRAP_WHITE_ICE_PORTRAIT);

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
            //TrapTooltip.add(new TooltipInfo("Trap", "This card's effect ignores Artifact."));
            TrapTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Trap"), BaseMod.getKeywordDescription("moonworks:Trap")));
        }
        return TrapTooltip;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new WantedPower(m, this.magicNumber)));
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
