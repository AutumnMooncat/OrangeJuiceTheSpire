package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporaryStrengthPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MiracleRedBeanIceCream extends AbstractGiftCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MiracleRedBeanIceCream.class.getSimpleName());
    public static final String IMG = makeCardPath("MiracleRedBeanIceCream.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;
    private static final int TEMP_STR = 2;
    private static final int HEAL = 5;

    private static final int USES = 2;
    private static final int UPGRADE_PLUS_USES = 1;
    // /STAT DECLARATION/


    public MiracleRedBeanIceCream() {

        this(USES, false);

    }
    public MiracleRedBeanIceCream(int currentUses, boolean checkedGolden) {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        this.magicNumber = this.baseMagicNumber = TEMP_STR;
        this.heal = this.baseHeal = HEAL;
        this.tags.add(CardTags.HEALING);

    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if(active) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToBot(new ApplyPowerAction(p, p, new TemporaryStrengthPower(p, magicNumber)));
            this.applyEffect();
        }
    }


    @Override
    public void triggerOnExhaust() {
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new HealAction(p, p, HEAL));
        super.triggerOnExhaust();
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if(active) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToBot(new ApplyPowerAction(p, p, new TemporaryStrengthPower(p, magicNumber)));
            this.applyEffect();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.secondMagicNumber = 0;
        this.exhaust = true;
        this.active = false; //Card cant be active when it has no uses
        this.rawDescription = this.spentDescription;
        super.use(p, m); //This will handle our update description
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
        return new MiracleRedBeanIceCream(secondMagicNumber, checkedGolden);
    }
}
