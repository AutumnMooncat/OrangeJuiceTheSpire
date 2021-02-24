package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;

import java.util.AbstractList;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class GoldenEgg extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(GoldenEgg.class.getSimpleName());
    public static final String IMG = makeCardPath("GoldenEgg.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int BASE_BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    private static final int CARDS = 1;

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/

    public GoldenEgg() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        //this.block = this.baseBlock = BLOCK;
        this.block = this.baseBlock = BASE_BLOCK;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Gain block.
        this.addToBot(new GainBlockAction(p, p, block));

        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                //Get a random attack
                AbstractCard attackCard = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.ATTACK);

                //Add it...
                if (getNormaLevel() >= NORMA_LEVELS[0]) {
                    //Directly to hand
                    this.addToBot(new MakeTempCardInHandAction(attackCard, CARDS));
                } else {
                    //Into the deck
                    this.addToBot(new MakeTempCardInDrawPileAction(attackCard, CARDS, true, true));
                }

                //End the action
                this.isDone = true;
            }
        });
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            //upgradeMagicNumber(UPGRADE_PLUS_BLOCKS);
            initializeDescription();
        }
    }
}
