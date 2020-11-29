package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.UpgradeRushAction;
import Moonworks.cards.defaultcards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class JonathanRush extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(JonathanRush.class.getSimpleName());
    public static final String IMG = makeCardPath("JonathanRush.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BONUS_DAMAGE = 2;
    private static final int UPGRADE_PLUS_BONUS_DAMAGE = 1;

    // /STAT DECLARATION/


    public JonathanRush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        AutoplayField.autoplay.set(this, true);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BONUS_DAMAGE;
        //this.isInAutoplay = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageRandomEnemyAction(new DamageInfo(p, damage, damageTypeForTurn),
                damage > 15 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY :AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToBot(new UpgradeRushAction(this, magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_DAMAGE);
            //upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}