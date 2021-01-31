package Moonworks.cards.magicalCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.patches.FixedPatches;
import Moonworks.powers.NormaPower;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MagicalInferno extends AbstractMagicalCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MagicalInferno.class.getSimpleName());
    public static final String IMG = makeCardPath("MagicalInferno.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int VIGOR = 5;
    private static final int UPGRADE_PLUS_VIGOR = 2;

    private static final Integer[] NORMA_LEVELS = {-1};

    // /STAT DECLARATION/


    public MagicalInferno() {
        this(0);
    }

    public MagicalInferno(int normaLevel) {
        this(normaLevel, normaLevel);
    }

    public MagicalInferno(int currentCharges, int maxCharges) {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET, currentCharges, maxCharges);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = VIGOR;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Deal fixed damage to all enemies
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                int index = AbstractDungeon.getMonsters().monsters.indexOf(aM);
                this.addToBot(new DamageAction(aM, new DamageInfo(p, multiDamage[index], damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true));
                for (AbstractMonster aM2: AbstractDungeon.getMonsters().monsters) {
                    if (aM2 != aM && !aM2.isDeadOrEscaped()) {
                        this.addToBot(new DamageAction(aM2, new DamageInfo(p, multiDamage[index]/2, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true));
                    }
                }
            }
        }

        for (AbstractPower pow : p.powers) {
            pow.onDamageAllEnemies(multiDamage);
        }

        //Gain Vigor
        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));

        //Regain 1 Norma
        this.addToBot(new ApplyPowerAction(p, p, new NormaPower(p, NORMA_RECHARGE)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_VIGOR);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicalInferno(ExhaustiveField.ExhaustiveFields.exhaustive.get(this), ExhaustiveField.ExhaustiveFields.baseExhaustive.get(this));
    }
}