package Moonworks.cards;

import Moonworks.actions.ApplyAndUpdateMemoriesAction;
import Moonworks.actions.FindAndReplaceCardAction;
import Moonworks.actions.RemoveAndUpdateMemoriesAction;
import Moonworks.cardModifiers.MemoryModifier;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.powers.interfaces.AssociateableInterface;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class DevilHand extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(DevilHand.class.getSimpleName());
    public static final String IMG = makeCardPath("DevilHand.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/


    public DevilHand() {
        this(true);
    }
    public DevilHand(final boolean setPreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.selfRetain = true;
        if (setPreview) {
            this.cardsToPreview = new AngelHand(false);
        }
    }

    @Override
    public void onRetained() {
        super.onRetained();
        //int index = AbstractDungeon.player.hand.group.indexOf(this);
        //this.addToBot(new TransformCardInHandAction(index, cardsToPreview.makeStatEquivalentCopy()));
        AbstractCard newCard = cardsToPreview.makeStatEquivalentCopy();
        OrangeJuiceMod.logger.info(CardModifierManager.modifiers(this));
        if (CardModifierManager.hasModifier(this, MemoryModifier.ID)) {
            this.addToBot(new ApplyAndUpdateMemoriesAction(newCard));
            this.addToBot(new RemoveAndUpdateMemoriesAction(this));
        }
        this.addToBot(new FindAndReplaceCardAction(this, newCard));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if(cardsToPreview != null) {
                cardsToPreview.upgrade();
            }
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}