package Moonworks.cards.uniqueCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporaryDexterityPower;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class LulusLuckiestEgg extends AbstractDynamicCard implements UniqueCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(LulusLuckiestEgg.class.getSimpleName());
    public static final String IMG = makeCardPath("LulusLuckyEgg.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int BASE_BLOCK = 5;

    private static final int BONUS_EFFECT = 2;
    private static final int UPGRADE_PLUS_BONUS_EFFECT = 1;

    // /STAT DECLARATION/


    public LulusLuckiestEgg() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = BASE_BLOCK;
        this.magicNumber = this.baseMagicNumber = BONUS_EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Gain multi-blocks
        for (int i = 0 ; i < magicNumber ; i++) {
            this.addToBot(new GainBlockAction(p, p, block));
        }
        //Draw
        this.addToBot(new DrawCardAction(magicNumber));
        //Temp Dex
        this.addToBot(new ApplyPowerAction(p, p, new TemporaryDexterityPower(p, magicNumber)));
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_EFFECT);
            initializeDescription();
        }
    }

}
