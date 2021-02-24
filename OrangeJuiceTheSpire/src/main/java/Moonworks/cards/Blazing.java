package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.powers.BlazingPower;
import basemod.helpers.BaseModCardTags;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Blazing extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Blazing.class.getSimpleName());
    public static final String IMG = makeCardPath("Blazing.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 3;

    private static final int BASE_BLAST = 6;
    private static final int UPGRADE_PLUS_BASE_BLAST = 3;

    private static final int BLAST_SCALE = 3;
    private static final int UPGRADE_PLUS_BLAST_SCALE = 3;

    //private static final int BAD_DEX = 1;

    //private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/

    //TODO balance
    public Blazing() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = BASE_BLAST;
        this.secondMagicNumber = this.baseSecondMagicNumber = BLAST_SCALE;
        //this.invertedNumber = this.baseInvertedNumber = BAD_DEX;
        this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.
        //CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INVERTEDMOD, -1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(BlazingPower.POWER_ID)) {
            ((BlazingPower)p.getPower(BlazingPower.POWER_ID)).updateScaling(secondMagicNumber);
        }
        this.addToBot(new ApplyPowerAction(p ,p , new BlazingPower(p, magicNumber, secondMagicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            //upgradeMagicNumber(UPGRADE_PLUS_BASE_BLAST);
            upgradeSecondMagicNumber(UPGRADE_PLUS_BLAST_SCALE);
            initializeDescription();
        }
    }
}
