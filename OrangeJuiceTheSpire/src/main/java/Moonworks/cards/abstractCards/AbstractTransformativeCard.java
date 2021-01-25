package Moonworks.cards.abstractCards;

import Moonworks.cards.CompletionReward;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;
@Deprecated
public abstract class AbstractTransformativeCard extends AbstractDynamicCard {

    // "How come DefaultCommonAttack extends CustomCard and not DynamicCard like all the rest?"

    // Well every card, at the end of the day, extends CustomCard.
    // Abstract Default Card extends CustomCard and builds up on it, adding a second magic number. Your card can extend it and
    // bam - you can have a second magic number in that card (Learn Java inheritance if you want to know how that works).
    // Abstract Dynamic Card builds up on Abstract Default Card even more and makes it so that you don't need to add
    // the NAME and the DESCRIPTION into your card - it'll get it automatically. Of course, this functionality could have easily
    // Been added to the default card rather than creating a new Dynamic one, but was done so to deliberately.

    protected AbstractCard tempCard;
    protected boolean transformedThisTurn;
    protected boolean transformedThisCombat;
    private static ArrayList<TooltipInfo> CustomTooltip;
    public AbstractTransformativeCard(final String id,
                                      final String img,
                                      final int cost,
                                      final CardType type,
                                      final CardColor color,
                                      final CardRarity rarity,
                                      final CardTarget target) {

        super(id, img, cost, type, color, rarity, target);
        this.transformedThisTurn = false;
        this.transformedThisCombat = false;

    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(BaseMod.getKeywordTitle("moonworks:transformative"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (CustomTooltip == null)
        {
            CustomTooltip = new ArrayList<>();
            CustomTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:transformative"), BaseMod.getKeywordDescription("moonworks:transformative")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(CustomTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }
    public void reinitialize(AbstractCard card) {
        this.cost = card.cost;
        //this.cost = Math.min(card.cost, 0);
        //this.costForTurn = 0;
        //this.isCostModified = card.cost >= 0;
        this.isCostModified = true;
        this.type = card.type;
        this.setDisplayRarity(card.rarity);
        this.target = card.target;
        //this.createCardImage();
        this.name = card.name+"?";
        this.rawDescription = card.rawDescription;
        this.selfRetain = card.selfRetain;
        this.dontTriggerOnUseCard = card.dontTriggerOnUseCard;
        //this.isInnate = card.isInnate; //not needed
        this.isLocked = card.isLocked;
        this.showEvokeValue = card.showEvokeValue;
        this.showEvokeOrbCount = card.showEvokeOrbCount;
        this.keywords = card.keywords;
        //this.isUsed = card.isUsed; //Protected
        //this.upgraded = false; //This will be dealt with elsewhere
        this.timesUpgraded = card.timesUpgraded;
        this.misc = card.misc;
        this.ignoreEnergyOnUse = card.ignoreEnergyOnUse;
        this.isSeen = card.isSeen;
        this.upgradedCost = card.upgradedCost;
        this.upgradedDamage = card.upgradedDamage;
        this.upgradedBlock = card.upgradedBlock;
        this.upgradedMagicNumber = card.upgradedMagicNumber;
        //this.isSelected = false; //Not needed
        //this.exhaust = false; //Counter to the card idea
        //this.returnToHand = false;
        //this.shuffleBackIntoDrawPile = false;
        //this.isEthereal = false;
        this.tags = card.tags;
        this.isMultiDamage = ReflectionHacks.getPrivate(card, AbstractCard.class, "isMultiDamage"); //Use ReflectionHacks
        this.multiDamage = card.multiDamage;
        this.baseDamage = card.baseDamage;
        this.baseBlock = card.baseBlock;
        this.baseMagicNumber = card.baseMagicNumber;
        this.baseHeal = card.baseHeal;
        this.baseDraw = card.baseDraw;
        this.baseDiscard = card.baseDiscard;
        this.damage = card.damage;
        this.block = card.block;
        this.magicNumber = card.magicNumber;
        this.heal = card.heal;
        this.draw = card.draw;
        this.discard = card.discard;

        //Attempt to load picture if this if cloning a basegame card
        this.assetUrl = card.assetUrl;
        this.portrait = card.portrait;
        //Then load the picture if it is a custom card
        if (tempCard instanceof CustomCard) {
            this.textureImg = ((CustomCard) tempCard).textureImg;
            if (textureImg != null) {
                this.loadCardImage(textureImg);
            }
        }

        //All Star Breaker cards will be at least AbstractDefaultCard. Not all cards will be though (Prismatic Shard).
        if (tempCard instanceof AbstractModdedCard) {
            reinitializeExtras((AbstractModdedCard) tempCard);
        }
        //TODO Make this not needed
        if (tempCard instanceof CompletionReward) {
            this.baseDamage = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        }
    }

    public void reinitializeExtras(AbstractModdedCard defaultCard) {
        this.secondMagicNumber = defaultCard.secondMagicNumber;
        this.baseSecondMagicNumber = defaultCard.baseSecondMagicNumber;
        this.upgradedSecondMagicNumber = defaultCard.upgradedSecondMagicNumber;
        this.isSecondMagicNumberModified = defaultCard.isSecondMagicNumberModified;
    }

    public void refreshStats(AbstractCard card) {
        this.dontTriggerOnUseCard = card.dontTriggerOnUseCard;
        this.isMultiDamage = ReflectionHacks.getPrivate(card, AbstractCard.class, "isMultiDamage"); //Use ReflectionHacks
        this.multiDamage = card.multiDamage;
        this.damage = card.damage;
        this.block = card.block;
        this.magicNumber = card.magicNumber;
        this.heal = card.heal;
        this.draw = card.draw;
        this.discard = card.discard;
        //All Star Breaker cards will be at least AbstractDefaultCard. Not all cards will be though (Prismatic Shard).
        if (tempCard instanceof AbstractModdedCard) {
            reinitializeExtras((AbstractModdedCard) tempCard);
        }
    }

    public void temporaryTransform() {
        if (!transformedThisTurn) {
            transformedThisTurn = true;
            transformedThisCombat = true;
            tempCard = AbstractDungeon.returnTrulyRandomCard().makeCopy();
            if (upgraded) {
                tempCard.upgrade();
            }
            reinitialize(tempCard);
            this.initializeTitle();
            this.initializeDescription();
        }
    }

    @Override
    public boolean isStarterStrike() {
        if (transformedThisCombat) {
            return tempCard.isStarterStrike();
        }
        return super.isStarterStrike();
    }

    @Override
    public boolean isStarterDefend() {
        if(transformedThisCombat) {
            return tempCard.isStarterDefend();
        }
        return super.isStarterDefend();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (transformedThisCombat) {
            tempCard.use(p, m);
            refreshStats(tempCard);
        }
    }

    @Override
    public void update() {
        if (transformedThisCombat) {
            tempCard.update();
            refreshStats(tempCard);
        }
        super.update();
    }

    //TODO this wont work. Needs a better solution.
    @Override
    public void moveToDiscardPile() {
        if (transformedThisCombat) {
            tempCard.moveToDiscardPile();
            refreshStats(tempCard);
        }
        super.moveToDiscardPile();
    }

    //TODO this wont work. Needs a better solution.
    @Override
    public void teleportToDiscardPile() {
        if (transformedThisCombat) {
            tempCard.teleportToDiscardPile();
            refreshStats(tempCard);
        }
        super.teleportToDiscardPile();
    }

    //TODO this wont work. Needs a better solution.
    @Override
    public void onMoveToDiscard() {
        if (transformedThisCombat) {
            tempCard.onMoveToDiscard();
            refreshStats(tempCard);
        }
        super.onMoveToDiscard();
    }

    @Override
    public void triggerWhenDrawn() {
        if (transformedThisCombat) {
            tempCard.triggerWhenDrawn();
            refreshStats(tempCard);
        } else {
            super.triggerWhenDrawn();
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        if (transformedThisCombat) {
            tempCard.triggerOnEndOfPlayerTurn();
            refreshStats(tempCard);
        }
        super.triggerOnEndOfPlayerTurn();
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        if (transformedThisCombat) {
            tempCard.triggerOnEndOfTurnForPlayingCard();
            refreshStats(tempCard);
        }
        super.triggerOnEndOfTurnForPlayingCard();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (transformedThisCombat) {
            tempCard.triggerOnOtherCardPlayed(c);
            refreshStats(tempCard);
        }
        super.triggerOnOtherCardPlayed(c);
    }

    @Override
    public void triggerOnGainEnergy(int e, boolean dueToCard) {
        if (transformedThisCombat) {
            tempCard.triggerOnGainEnergy(e, dueToCard);
            refreshStats(tempCard);
        }
        super.triggerOnGainEnergy(e, dueToCard);
    }

    @Override
    public void triggerOnManualDiscard() {
        if (transformedThisCombat) {
            tempCard.triggerOnManualDiscard();
            refreshStats(tempCard);
        }
        super.triggerOnManualDiscard();
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        if (transformedThisCombat) {
            tempCard.triggerOnCardPlayed(cardPlayed);
            refreshStats(tempCard);
        }
        super.triggerOnCardPlayed(cardPlayed);
    }

    @Override
    public void triggerOnScry() {
        if (transformedThisCombat) {
            tempCard.triggerOnScry();
            refreshStats(tempCard);
        }
        super.triggerOnScry();
    }

    @Override
    public void triggerAtStartOfTurn() {
        if (transformedThisCombat) {
            tempCard.triggerAtStartOfTurn();
            refreshStats(tempCard);
        }
        super.triggerAtStartOfTurn();
    }

    @Override
    public void atTurnStart() {
        if (transformedThisCombat) {
            tempCard.atTurnStart();
            refreshStats(tempCard);
        }
        super.atTurnStart();
    }

    @Override
    public void atTurnStartPreDraw() {
        if (transformedThisCombat) {
            tempCard.atTurnStartPreDraw();
            refreshStats(tempCard);
        }
        super.atTurnStartPreDraw();
    }

    @Override
    public void onRetained() {
        if (transformedThisCombat) {
            tempCard.onRetained();
            refreshStats(tempCard);
        }
        super.onRetained();
    }

    @Override
    public void applyPowers() {
        if (transformedThisCombat) {
            tempCard.applyPowers();
            refreshStats(tempCard);
        } else {
            super.applyPowers();
        }
    }

    @Override
    public void calculateDamageDisplay(AbstractMonster mo) {
        if (transformedThisCombat) {
            tempCard.calculateDamageDisplay(mo);
            refreshStats(tempCard);
        } else {
            super.calculateDamageDisplay(mo);
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (transformedThisCombat) {
            tempCard.calculateCardDamage(mo);
            refreshStats(tempCard);
        } else {
            super.calculateCardDamage(mo);
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (transformedThisCombat) {
            return tempCard.canUse(p, m);
        } else {
            return false;
        }
    }

}