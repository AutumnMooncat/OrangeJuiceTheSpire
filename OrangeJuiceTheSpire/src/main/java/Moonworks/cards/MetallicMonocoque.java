package Moonworks.cards;

import Moonworks.actions.UpdateSecondValueAction;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.powers.MetallicMonocoquePower;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MetallicMonocoque extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MetallicMonocoque.class.getSimpleName());
    public static final String IMG = makeCardPath("MetallicMonocoque.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    //private static final int UPGRADE_COST = 2;
    private static final int DAMAGE_REDUCE = 1;
    private static final int UPGRADE_PLUS_DAMAGE_REDUCE = 1;
    private static final int THORNS_REDUCE = 2;
    private static final int UPGRADE_PLUS_THORNS_REDUCE = 1;

    // /STAT DECLARATION/


    public MetallicMonocoque() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = DAMAGE_REDUCE;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = THORNS_REDUCE;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean updateSecondValue = p.hasPower(MetallicMonocoquePower.POWER_ID);
        this.addToBot(new ApplyPowerAction(p, p, new MetallicMonocoquePower(p, magicNumber, defaultSecondMagicNumber)));
        if (updateSecondValue) {
            this.addToBot(new UpdateSecondValueAction(p, p, (TwoAmountPower)p.getPower(MetallicMonocoquePower.POWER_ID), defaultSecondMagicNumber));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_THORNS_REDUCE);
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_REDUCE);
            //upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
