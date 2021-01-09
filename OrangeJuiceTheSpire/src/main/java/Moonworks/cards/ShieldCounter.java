package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.powers.Heat300PercentPower;
import Moonworks.powers.ShieldCounterPower;
import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class ShieldCounter extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ShieldCounter.class.getSimpleName());
    public static final String IMG = makeCardPath("ShieldCounter.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int BLOCK = 15;
    private static final int UPGRADE_PLUS_BLOCK = 5;
    private static final int TURNS = 1;

    private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/


    public ShieldCounter() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        baseBlock = block = BLOCK;
        this.exhaust = true;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
        this.addToBot(new ApplyPowerAction(p, p, new ShieldCounterPower(p, TURNS, getNormaLevel() >= NORMA_LEVELS[0]))); //Retain if we pass the check
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}