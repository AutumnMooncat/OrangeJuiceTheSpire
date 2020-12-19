package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.CheckGoldenAction;
import Moonworks.powers.NormaPower;
import Moonworks.relics.GoldenDie;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGiftCard extends AbstractNormaAttentiveCard {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    private final String description;
    private final String spentDescription;
    private static ArrayList<TooltipInfo> GiftTooltip;
    protected boolean active;
    public boolean checkedGolden;
    public final boolean ignoreGolden;
    public static final int GOLDEN_BUFF = 2;

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, false, false);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, checkedGolden, false);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden, final boolean ignoreGolden) {

        super(id, img, cost, type, color, rarity, target);
        CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        description = cardStrings.DESCRIPTION;
        spentDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.active = false; //Card wont be active until it is in our hand
        if (currentUses <= 0) {
            rawDescription = spentDescription;
        }
        this.defaultSecondMagicNumber = currentUses;
        this.defaultBaseSecondMagicNumber = uses;
        if (uses != currentUses) {
            this.isDefaultSecondMagicNumberModified = true;
        }
        this.checkedGolden = checkedGolden;
        this.ignoreGolden = ignoreGolden;
        /*if (AbstractDungeon.player != null) {
            //checkGolden();
            logger.info("Card created, checking golden? " + !checkedGolden);
            if (!ignoreGolden && !checkedGolden) {
                AbstractDungeon.actionManager.addToBottom(new CheckGoldenAction(this));
            }
        }*/
        this.selfRetain = true; //Let it retain N times
        this.isEthereal = true; //Then it is ethereal
        setBackgroundTexture(OrangeJuiceMod.GIFT_WHITE_ICE, OrangeJuiceMod.GIFT_WHITE_ICE_PORTRAIT);
        initializeDescription();
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add("Gift");
        return tags;
    }

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
        checkGolden(); //This will be the main way we check for the buff
        /*logger.info("Card drawn, checking golden? " + !checkedGolden);
        if (!ignoreGolden && !checkedGolden) {
            this.addToTop(new CheckGoldenAction(this));
        }*/
        //modifyUses(-1); //Dont consume here
        this.active = defaultSecondMagicNumber >= 1;
        initializeDescription();
        super.triggerWhenDrawn();
    }

    @Override
    public void onMoveToDiscard() {
        checkGolden(); //This will happen if the card is generated or transformed, and then we play it to discard it
        /*logger.info("Card discarded, checking golden? " + !checkedGolden);
        if (!ignoreGolden && !checkedGolden) {
            this.addToTop(new CheckGoldenAction(this));
        }*/
        if (this.active) { //Bug testing, uses were being consumed in the discard pile at end of turn.
            modifyUses(-1); //consume 1 use
        }
        this.active = false;
        super.onMoveToDiscard();
    }

    @Override
    public void moveToDiscardPile() {
        super.moveToDiscardPile();
    }

    @Override
    public void onRetained() {
        checkGolden(); //This will happen if the card is generated or transformed, and then not played to discard it
        /*logger.info("Card retained, checking golden? " + !checkedGolden);
        if (!ignoreGolden && !checkedGolden) {
            this.addToTop(new CheckGoldenAction(this));
        }*/
        this.active = defaultSecondMagicNumber >= 1;
        modifyUses(-1); //consume 1 use
        super.onRetained();
    }

    @Override
    public void triggerOnExhaust() {
        this.active = false;
        modifyUses(-1); //Since we want to exhaust if the card has only 1 use (used for the turn then exhausted), decay to 0 for the exhaust pile
        super.triggerOnExhaust();
    }

    public void modifyUses(int uses) {
        this.defaultSecondMagicNumber += uses;
        if(this.defaultSecondMagicNumber <= 1){
            this.selfRetain = false;
            this.isEthereal = true;
            this.exhaust = true;
            //Don't go below 0
            this.defaultSecondMagicNumber = Math.max(defaultSecondMagicNumber, 0);
            if(this.defaultSecondMagicNumber == 0) {
                rawDescription = spentDescription;
            }
        } else {
            this.selfRetain = true;
            this.isEthereal = false;
            this.exhaust = false;
            //We can be allowed to go above max charges
            //this.defaultSecondMagicNumber = Math.min(defaultSecondMagicNumber, defaultBaseSecondMagicNumber);
            rawDescription = description;
        }
        if (defaultSecondMagicNumber != defaultBaseSecondMagicNumber) {
            this.isDefaultSecondMagicNumberModified = true;
        }
        initializeDescription();
    }

    public void checkGolden() {
        if (!ignoreGolden && !checkedGolden) {
            boolean goldenDie = AbstractDungeon.player.hasRelic(GoldenDie.ID);
            this.defaultSecondMagicNumber += (goldenDie ? GOLDEN_BUFF : 0);
            //this.defaultBaseSecondMagicNumber += (goldenDie ? GOLDEN_BUFF : 0);
            if (goldenDie) {
                this.isDefaultSecondMagicNumberModified = true;
            }
            checkedGolden = true;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        /*if (this.defaultSecondMagicNumber == 1) { //If we play the card on it's last use, we want it to exhaust, not discard.
            this.exhaust = true; //This is now handled in modifyUses
        }*/
        initializeDescription();
    }

    public static ArrayList<AbstractGiftCard> getExhaustedGifts() {
        ArrayList<AbstractGiftCard> exhaustedGifts = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            if (card instanceof AbstractGiftCard) {
                exhaustedGifts.add((AbstractGiftCard)card);
            }
        }
        return exhaustedGifts;
    }

    public static AbstractGiftCard getRandomExhaustedGift() {
        ArrayList<AbstractGiftCard> exhaustedGifts = getExhaustedGifts();
        if (exhaustedGifts.isEmpty()) {
            return null;
        } else {
            return exhaustedGifts.get(AbstractDungeon.cardRandomRng.random(1, exhaustedGifts.size()));
        }
    }

    public static boolean purgeRandomExhaustedGift() {
        AbstractGiftCard gift = getRandomExhaustedGift();
        if (gift != null) {
            AbstractDungeon.player.exhaustPile.removeCard(gift);
            return true;
        } else {
            return false;
        }
    }
}