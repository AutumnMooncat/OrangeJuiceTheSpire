package Moonworks.powers;

import Moonworks.actions.DelayedDrawCheckAction;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Moonworks.OrangeJuiceMod;

public class OutOfAmmoPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("OutOfAmmoPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public OutOfAmmoPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("swivel");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    /*
    @Override //Nope
    public void onAfterCardPlayed(AbstractCard usedCard) {
        super.onAfterCardPlayed(usedCard);
        if (AbstractDungeon.actionManager.actions.isEmpty() && AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.player.hasPower("No Draw") && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && (AbstractDungeon.player.discardPile.size() > 0 || AbstractDungeon.player.drawPile.size() > 0)) {
            this.flash();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, amount));
        }
    }*/

    /*
    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        super.onAfterCardPlayed(usedCard);
        if (AbstractDungeon.actionManager.actions.isEmpty() && emptyOrOnlyGiftCards() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.player.hasPower("No Draw") && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && (AbstractDungeon.player.discardPile.size() > 0 || AbstractDungeon.player.drawPile.size() > 0)) {
            this.flash();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, amount));
        }
    }*/


    @Override // Incase we draw another Gift, doesnt work...
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        this.addToBot(new DelayedDrawCheckAction(this, amount));
        /*
        if (emptyOrOnlyGiftCards() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.player.hasPower("No Draw") && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && (AbstractDungeon.player.discardPile.size() > 0 || AbstractDungeon.player.drawPile.size() > 0)) {
            this.flash();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, amount));
        }*/
    }

    @Override //Covers most cases, but strangely not all
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        this.addToBot(new DelayedDrawCheckAction(this, amount));
        /*
        if (emptyOrOnlyGiftCards() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.player.hasPower("No Draw") && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && (AbstractDungeon.player.discardPile.size() > 0 || AbstractDungeon.player.drawPile.size() > 0)) {
            this.flash();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, amount));
        }*/
    }

    /*
    @Override //Hopefully these next 2 cover the rest of the cases
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);
        if (emptyOrOnlyGiftCards() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.player.hasPower("No Draw") && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && (AbstractDungeon.player.discardPile.size() > 0 || AbstractDungeon.player.drawPile.size() > 0)) {
            this.flash();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, amount));
        }
    }

    @Override
    public void onDrawOrDiscard() {
        super.onDrawOrDiscard();
        if (emptyOrOnlyGiftCards() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.player.hasPower("No Draw") && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && (AbstractDungeon.player.discardPile.size() > 0 || AbstractDungeon.player.drawPile.size() > 0)) {
            this.flash();
            this.addToBot(new DrawCardAction(AbstractDungeon.player, amount));
        }
    }*/

    private boolean emptyOrOnlyGiftCards() {
        for(AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action.actionType == AbstractGameAction.ActionType.DRAW){
                return false;
            }
        }
        if (AbstractDungeon.player.hand.isEmpty()) {
            return true;
        }
        for(AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cost != -2) {
                return false;
            }
        }
        return true;
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new OutOfAmmoPower(owner, amount);
    }
}
