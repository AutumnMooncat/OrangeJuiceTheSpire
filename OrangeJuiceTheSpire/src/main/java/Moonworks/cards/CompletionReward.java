package Moonworks.cards;

import Moonworks.cards.defaultcards.AbstractNormaAttentiveCard;
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

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
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

    private static final int COST = 1;
    private int lastCount; //How many cards ad been played last time this card was played. Defaults to 0 since we haven't played the card yet.

    // /STAT DECLARATION/

    public CompletionReward() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //upgraded = true;
        lastCount = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - lastCount;//Damage is the number of cards played since the last time this card was played
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //logger.info("Use: last count: " + lastCount);
        int bonus = 0;
        switch (getNormaLevel()) {
            case 5:
            case 4:
            case 3:
            case 2: bonus += 3;
            case 1:
            default:
        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage-1+bonus, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if(upgraded) {
            lastCount = AbstractDungeon.actionManager.cardsPlayedThisCombat.size()/2;
        } else {
            lastCount = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        }
        this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - lastCount;
        //logger.info("Used: last count: " + lastCount);
        initializeDescription();
    }

    public void applyPowers() {
        //logger.info("Apply: last count: " + lastCount);
        this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - lastCount;
        super.applyPowers();
        initializeDescription();

    }

    public void calculateCardDamage(AbstractMonster m) {
        //logger.info("Calc: last count: " + lastCount);
        this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - lastCount;
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
            rawDescription = UPGRADE_DESCRIPTION;
            //this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.initializeDescription();
        }
    }
}