package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.MeltingMemoriesPower;
import Moonworks.powers.NormaGainPower;
import Moonworks.powers.NormaPower;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MeltingMemories extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MeltingMemories.class.getSimpleName());
    public static final String IMG = makeCardPath("MeltingMemories.png");

    private static ArrayList<TooltipInfo> ConversionTooltip;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int AMOUNT = 1;

    // /STAT DECLARATION/


    public MeltingMemories() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = AMOUNT;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (ConversionTooltip == null)
        {
            ConversionTooltip = new ArrayList<>();
            ConversionTooltip.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(ConversionTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new MeltingMemoriesAction(upgraded));

        //Get our Norma power, if it exists
        AbstractPower normaPower = p.getPower(NormaPower.POWER_ID);

        //Get our norma level, or 0 if we don't have the power
        int normaLevels = normaPower != null ? normaPower.amount : 0;

        //If our norma is greater than 0...
        if (normaLevels > 0) {
            //Stack the negative amount to reduce Norma to 0
            this.addToBot(new ApplyPowerAction(p, p, new NormaPower(p, -normaLevels)));

            //Add the power to restore our lost Norma
            this.addToBot(new ApplyPowerAction(p, p, new NormaGainPower(p, normaLevels)));
        }

        //Get the power to see if we already have it
        AbstractPower pow = p.getPower(MeltingMemoriesPower.POWER_ID);

        //If we have it already, we might need to update if it should upgrade cards, since this wont update in StackPower
        if (upgraded && pow instanceof MeltingMemoriesPower) {
            ((MeltingMemoriesPower) pow).setUpgrade(true);
        }

        //Apply the power
        this.addToBot(new ApplyPowerAction(p, p, new MeltingMemoriesPower(p, magicNumber, upgraded)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
