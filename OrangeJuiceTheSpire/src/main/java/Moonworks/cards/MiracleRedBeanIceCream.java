package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
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
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, magicNumber), magicNumber));
        }
    }


    @Override
    public void triggerOnExhaust() {
        if(active) {
            AbstractPlayer p = AbstractDungeon.player;
            this.addToBot(new HealAction(p, p, this.heal));
        }
        super.triggerOnExhaust();
    }

    @Override
    public void onRetained() {
        super.onRetained();
        if(active) {
            AbstractPlayer p = AbstractDungeon.player;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, magicNumber), magicNumber));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.defaultSecondMagicNumber = 0;
        super.use(p, m);
        this.exhaust = true;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MiracleRedBeanIceCream(defaultSecondMagicNumber, checkedGolden);
    }
}
