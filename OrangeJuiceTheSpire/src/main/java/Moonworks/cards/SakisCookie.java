package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.powers.TemporaryDexterityPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

    private static final int HEAL = 2;
    private static final int UPGRADE_PLUS_HEAL = 2;

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/

    public SakisCookie() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        magicNumber = baseMagicNumber = HEAL;
        exhaust = true;
        this.tags.add(CardTags.HEALING);
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, 2, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = AbstractDungeon.cardRandomRng.random(1, 3); //Choose a random number for the effect
        switch (effect) { //Switches allow us to run code based on the value in the switch.
            case 1: //Big Block. We multiply by 2 or 3 here
                this.block *= magicNumber;
                break; //Break after each case since we dont want it to then look at the other cases
            case 2: //Draw 2 or 3 cards, as this is stored in magicNumber
                this.addToBot(new DrawCardAction(magicNumber));
                break;
            case 3: //Buff 2 or 3 stacks of temp dex
                this.addToBot(new ApplyPowerAction(p, p, new TemporaryDexterityPower(p, magicNumber)));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
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