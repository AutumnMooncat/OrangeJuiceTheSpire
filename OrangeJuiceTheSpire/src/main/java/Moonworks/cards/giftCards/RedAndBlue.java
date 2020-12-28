package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.SteadyPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class RedAndBlue extends AbstractGiftCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(RedAndBlue.class.getSimpleName());
    public static final String IMG = makeCardPath("RedAndBlue.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;
    private static final int EFFECT = 4;
    private static final int UPGRADE_PLUS_EFFECT = 2;
    private static final int USES = 3;

    //private static final int UPGRADE_PLUS_RETAINS = 1;

    // /STAT DECLARATION/


    public RedAndBlue() {

        this(USES, false);
        //this.exhaust = true;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }
    public RedAndBlue(int currentUses, boolean checkedGolden) {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        this.magicNumber = this.baseMagicNumber = EFFECT;

    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if(active){
            AbstractPlayer p = AbstractDungeon.player;
            this.addToBot(new ApplyPowerAction(p, p, new SteadyPower(p, magicNumber)));
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
            this.applyEffect();
        }
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if(active) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToBot(new ApplyPowerAction(p, p, new SteadyPower(p, magicNumber)));
            this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
            this.applyEffect();
        }
    }


    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RedAndBlue(defaultSecondMagicNumber, checkedGolden);
    }
}
