package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class SakisCookie extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(SakisCookie.class.getSimpleName());
    public static final String IMG = makeCardPath("SakisCookie.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int COOKIES = 3;
    private static final int UPGRADE_PLUS_COOKIES = 1;

    private static final int HEAL = 2;
    private static final int UPGRADE_PLUS_HEAL = 1;

    private static final int CHARGES = 1;
    private static final int UPGRADE_PLUS_CHARGES = 1;

    private static final Integer[] NORMA_LEVELS = {4};

    // /STAT DECLARATION/

    public SakisCookie() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        magicNumber = baseMagicNumber = HEAL;
        secondMagicNumber = baseSecondMagicNumber = CHARGES;
        exhaust = true;
        this.tags.add(CardTags.HEALING);
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 0, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //TODO add bonus to vigor/steady?
        this.addToBot(new HealAction(p, p, magicNumber, 0.1f));
        if (getNormaLevel() >= NORMA_LEVELS[0]) {
            this.addToBot(new DrawCardAction(1));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            this.upgradeSecondMagicNumber(UPGRADE_PLUS_CHARGES);
            this.initializeDescription();
        }
    }
}