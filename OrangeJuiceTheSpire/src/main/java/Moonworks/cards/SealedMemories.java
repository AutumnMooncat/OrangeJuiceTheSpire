package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ConvertMemoryAction;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.MeltingMemoriesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class SealedMemories extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(SealedMemories.class.getSimpleName());
    public static final String IMG = makeCardPath("SealedMemories.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private final static int UPGRADE_REDUCED_COST = 0;
    private static final int CARDS = 1;

    // /STAT DECLARATION/


    public SealedMemories() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = CARDS;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Convert a card to a memory
        this.addToBot(new ConvertMemoryAction(CARDS, false, true, true));

        //Old effect
        //Apply Blur so we retain our block
        //this.addToBot(new ApplyPowerAction(p, p, new BlurPower(p, this.magicNumber)));

        //Apply Equilibrium so we retain our hand
        this.addToBot(new ApplyPowerAction(p, p, new EquilibriumPower(p, this.magicNumber)));


        //Old effect
        /*
        this.addToBot(new ApplyPowerAction(p, p, new EnergizedBluePower(p, EnergyPanel.totalCount-this.cost)));
        this.addToBot(new PressEndTurnButtonAction());*/
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //this.isInnate = true;
            this.selfRetain = true;
            rawDescription = UPGRADE_DESCRIPTION;
            //upgradeBaseCost(UPGRADE_REDUCED_COST);
            initializeDescription();
        }
    }
}
