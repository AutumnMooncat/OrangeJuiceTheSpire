package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.magicalCards.MagicalInferno;
import Moonworks.cards.magicalCards.MagicalMassacre;
import Moonworks.cards.magicalCards.MagicalRevenge;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.MeltingMemoriesPower;
import Moonworks.powers.NormaGainPower;
import Moonworks.powers.NormaPower;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MeltingMemories extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MeltingMemories.class.getSimpleName());
    public static final String IMG = makeCardPath("MeltingMemories.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 3;

    // /STAT DECLARATION/


    public MeltingMemories() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new MeltingMemoriesAction(upgraded));

        //Get our Norma power, if it exists
        AbstractPower normaPower = p.getPower(NormaPower.POWER_ID);

        //Get our norma level, or 0 if we dont have the power
        int normaLevels = normaPower != null ? normaPower.amount : 0;

        //If our norma is greater than 0...
        if (normaLevels > 0) {
            //Stack the negative amount to reduce Norma to 0
            this.addToBot(new ApplyPowerAction(p, p, new NormaPower(p, -normaLevels)));

            //Add the power to restore our lost Norma
            this.addToBot(new ApplyPowerAction(p, p, new NormaGainPower(p, normaLevels)));

            //Add the power that gives us the Magical cards
            this.addToBot(new ApplyPowerAction(p, p, new MeltingMemoriesPower(p, normaLevels, upgraded)));
        }

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
