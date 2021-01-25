package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.interfaces.RangedAttack;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;

public class ImmobilePower extends AbstractTrapPower implements CloneablePowerInterface {

    public static final String POWER_ID = OrangeJuiceMod.makeID("ImmobilePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static class InvisibleBlurPower extends BlurPower implements CloneablePowerInterface, InvisiblePower {

        public InvisibleBlurPower(AbstractCreature owner) {
            super(owner, 0);
        }

        @Override
        public void updateDescription() {
            this.description = "";
        }

        //Acts as a safety net incase some other powers removes Immobile and this isn't also removed at the same time
        private void removeMe() {
            this.owner.powers.remove(this);
        }

        //This will activate before the next turn, so block should hopefully not stick next round
        @Override
        public void atEndOfRound() {
            if (!owner.hasPower(ImmobilePower.POWER_ID)) {
                removeMe();
            }
        }

        @Override
        public AbstractPower makeCopy() {
            return new InvisibleBlurPower(owner);
        }
    }

    private final AbstractPower invisibleRef;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ImmobilePower(final AbstractCreature owner, final int amount) {
        //logger.info("Poppo initializing on " + owner.toString());
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.invisibleRef = new InvisibleBlurPower(owner);
        //this.addToBot(new ApplyPowerAction(owner, owner, invisibleRef));
        this.owner.powers.add(invisibleRef);
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those txtures here.
        this.loadRegion("deva");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    //Can only play Attack cards if they are Ranged Attacks
    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK /*If the card is an attack...*/
                && !(card instanceof RangedAttack /*And it isn't a Ranged attack...*/
                && (!((RangedAttack) card).conditional || ((RangedAttack) card).conditionMet))) { /*Or if it IS a ranged attack that is conditional but we haven't met the condition...*/
            //Then we cant play the card. Set the appropriate message and return false.
            card.cantUseMessage = DESCRIPTIONS[1];
            return false;
        }
        //Otherwise all good.
        return true;
    }

    //This did not work properly
    /*@Override
    public void onRemove() {
        this.owner.powers.remove(invisibleRef);
        super.onRemove();
    }*/

    //Reduce this power by 1 at the start of each turn
    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        flash();
        if (amount == 1) {
            this.owner.powers.remove(invisibleRef);
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    //Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ImmobilePower(owner, amount);
    }
}
