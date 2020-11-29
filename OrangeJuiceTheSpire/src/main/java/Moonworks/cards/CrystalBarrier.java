package Moonworks.cards;

import Moonworks.cards.defaultcards.AbstractDynamicCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.*;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class CrystalBarrier extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(CrystalBarrier.class.getSimpleName());
    public static final String IMG = makeCardPath("CrystalBarrier.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 3;

    private static final int BARRIER = 1;
    private static final int UPGRADE_PLUS_BARRIER = 1;
    private static final int PLATED = 4;
    private static final int UPGRADE_PLUS_PLATED = 2;

    // /STAT DECLARATION/


    public CrystalBarrier() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = PLATED;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new BarricadePower(p)));
        this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber)));
        //this.addToBot(new ObtainPotionAction(new OneHundredPercentOrangeJuicePotion()));
        //this.addToBot(new ApplyPowerAction(p, p, new BufferPower(p, magicNumber)));
    }
    private static ArrayList<TooltipInfo> platedTooltip;
    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (platedTooltip == null)
        {
            platedTooltip = new ArrayList<>();
            platedTooltip.add(new TooltipInfo(PlatedArmorPower.NAME, PlatedArmorPower.DESCRIPTIONS[0].substring(0, PlatedArmorPower.DESCRIPTIONS[0].length()-3) + PlatedArmorPower.DESCRIPTIONS[1]));
        }
        return platedTooltip;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_PLATED);
            //rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
