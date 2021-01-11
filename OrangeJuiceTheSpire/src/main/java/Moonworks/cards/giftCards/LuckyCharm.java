package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class LuckyCharm extends AbstractGiftCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(LuckyCharm.class.getSimpleName());
    public static final String IMG = makeCardPath("LuckyCharm.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;
    private static final int ENERGY = 1;

    private static final int USES = 3;
    private static final int UPGRADE_PLUS_USES = 1;

    private static final int MAX_RECHARGE = 3; //You cant recharge more than this at once


    // /STAT DECLARATION/


    public LuckyCharm() {

        this(USES, false);
    }

    public LuckyCharm(int currentUses, boolean checkedGolden) {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        this.magicNumber = this.baseMagicNumber = ENERGY;
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if(active) {
            this.addToBot(new GainEnergyAction(magicNumber));
            this.applyEffect();
        }
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if (active) {
            this.addToBot(new GainEnergyAction(magicNumber));
            this.applyEffect();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int energy = Math.min(MAX_RECHARGE, EnergyPanel.totalCount);
        if (energy > 0) {
            this.modifyUses(energy);
            p.energy.use(energy);
        }
        super.use(p, m);
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LuckyCharm(secondMagicNumber, checkedGolden);
    }
}
