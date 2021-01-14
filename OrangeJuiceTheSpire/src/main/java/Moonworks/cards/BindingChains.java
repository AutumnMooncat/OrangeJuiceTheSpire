package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.powers.TemporaryStrengthPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.*;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class BindingChains extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(BindingChains.class.getSimpleName());
    public static final String IMG = makeCardPath("BindingChains.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String DECAYED_NAME = cardStrings.EXTENDED_DESCRIPTION[0];
    public static final String DECAYED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[1];
    public static final String DECAYED_USE = cardStrings.EXTENDED_DESCRIPTION[2];
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int SHACKLES = 8;
    private static final int UPGRADE_PLUS_SHACKLES = 4;
    private static final int DEGRADE = -4;

    private boolean decayed;

    // /STAT DECLARATION/


    public BindingChains() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = SHACKLES;
        this.decayed = false;
    }

    public BindingChains(boolean decayed) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = SHACKLES;
        this.decayed = decayed;
        if(decayed) {
            this.name = DECAYED_NAME;
            this.rawDescription = DECAYED_DESCRIPTION;
            this.magicNumber = 0;
            this.cost = -2;
            this.target = CardTarget.NONE;
            this.exhaust = true;
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(!decayed) {
            this.addToBot(new ApplyPowerAction(m, p, new TemporaryStrengthPower(m, -magicNumber)));
            for (AbstractMonster aM: AbstractDungeon.getMonsters().monsters)
            {
                if (aM != m) {
                    this.addToBot(new ApplyPowerAction(aM, p, new TemporaryStrengthPower(aM, -magicNumber/2)));
                }
            }
            degrade();
        }

    }

    private void degrade() {
        upgradeMagicNumber(DEGRADE);
        if(this.magicNumber <= 0) {
            this.name = DECAYED_NAME;
            this.rawDescription = DECAYED_DESCRIPTION;
            //this.cantUseMessage = DECAYED_USE;
            this.decayed = true;
            this.magicNumber = 0;
            this.cost = -2;
            this.target = CardTarget.NONE;
        }
        initializeDescription();
        if(decayed) {
            //this.isEthereal = true;
            this.exhaust = true;
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SHACKLES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BindingChains(decayed);
    }
}