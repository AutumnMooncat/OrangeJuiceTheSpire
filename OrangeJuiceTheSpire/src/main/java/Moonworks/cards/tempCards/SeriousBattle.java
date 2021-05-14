package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.BookOfMemoriesPower;
import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;
@AutoAdd.Ignore
public class SeriousBattle extends AbstractTempCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(SeriousBattle.class.getSimpleName());
    public static final String IMG = makeCardPath("SeriousBattle.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int CARDS = 1;
    private static final int UPGRADE_PLUS_CARDS = 1;


    // /STAT DECLARATION/


    public SeriousBattle() {
        super(ID, IMG, COST, TYPE, COLOR, TARGET);
        this.purgeOnUse = false;
        this.magicNumber = this.baseMagicNumber = CARDS;
        //setBackgroundTexture(OrangeJuiceMod.MAGIC_SKILL_WHITE_ICE, OrangeJuiceMod.MAGIC_SKILL_WHITE_ICE_PORTRAIT);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Remaining functionality completely provided in BookOfMemoriesPower
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CARDS);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}