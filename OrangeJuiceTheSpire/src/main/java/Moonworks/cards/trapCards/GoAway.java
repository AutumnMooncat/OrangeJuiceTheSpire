package Moonworks.cards.trapCards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractTrapCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class GoAway extends AbstractTrapCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(GoAway.class.getSimpleName());
    public static final String IMG = makeCardPath("GoAway.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 3;
    //private static final int UPGRADE_COST = 0;

    private static final int STUN = 1;

    // /STAT DECLARATION/

    public GoAway() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STUN;
        exhaust = true;
        isEthereal = true;
        //isInnate = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        StunMonsterPower stun = new StunMonsterPower(m, magicNumber);
        stun.type = AbstractPower.PowerType.BUFF; //Hows that for a hack?
        this.addToBot(new ApplyPowerAction(m, p, stun, magicNumber));

    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            this.isEthereal = false;
            //this.upgradeBaseCost(UPGRADE_COST);
            this.initializeDescription();
        }
    }
}