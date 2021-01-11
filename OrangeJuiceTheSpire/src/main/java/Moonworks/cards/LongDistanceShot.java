package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class LongDistanceShot extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(LongDistanceShot.class.getSimpleName());
    public static final String IMG = makeCardPath("LongDistanceShot.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String COPY_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DRAW = 1;
    private static final int DRAWS = 2;
    private static final int UPGRADE_PLUS_DRAWS = 1;

    private static final Integer[] NORMA_LEVELS = {3};

    public boolean copy;
    //public boolean normaAutoPlay;
    //public boolean oldAutoPlayState;

    // /STAT DECLARATION/


    public LongDistanceShot() {
        this(false);
    }

    public LongDistanceShot(boolean copy) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, copy ? null : NORMA_LEVELS);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DRAW;
        secondMagicNumber = baseSecondMagicNumber = DRAWS;
        this.copy = copy;
        if(copy) {
            this.isEthereal = true;
            this.exhaust = true;
            this.rawDescription = COPY_DESCRIPTION;
            initializeDescription();
        } else {
            CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 0, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[1]));
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        if (getNormaLevel() >= NORMA_LEVELS[0] && p.hand.size() < BaseMod.MAX_HAND_SIZE && !isEthereal && secondMagicNumber > 0) {
            this.addToBot(new DrawCardAction(magicNumber));
            secondMagicNumber = Math.max(0, secondMagicNumber - 1);
            this.isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
        }
        /*if (normaAutoPlay) {
            AutoplayField.autoplay.set(this, oldAutoPlayState);
            normaAutoPlay = false;
        }*/
        /*if (getNormaLevel() >= NORMA_LEVELS[0] && p.hand.size() < BaseMod.MAX_HAND_SIZE && !isEthereal) {
            AbstractCard lds = new LongDistanceShot(true);
            if (upgraded) {
                lds.upgrade();
            }
            //this.addToBot(new MakeTempCardInHandAction(lds));
            //lds.current_x = -1000.0F * Settings.scale;
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(lds));
        }*/
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeSecondMagicNumber(UPGRADE_PLUS_DRAWS);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LongDistanceShot(copy);
    }
}