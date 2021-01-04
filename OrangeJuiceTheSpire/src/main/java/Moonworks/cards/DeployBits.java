package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class DeployBits extends AbstractNormaAttentiveCard {

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
    private static final int UPGRADE_PLUS_STACKS = 4;

    private static final Integer[] NORMA_LEVELS = {4};

    // /STAT DECLARATION/


    public DeployBits() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        //this.block = this.baseBlock = BLOCK;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = STACKS;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bits = defaultSecondMagicNumber;
        if (getNormaLevel() >= NORMA_LEVELS[0]) {
            //bits -= 3;
            int dmgSum = 0, dmg;
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped() && isAttacking(aM)) {
                    dmg = (int) ReflectionHacks.getPrivate(aM, AbstractMonster.class, "intentDmg");
                    if ((boolean)ReflectionHacks.getPrivate(aM, AbstractMonster.class, "isMultiDmg"))
                    {
                        dmg *= (int)ReflectionHacks.getPrivate(aM, AbstractMonster.class, "intentMultiAmt");
                    }
                    dmgSum += Math.max(0, dmg);
                }
            }
            dmgSum = Math.max(0, dmgSum - p.currentBlock);
            this.block = Math.min(dmgSum, bits);
        } else {
            this.block = AbstractDungeon.cardRandomRng.random(1, bits);
        }

        this.magicNumber = bits - this.block;

        if(block > 0) {
            this.addToBot(new GainBlockAction(p, p, block));
        }

        if(magicNumber > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
        }

    }

    //Hopefully this works. Custom Intents that do damage might not work though.
    private boolean isAttacking(AbstractMonster m) {
        AbstractMonster.Intent i = m.intent;
        //Maybe this will work? This is how Spot Weakness does it
        return m.getIntentBaseDmg() >= 0;
        //return (i == AbstractMonster.Intent.ATTACK || i == AbstractMonster.Intent.ATTACK_BUFF || i == AbstractMonster.Intent.ATTACK_DEBUFF || i == AbstractMonster.Intent.ATTACK_DEFEND);
    }


    @Override
    public void applyNormaEffects() {
        modifySecondMagic(-3, NORMA_LEVELS[0]);
        super.applyNormaEffects();
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
