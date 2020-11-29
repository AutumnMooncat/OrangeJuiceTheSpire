package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class BigMagnum extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(BigMagnum.class.getSimpleName());
    public static final String IMG = makeCardPath("BigMagnum.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int HPLOSS = 5;

    private static final int STR = 3;
    private static final int UPGRADE_PLUS_STR = 2;

    // /STAT DECLARATION/

    public BigMagnum() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = HPLOSS;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = STR;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, defaultSecondMagicNumber),defaultSecondMagicNumber));
        int vigor = 0;
        switch (getNormaLevel()) {
            case 5:
            case 4:
            case 3:
            case 2: vigor += 3;
            case 1: vigor += 2;
            default:
        }
        if (vigor > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, vigor)));
        }
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, defaultSecondMagicNumber),defaultSecondMagicNumber));

    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_STR);
            this.initializeDescription();
        }
    }
}