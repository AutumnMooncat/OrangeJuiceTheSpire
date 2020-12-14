package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.relics.BrokenBomb;
import Moonworks.relics.GoldenDie;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Flamethrower extends AbstractDynamicCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Flamethrower.class.getSimpleName());
    public static final String IMG = makeCardPath("Flamethrower.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;
    private static final int EFFECT = 2;

    private static final int RETAINS = 2;
    private static final int UPGRADE_PLUS_RETAINS = 1;

    private static final int DAMAGE = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 1;

    // /STAT DECLARATION/


    public Flamethrower() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = EFFECT;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = RETAINS;
        this.damage = this.baseDamage = DAMAGE;
        this.selfRetain = true; //Let it retain N times?
        //this.selfRetain = true;
        this.dontTriggerOnUseCard = true;
        setBackgroundTexture(OrangeJuiceMod.GIFT_WHITE_ICE, OrangeJuiceMod.GIFT_WHITE_ICE_PORTRAIT);
        //this.exhaust = true;
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add("Gift");
        return tags;
    }
    private static ArrayList<TooltipInfo> GiftTooltip;
    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (GiftTooltip == null)
        {
            GiftTooltip = new ArrayList<>();
            GiftTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Gift"), BaseMod.getKeywordDescription("moonworks:Gift")));
        }
        return GiftTooltip;
    }

    @Override
    public void triggerWhenDrawn() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean goldenDie = AbstractDungeon.player.hasRelic(GoldenDie.ID);
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = RETAINS + (goldenDie ? 1 : 0);
        //logger.info("Flamethrower Drawn: " + defaultSecondMagicNumber);
        this.selfRetain = true;
        rawDescription = DESCRIPTION;
        this.dontTriggerOnUseCard = false;
        //this.addToBot(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        initializeDescription();
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if(!dontTriggerOnUseCard) {
            AbstractPlayer p = AbstractDungeon.player;
            if(m != null) {
                this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
            } else {
                this.addToBot(new DamageRandomEnemyAction(new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    @Override
    public void onRetained() {
        AbstractPlayer p = AbstractDungeon.player;
        //logger.info("Flamethrower Retained. Count: " + this.defaultSecondMagicNumber);
        this.defaultSecondMagicNumber--; this.defaultBaseSecondMagicNumber--;
        //logger.info("Flamethrower End Count: " + this.defaultSecondMagicNumber);
        if(this.defaultSecondMagicNumber <= 0){
            this.selfRetain = false;
            //logger.info("Flamethrower Retain Lost");
            rawDescription = SPENT_DESCRIPTION;
        } else {
            this.selfRetain = true;
            //logger.info("Flamethrower Retained");
            rawDescription = DESCRIPTION;
        }
        this.dontTriggerOnUseCard = false;

        //this.addToBot(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));

        initializeDescription();
    }

    @Override
    public void applyPowers() {}
    @Override
    public void calculateCardDamage(AbstractMonster m) {}

    @Override
    public void onMoveToDiscard() {
        this.dontTriggerOnUseCard = true;
        super.onMoveToDiscard();
    }

    @Override
    public void triggerOnExhaust() {
        this.dontTriggerOnUseCard = true;
        super.triggerOnExhaust();
    }
    /* //Allow the player to use the card to discard it.
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }*/
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_RETAINS);
            initializeDescription();
        }
    }
}
