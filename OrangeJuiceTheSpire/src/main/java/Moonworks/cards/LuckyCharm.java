package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.relics.BrokenBomb;
import Moonworks.relics.GoldenDie;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.*;
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

public class LuckyCharm extends AbstractDynamicCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(LuckyCharm.class.getSimpleName());
    public static final String IMG = makeCardPath("LuckyCharm.png");

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
    private static final int EFFECT = 1;

    private static final int RETAINS = 2;
    private static final int UPGRADE_PLUS_RETAINS = 1;


    // /STAT DECLARATION/


    public LuckyCharm() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = EFFECT;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = RETAINS;
        this.selfRetain = true;
        this.dontTriggerOnUseCard = true;
        setBackgroundTexture(OrangeJuiceMod.GIFT_WHITE_ICE, OrangeJuiceMod.GIFT_WHITE_ICE_PORTRAIT);
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
        this.selfRetain = true;
        rawDescription = DESCRIPTION;
        this.dontTriggerOnUseCard = false;

        this.addToBot(new GainEnergyAction(magicNumber));

        initializeDescription();
    }

    @Override
    public void onRetained() {
        AbstractPlayer p = AbstractDungeon.player;
        this.defaultSecondMagicNumber--; this.defaultBaseSecondMagicNumber--;
        if(this.defaultSecondMagicNumber <= 0){
            this.selfRetain = false;
            rawDescription = SPENT_DESCRIPTION;
        } else {
            this.selfRetain = true;
            rawDescription = DESCRIPTION;
        }
        this.dontTriggerOnUseCard = false;

        initializeDescription();
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (!dontTriggerOnUseCard) {
            this.addToBot(new GainEnergyAction(magicNumber));
        }
    }

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
