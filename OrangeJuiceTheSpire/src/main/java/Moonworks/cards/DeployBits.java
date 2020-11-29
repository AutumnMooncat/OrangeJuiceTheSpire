package Moonworks.cards;

import Moonworks.cards.defaultcards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class DeployBits extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(DeployBits.class.getSimpleName());
    public static final String IMG = makeCardPath("DeployBits.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    //private static final int BLOCK = 7;
    //private static final int UPGRADE_PLUS_BLOCK = 3;

    private static final int STACKS = 10;
    private static final int UPGRADE_PLUS_STACKS = 6;

    // /STAT DECLARATION/


    public DeployBits() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //this.block = this.baseBlock = BLOCK;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = STACKS;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.block = AbstractDungeon.cardRandomRng.random(1, defaultSecondMagicNumber);
        this.magicNumber = defaultSecondMagicNumber - this.block;

        if(block > 0) {
            this.addToBot(new GainBlockAction(p, p, block));
        }

        if(magicNumber > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
        }

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_STACKS);
            initializeDescription();
        }
    }
}
