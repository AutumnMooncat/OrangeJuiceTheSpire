package Moonworks.cards.magicalCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MagicalRevenge extends AbstractMagicalCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MagicalRevenge.class.getSimpleName());
    public static final String IMG = makeCardPath("MagicalRevenge.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int HEAL_PERCENT = 10;
    private static final int UPGRADE_PLUS_HEAL_PERCENT = 5;

    private static final int DAMAGE_PERCENT = 20;
    private static final int UPGRADE_PLUS_DAMAGE_PERCENT = 10;

    private static final Integer[] NORMA_LEVELS = {-1};

    // /STAT DECLARATION/


    public MagicalRevenge() {
        super(ID, IMG, TYPE, COLOR, TARGET);
        magicNumber = baseMagicNumber = DAMAGE_PERCENT;
        secondMagicNumber = baseSecondMagicNumber = HEAL_PERCENT;

        /*if (AbstractDungeon.player != null) {
            magicNumber = Math.max(0, baseMagicNumber - getNormaLevel());
            secondMagicNumber = Math.max(0, baseSecondMagicNumber - getNormaLevel());
            isMagicNumberModified = magicNumber != baseMagicNumber;
            isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
        }

        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, -3, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
        */
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void calculateCardDamage(AbstractMonster mo) {}

    @Override
    public void applyPowers() {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_PERCENT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_HEAL_PERCENT);

            /*if (AbstractDungeon.player != null) {
                magicNumber = Math.max(0, baseMagicNumber - getNormaLevel());
                secondMagicNumber = Math.max(0, baseSecondMagicNumber - getNormaLevel());
                isMagicNumberModified = magicNumber != baseMagicNumber;
                isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
            }*/

            initializeDescription();
        }
    }
}