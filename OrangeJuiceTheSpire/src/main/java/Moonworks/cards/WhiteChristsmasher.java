package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.cards.giftCards.RedAndBlue;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class WhiteChristsmasher extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(WhiteChristsmasher.class.getSimpleName());
    public static final String IMG = makeCardPath("WhiteChristsmasher.png");
    public static final String IMG2 = makeCardPath("TrueWhiteChristsmasher.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    private static final Integer[] NORMA_LEVELS = {2};

    private final String NAME;
    private float fontSize = 17F;
    private boolean flashed;


    // /STAT DECLARATION/


    public WhiteChristsmasher() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        NAME = this.name;
        //CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.DAMAGEMOD, 2, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[1]));

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (hasRedAndBlue()) {
            //Deal damage to everyone twice
            this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
            this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        } else {
            //Deal damage twice
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }

    }

    @Override
    public void applyPowers() {
        changeCardEffect();
        super.applyPowers();
    }

    public void calculateCardDamage(AbstractMonster m) {
        changeCardEffect();
        super.calculateCardDamage(m);
    }

    @Override
    public float getTitleFontSize() {
        return fontSize;
    }

    private void changeCardEffect() {
        if (hasRedAndBlue()) {
            this.target = CardTarget.ALL_ENEMY;
            this.isMultiDamage = true;
            this.textureImg = IMG2;
            this.loadCardImage(IMG2);
            this.rawDescription = EXTENDED_DESCRIPTION[1];
            this.name = EXTENDED_DESCRIPTION[0];
            if (!flashed) {
                flash();
                flashed = true;
            }
            //this.fontSize = 16F;
        } else {
            this.target = CardTarget.ENEMY;
            this.isMultiDamage = false;
            this.textureImg = IMG;
            this.loadCardImage(IMG);
            this.rawDescription = DESCRIPTION;
            this.name = NAME;
            flashed = false;
            //this.fontSize = 22F;
        }
    }

    private boolean hasRedAndBlue() {
        //Check every card in the hand
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            //IF we have a Red and Blue that hasnt been exhausted
            if (c instanceof RedAndBlue && ((RedAndBlue) c).secondMagicNumber > 0) {
                //Then return true
                return true;
            }
        }
        return false;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            //upgradeMagicNumber(UPGRADE_PLUS_TEMP_STR);
            initializeDescription();
        }
    }
}
