package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class CompletionReward extends AbstractNormaAttentiveCard {
    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(CompletionReward.class.getSimpleName());
    public static final String IMG = makeCardPath("CompletionReward.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private int lastCount; //How many cards ad been played last time this card was played. Defaults to 0 since we haven't played the card yet.

    private static final Integer[] NORMA_LEVELS = {2};
    private static final int NORMA_BOOST = 6;

    // /STAT DECLARATION/

    public CompletionReward() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        //upgraded = true;
        //lastCount = AbstractDungeon.actionManager.cardsPlayedThisCombat.size(); //Buff: Card wont start at 0 damage if generated in combat
        this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();//Damage is the number of cards played since the last time this card was played
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //logger.info("Use: last count: " + lastCount);
        //We have to -1 or else it counts the damage from this card being played as well
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage-1, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() + (getNormaLevel() >= NORMA_LEVELS[0] ? NORMA_BOOST : 0);
        //logger.info("Used: last count: " + lastCount);
        initializeDescription();
    }

    public void applyPowers() {
        //logger.info("Apply: last count: " + lastCount);
        this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() + (getNormaLevel() >= NORMA_LEVELS[0] ? NORMA_BOOST : 0);
        super.applyPowers();
        initializeDescription();

    }

    public void calculateCardDamage(AbstractMonster m) {
        //logger.info("Calc: last count: " + lastCount);
        this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() + (getNormaLevel() >= NORMA_LEVELS[0] ? NORMA_BOOST : 0);
        super.calculateCardDamage(m);
        initializeDescription();

    }

    public void onMoveToDiscard() {
        //this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeBaseCost(UPGRADE_COST);
            //this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.initializeDescription();
        }
    }
}