package Moonworks.cards;

import Moonworks.actions.CookingTimeAction;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class CookingTime extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(CookingTime.class.getSimpleName());
    public static final String IMG = makeCardPath("CookingTime.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int HEAL = 3;
    private static final int UPGRADE_PLUS_HEAL = 2;

    private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/

    public CookingTime() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        magicNumber = baseMagicNumber = HEAL;
        this.tags.add(CardTags.HEALING);
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CookingTimeAction(p, m, magicNumber, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void applyNormaEffects() {
        modifyMagicNumber(3, NORMA_LEVELS[0]);
        super.applyNormaEffects();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            this.initializeDescription();
        }
    }
}