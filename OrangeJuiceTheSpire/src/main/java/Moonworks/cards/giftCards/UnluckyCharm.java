package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporaryDexterityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class UnluckyCharm extends AbstractGiftCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(UnluckyCharm.class.getSimpleName());
    public static final String IMG = makeCardPath("UnluckyCharm.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;

    private static final int DEBUFF_STACKS = 1;

    private static final int USES = 3;
    private static final int UPGRADE_PLUS_USES = 1;

    // /STAT DECLARATION/


    public UnluckyCharm() {
        this(USES, false);
    }
    public UnluckyCharm(int currentUses, boolean checkedGolden) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden, true);
        this.magicNumber = this.baseMagicNumber = DEBUFF_STACKS;
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber),magicNumber));
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (isActive(true)) {
            applyDebuffs();
        }
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if(isActive()) {
            applyDebuffs();
        }
    }

    private void applyDebuffs() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
        m = AbstractDungeon.getRandomMonster();
        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false)));
        m = AbstractDungeon.getRandomMonster();
        this.addToBot(new ApplyPowerAction(m, p, new FrailPower(m, magicNumber, false)));
        this.applyEffect();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_USES);
            //upgradeMagicNumber(UPGRADE_PLUS_POSITIVE_EFFECT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnluckyCharm(secondMagicNumber, checkedGolden);
    }
}
