package Moonworks.cards.trapCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractTrapCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.Heat300PercentPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;
//@AutoAdd.Ignore
public class Heat300Percent extends AbstractTrapCard {

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

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/


    public Heat300Percent() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        this.magicNumber = this.baseMagicNumber = STACKS;
        //this.retain = true;
        //this.exhaust = true;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new Heat300PercentPower(m, this.magicNumber)));
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
