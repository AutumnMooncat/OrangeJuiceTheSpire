package Moonworks.cards.abstractCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.CheckGoldenAction;
import Moonworks.actions.WitherExhaustImmediatelyAction;
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

    protected final String description;
    protected final String spentDescription;
    private static ArrayList<TooltipInfo> GiftTooltip;
    protected boolean active;
    public boolean checkedGolden;
    public final boolean ignoreGolden;
    public static final float GOLDEN_BUFF = 1.5F;

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, false, false, null);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, checkedGolden, false, null);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden, final boolean ignoreGolden) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, checkedGolden, ignoreGolden, null);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, Integer[] normaLevels) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, false, false, normaLevels);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden, Integer[] normaLevels) {

        this(id, img, cost, type, color, rarity, target, uses, currentUses, checkedGolden, false, normaLevels);
    }

    public AbstractGiftCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity,
                            final CardTarget target, final int uses, final int currentUses, final boolean checkedGolden, final boolean ignoreGolden, Integer[] normaLevels) {

        super(id, img, cost, type, color, rarity, target, normaLevels);
        CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        description = cardStrings.DESCRIPTION;
        spentDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.active = false; //Card wont be active until it is in our hand
        if (currentUses <= 0) {
            DESCRIPTION = spentDescription;
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
        tags.add(BaseMod.getKeywordTitle("moonworks:Gift"));
        tags.addAll(super.getCardDescriptors());
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
        this.active = defaultSecondMagicNumber >= 1; //In case we've pulled from exhaust, only set active with uses
        initializeDescription();
        super.triggerWhenDrawn();
    }

    @Override
    public void onMoveToDiscard() {
        checkGolden(); //This will happen if the card is generated or transformed, and then we play it to discard it
        this.active = false; //Cant be active if it isnt in our hand
        super.onMoveToDiscard();
    }

    @Override
    public void moveToDiscardPile() {
        checkGolden(); //May as well check both cases
        this.active = false;
        super.moveToDiscardPile();
    }

    @Override
    public void onRetained() {
        checkGolden(); //This will happen if the card is generated or transformed, and then not played to discard it
        this.active = defaultSecondMagicNumber >= 1; //If we retained it, make sure it still has uses. This can happen if a card force retains an empty gift
        super.onRetained();
    }

    @Override
    public void triggerOnExhaust() {
        this.active = false; //Obviously it shouldn't be active in the exhaust pile
        super.triggerOnExhaust();
    }

    public void modifyUses(int uses) {
        this.defaultSecondMagicNumber += uses;
        if (defaultSecondMagicNumber != defaultBaseSecondMagicNumber) {
            this.isDefaultSecondMagicNumberModified = true;
        }
        boolean hasUses = defaultSecondMagicNumber > 0;
        this.selfRetain = hasUses; //Retain while we have uses
        this.exhaust = !hasUses; //Exhaust and Ethereal if we dont
        this.isEthereal = !hasUses;
        if (!hasUses) { //If we hit 0, or below 0 somehow, exhaust immediately
            this.active = false; //Card cant be active when it has no uses
            this.DESCRIPTION = this.spentDescription;
            initializeDescription(); //Initialize before we move to exhaust
            this.addToTop(new WitherExhaustImmediatelyAction(this)); //Hijack this wither code I wrote before, lol
        } else {
            //We dont set active is true here, since it might not be in our hand, and shouldnt be active if it isnt
            this.DESCRIPTION = this.description;
            initializeDescription();
        }
    }

    public void checkGolden() {
        if (!ignoreGolden && !checkedGolden) {
            boolean goldenDie = AbstractDungeon.player.hasRelic(GoldenDie.ID);
            this.defaultSecondMagicNumber *= (goldenDie ? GOLDEN_BUFF : 1);
            //this.defaultBaseSecondMagicNumber += (goldenDie ? GOLDEN_BUFF : 0);
            if (goldenDie) {
                AbstractDungeon.player.getRelic(GoldenDie.ID).flash();
                this.isDefaultSecondMagicNumberModified = true;
            }
            checkedGolden = true;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        initializeDescription();
    }

    public void applyEffect() { //If we need additional effects, they can be defined on a card by card basis, and simply call this to know to decrement the uses
        //Maybe make the card flash or something nifty?
        this.flash();
        modifyUses(-1); //Reduce our uses by 1 each time the effect happens
        initializeDescription();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m);
    }

    @Override
    public void applyNormaDescriptions(){
        StringBuilder sb = new StringBuilder();
        boolean passedCheck, normaX;
        sb.append(DESCRIPTION);
        if(defaultSecondMagicNumber > 0 && normaLevels != null && normaLevels.size() > 0) {
            for (int i = 0 ; i < normaLevels.size() ; i++) {
                normaX = normaLevels.get(i) == -1;
                passedCheck = getNormaLevel() >= (normaX ? 1 : normaLevels.get(i)); //Could also use absolute value here, but thats less intuitive to read
                sb.append(" NL ");
                sb.append(passedCheck ? upgradeGreen : "*");
                sb.append(BaseMod.getKeywordTitle("moonworks:Norma")).append(" ");
                sb.append(passedCheck ? upgradeGreen : "*");
                sb.append(normaX ? "X" : normaLevels.get(i));
                sb.append(": ");
                sb.append(EXTENDED_DESCRIPTION[i]);
            }
        }
        rawDescription = sb.toString();
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