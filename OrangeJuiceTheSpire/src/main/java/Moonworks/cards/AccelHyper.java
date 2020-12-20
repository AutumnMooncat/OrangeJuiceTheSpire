package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.BurstPower;
import com.megacrit.cardcrawl.powers.DoubleTapPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class AccelHyper extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(AccelHyper.class.getSimpleName());
    public static final String IMG = makeCardPath("AccelHyper.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    // /STAT DECLARATION/


    public AccelHyper() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.magicNumber = this.baseMagicNumber = COST;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DoubleTapPower(p, magicNumber), magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BurstPower(p, magicNumber), magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}