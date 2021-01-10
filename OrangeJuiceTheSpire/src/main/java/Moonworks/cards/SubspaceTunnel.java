package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ReplaceCardFromAnywhere;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class SubspaceTunnel extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(SubspaceTunnel.class.getSimpleName());
    public static final String IMG = makeCardPath("SubspaceTunnel.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    private static final int CARDS = 1;

    private int handIndex;
    public boolean success;
    public CardGroup targetGroup;

    // /STAT DECLARATION/


    public SubspaceTunnel() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = CARDS;
        //this.exhaust = true; //This shouldnt exhaust
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ReplaceCardFromAnywhere(this, handIndex));
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {
            return p.drawPile.group.size() > 0 || p.discardPile.group.size() > 0 || p.exhaustPile.group.size() > 0;
        }
    }

    @Override
    public void applyPowers() {
        this.handIndex = AbstractDungeon.player.hand.group.indexOf(this);
        super.applyPowers();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}