package Moonworks.cards;

import Moonworks.cards.defaultcards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class LulusLuckyEgg extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(LulusLuckyEgg.class.getSimpleName());
    public static final String IMG = makeCardPath("LulusLuckyEgg.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int BASE_BLOCK = 5;

    private static final int BONUS_EFFECT = 2;
    private static final int UPGRADE_PLUS_BONUS_EFFECT = 1;

    // /STAT DECLARATION/


    public LulusLuckyEgg() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //this.block = this.baseBlock = BLOCK;
        this.block = this.baseBlock = BASE_BLOCK;
        this.magicNumber = this.baseMagicNumber = BONUS_EFFECT;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.block = this.baseBlock; //Reset the block since it could have been modified from case 2. This also removes dex and other debufs :(
        int effect = AbstractDungeon.cardRandomRng.random(1, 3); //Choose a random number for the effect
        switch (effect) { //Switches allow us to run code based on the value in the switch.
            case 1: //Big Block. We multiply by 2 or 3 here
                this.block *= magicNumber;
                break; //Break after each case since we dont want it to then look at the other cases
            case 2: //Draw 2 or 3 cards, as this is stored in magicNumber
                this.addToBot(new DrawCardAction(magicNumber));
                break;
            case 3: //Buff 2 or 3 stacks of temp dex
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
                this.addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, magicNumber), magicNumber));
                break;
        }
        this.addToBot(new GainBlockAction(p, p, block)); //Do our block action. If case 2 happened, this will be higher than the default 5
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_EFFECT);
            initializeDescription();
        }
    }
}
