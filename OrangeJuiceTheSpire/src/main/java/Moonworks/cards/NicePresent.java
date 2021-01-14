package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ModifyCostThisCombatAction;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class NicePresent extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(NicePresent.class.getSimpleName());
    public static final String IMG = makeCardPath("NicePresent.png");

    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAWS = 1;

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/

    public NicePresent() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.exhaust = true;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> oldCards = new ArrayList<>();
        ArrayList<AbstractCard> newCards = new ArrayList<>();
        boolean normaCheck = getNormaLevel() >= NORMA_LEVELS[0];

        //Grab all of our cards
        if (normaCheck) {
            this.addToBot(new AbstractGameAction() {
                public void update() {
                    oldCards.addAll(p.hand.group);
                    this.isDone = true;
                }});
        }

        //Draw cards
        this.addToBot(new DrawCardAction(p, this.magicNumber));

        //Add temp retain to our new cards
        if (normaCheck) {
            this.addToBot(new AbstractGameAction() {
                public void update() {
                    newCards.addAll(p.hand.group);
                    for (AbstractCard c : newCards) {
                        if (!oldCards.contains(c)) {
                            c.retain = true;
                        }
                    }
                    this.isDone = true;
                }});
        }

        initializeDescription();

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_DRAWS);
            this.initializeDescription();
        }
    }
}