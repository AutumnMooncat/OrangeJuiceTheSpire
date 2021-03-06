package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.cards.interfaces.RevengeAttack;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.Heat300PercentPower;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class ChefSomeHelp extends AbstractNormaAttentiveCard implements RevengeAttack {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ChefSomeHelp.class.getSimpleName());
    public static final String IMG = makeCardPath("ChefICouldUseSomeHelp.png");
    public static final String IMG2 = makeCardPath("ManagerICouldUseSomeHelp.png");

    private final String NAME;
    private boolean changed;
    private float fontSize = 20F;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DAMAGE = 4;

    private static final int HITS_NEEDED = 3;
    private int timesDamaged;

    //private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/

    public ChefSomeHelp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.NAME = this.name;
        this.damage = this.baseDamage = DAMAGE;
        this.secondMagicNumber = this.baseSecondMagicNumber = HITS_NEEDED;
        //CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        if (changed) {
            changeCardEffect();
            timesDamaged = 0;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (changed) {
            damage *= 2;
            isDamageModified = damage != baseDamage;
        }
    }

    @Override
    public void tookDamage() {
        if(!changed) {
            timesDamaged++;
            if (timesDamaged >= HITS_NEEDED) {
                changeCardEffect();
            }
        }
    }

    private void changeCardEffect() {
        if (!changed) {
            //this.target = CardTarget.ALL_ENEMY;
            //this.isMultiDamage = true;
            this.textureImg = IMG2;
            this.loadCardImage(IMG2);
            this.rawDescription = EXTENDED_DESCRIPTION[1];
            this.name = EXTENDED_DESCRIPTION[0];
            flash();
            changed = true;
            //this.fontSize = 16F;
        } else {
            //this.target = CardTarget.ENEMY;
            //this.isMultiDamage = false;
            this.textureImg = IMG;
            this.loadCardImage(IMG);
            this.rawDescription = DESCRIPTION;
            this.name = NAME;
            flash();
            changed = false;
            //this.fontSize = 22F;
        }
    }

    @Override
    public float getTitleFontSize() {
        return fontSize;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            //this.upgradeSecondMagicNumber(UPGRADE_PLUS_HEAT);
            this.initializeDescription();
        }
    }
}