package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Dinner extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Dinner.class.getSimpleName());
    public static final String IMG = makeCardPath("Dinner.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int UPGRADE_REDUCED_COST = 0;

    private static final int REGEN = 3;
    private static final int UPGRADE_PLUS_REGEN = 1;

    // /STAT DECLARATION/

    public Dinner() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = REGEN;
        exhaust = true;
        this.tags.add(CardTags.HEALING);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new RegenPower(p, magicNumber)));
        for(AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if(!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new RegenPower(aM, magicNumber)));
            }
        }

    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            //this.upgradeBaseCost(UPGRADE_REDUCED_COST);
            this.upgradeMagicNumber(UPGRADE_PLUS_REGEN);
            this.initializeDescription();
        }
    }
}