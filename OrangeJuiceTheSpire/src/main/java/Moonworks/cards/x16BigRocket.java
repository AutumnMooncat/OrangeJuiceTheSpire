package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class x16BigRocket extends AbstractDynamicCard {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Moonworks.cards.x16BigRocket.class.getSimpleName());
    public static final String IMG = makeCardPath("x16BigRocket.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int DAMAGE_SCALE = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int UPGRADE_PLUS_DAMAGE_SCALE = 2;

    // /STAT DECLARATION/
    public x16BigRocket() {this(0);} //ignore

    public x16BigRocket(int upgrades) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_SCALE;
        //isMultiDamage = true; //Uses for each loop.
        while(timesUpgraded < upgrades) {
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_SCALE);
            this.timesUpgraded++;
            this.upgraded = true;
        }
        this.name = "x" + (timesUpgraded+1) + " " + languagePack.getCardStrings(ID).NAME;
        initializeTitle();
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        this.baseDamage += magicNumber;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
            //logger.info("Upgrading x16 Big Rocket...");
        upgradeDamage(UPGRADE_PLUS_DAMAGE);
        upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_SCALE);
            this.timesUpgraded++;
            this.upgraded = true;
            this.name = "x" + (timesUpgraded+1) + " " + languagePack.getCardStrings(ID).NAME;
            initializeTitle();
            initializeDescription();
    }
    public boolean canUpgrade() {
        //logger.info("x16 Big Rocket checking for upgrade viability...");
        return this.timesUpgraded < 15;
    }

    public AbstractCard makeCopy() {
        return new x16BigRocket(this.timesUpgraded);
    }
}