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

    // /STAT DECLARATION/


    public DeployBits() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //this.block = this.baseBlock = BLOCK;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = STACKS;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (getNormaLevel() >= 4) {
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
            dmgSum -= p.currentBlock;
            this.block = Math.min(dmgSum, defaultSecondMagicNumber);
        } else {
            this.block = AbstractDungeon.cardRandomRng.random(1, defaultSecondMagicNumber);
        }

        this.magicNumber = defaultSecondMagicNumber - this.block;

        if(block > 0) {
            this.addToBot(new GainBlockAction(p, p, block));
        }

        if(magicNumber > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
        }

    }

    //Hopefully this works. Custom Intents that do damage will absolutely not work though.
    private boolean isAttacking(AbstractMonster m) {
        AbstractMonster.Intent i = m.intent;
        return (i == AbstractMonster.Intent.ATTACK || i == AbstractMonster.Intent.ATTACK_BUFF ||
                i == AbstractMonster.Intent.ATTACK_DEBUFF || i == AbstractMonster.Intent.ATTACK_DEFEND);
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
