package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ExtendedPhotonRifleAction;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class ExtendedPhotonRifle extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ExtendedPhotonRifle.class.getSimpleName());
    public static final String IMG = makeCardPath("ExtendedPhotonRifle.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -1;
    private static final int DAMAGE = 0;

    private static final int HITS = 1;
    //private static final int UPGRADE_PLUS_HITS = 4; //We need to either use 4X(5X) Hits without Str Multi

    private static final int STRENGTH_MULTI = 3; //Or X Hits with 4(5) Str Multi
    private static final int UPGRADE_PLUS_STRENGTH_MULTI = 1;

    // /STAT DECLARATION/


    public ExtendedPhotonRifle() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = HITS;
        secondMagicNumber = baseSecondMagicNumber = STRENGTH_MULTI;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }

    @Override
    public float getTitleFontSize() {
        return 20F;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ExtendedPhotonRifleAction(p, m, new DamageInfo(p, damage, damageTypeForTurn), magicNumber, this.freeToPlayOnce, this.energyOnUse));
    }

    public void applyPowers() {
        AbstractPower strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
        //AbstractPower vigor = AbstractDungeon.player.getPower(VigorPower.POWER_ID);
        //int vigorAmount = 0;
        if (strength != null) {
            strength.amount *= this.secondMagicNumber;
        }
        /*if (vigor != null) {
            vigorAmount = vigor.amount;
            vigor.amount = 0;
        }*/

        super.applyPowers();

        if (strength != null) {
            strength.amount /= this.secondMagicNumber;
        }
        /*if (vigor != null) {
            vigor.amount = vigorAmount;
        }*/

    }

    public void calculateCardDamage(AbstractMonster m) {
        AbstractPower strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
        AbstractPower vigor = AbstractDungeon.player.getPower(VigorPower.POWER_ID);
        int vigorAmount = 0;
        if (strength != null) {
            strength.amount *= this.secondMagicNumber;
        }
        if (vigor != null) {
            vigorAmount = vigor.amount;
            vigor.amount = 0;
        }

        super.calculateCardDamage(m);

        if (strength != null) {
            strength.amount /= this.secondMagicNumber;
        }
        if (vigor != null) {
            vigor.amount = vigorAmount;
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_HITS);
            upgradeSecondMagicNumber(UPGRADE_PLUS_STRENGTH_MULTI);
            //rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
