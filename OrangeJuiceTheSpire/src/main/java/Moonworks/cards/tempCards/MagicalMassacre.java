package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.SteadyPower;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MagicalMassacre extends AbstractMagicalCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MagicalMassacre.class.getSimpleName());
    public static final String IMG = makeCardPath("MagicalMassacre.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int PERCENT_DAMAGE = 8;
    private static final int UPGRADE_PLUS_PERCENT_DAMAGE = 2;

    private static final int STEADY = 6;
    private static final int UPGRADE_PLUS_STEADY = 2;

    private static final Integer[] NORMA_LEVELS = {-1};
    // /STAT DECLARATION/


    public MagicalMassacre() {
        super(ID, IMG, COST, TYPE, COLOR, TARGET);
        magicNumber = baseMagicNumber = PERCENT_DAMAGE;
        secondMagicNumber = baseSecondMagicNumber = STEADY;
        damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
        //this.setDisplayRarity(CardRarity.RARE);
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, -1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.SECONDMAGICMOD, -1, NORMA_LEVELS[0], null));

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int dmg = (int)(m.maxHealth * magicNumber/100f);
        this.addToBot(new DamageAction(m, new DamageInfo(p, dmg, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new ApplyPowerAction(p, p, new SteadyPower(p, secondMagicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_PERCENT_DAMAGE);
            upgradeSecondMagicNumber(UPGRADE_PLUS_STEADY);
            initializeDescription();
        }
    }
}