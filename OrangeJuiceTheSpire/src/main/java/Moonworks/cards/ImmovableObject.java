package Moonworks.cards;

import Moonworks.cards.defaultcards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class ImmovableObject extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ImmovableObject.class.getSimpleName());
    public static final String IMG = makeCardPath("ImmovableObject.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int BLOCK = 30;
    private static final int UPGRADE_PLUS_BLOCK = 10;
    private static final int EFFECT = 2;

    // /STAT DECLARATION/

    public ImmovableObject() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = EFFECT;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
        this.addToBot(new ApplyPowerAction(p, p, new BlurPower(p, magicNumber)));
        this.addToBot(new ApplyPowerAction(p, p, new EntanglePower(p)));
        this.addToBot(new ApplyPowerAction(p, p, new EntanglePower(p)));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_PLUS_BLOCK);
            this.initializeDescription();
        }
    }
}