package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class UnluckyCharm extends AbstractDynamicCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(UnluckyCharm.class.getSimpleName());
    public static final String IMG = makeCardPath("UnluckyCharm.png");

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
    private static final int EFFECT = -1;
    private static final int POSITIVE_EFFECT = 2;
    private static final int RETAINS = 2;
    private static final int UPGRADE_PLUS_RETAINS = -1;

    // /STAT DECLARATION/


    public UnluckyCharm() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = EFFECT;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = RETAINS;
        this.selfRetain = true; //Let it retain N times?
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
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, POSITIVE_EFFECT),POSITIVE_EFFECT));
    }

    @Override
    public void triggerWhenDrawn() {
        AbstractPlayer p = AbstractDungeon.player;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber;
        //logger.info("Drawn Red And Blue. Count: "+defaultSecondMagicNumber);
        this.selfRetain = true;
        rawDescription = DESCRIPTION;
        initializeDescription();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber),magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseDexterityPower(p, magicNumber),magicNumber));


    }

    @Override
    public void onRetained() {
        //logger.info("RB Retained. Count: " + this.defaultSecondMagicNumber);
        AbstractPlayer p = AbstractDungeon.player;
        this.defaultSecondMagicNumber--; this.defaultBaseSecondMagicNumber--;
        //logger.info("RB End Count: " + this.defaultSecondMagicNumber);
        if(this.defaultSecondMagicNumber <= 0){
            this.selfRetain = false;
            this.isEthereal = true;
            //logger.info("RB Retain Lost.");
            rawDescription = SPENT_DESCRIPTION;
        } else {
            this.selfRetain = true;
            //logger.info("RB Retained");
            rawDescription = DESCRIPTION;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber),magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseDexterityPower(p, magicNumber),magicNumber));
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_RETAINS);
            initializeDescription();
        }
    }
}
