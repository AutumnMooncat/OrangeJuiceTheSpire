package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.patches.PiercingPatches;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Heat300PercentPower extends AbstractTrapPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("Heat300PercentPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    //private int previousBlock;
    private int dmgPercent;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    /**
     * Sets a default 30% increase in damage, use other constructor for a different increase
     * @param owner - Who has the power
     * @param amount - How many turns the power will be active for
     */
    public Heat300PercentPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        this(owner, source, amount, 30);
    }

    /**
     *
     * @param owner - Who has the power
     * @param amount - How many turns the power will be active for
     * @param dmgPercent - An int holding the percentile increase. For a 50% increase enter 50, etc.
     */
    public Heat300PercentPower(final AbstractCreature owner, final AbstractCreature source, final int amount, final int dmgPercent) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.dmgPercent = dmgPercent;

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those txtures here.
        this.loadRegion("noBlock");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void setDmgPercent(int dmgPercent) {
        this.dmgPercent = dmgPercent;
        updateDescription();
    }

    //Apply bonus damage if they have no Block
    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        //If they have no block, deal 30-50% more damage, stacks with Vulnerable
        if (owner.currentBlock <= 0) {
            damage *= (100f + dmgPercent)/100f;
        }
        return super.atDamageReceive(damage, damageType);
    }

    /*//Used to grab the old block values before they get attacked
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if(!this.owner.isDead && this.amount > 0) {
            //Grab the amount of block they used to have, used for piercing calculations
            previousBlock = owner.currentBlock;
        }
        return super.atDamageFinalReceive(damage, type);
    }*/

    //Apply the pierce when we actually attack them
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(!this.owner.isDead && this.amount > 0 && !PiercingPatches.PiercingField.piercing.get(info)) {

            //Needed for piercing and indirect calculations to know how much to modify by
            //int blockDelta = previousBlock - owner.currentBlock;
            int blockDelta = info.output - damageAmount;

            if (blockDelta > 0) {
                //Set up a damage info with the piecing flag.
                DamageInfo pierceDamage = new DamageInfo(source, blockDelta, DamageInfo.DamageType.HP_LOSS);
                PiercingPatches.PiercingField.piercing.set(pierceDamage, true);
                this.addToTop(new DamageAction(owner, pierceDamage, AbstractGameAction.AttackEffect.NONE, true));
            }

        }
        return damageAmount;
    }

    //Decrement this at the end of the round
    @Override
    public void atEndOfRound() {
        this.addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3] + dmgPercent + DESCRIPTIONS[4];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + dmgPercent + DESCRIPTIONS[4];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new Heat300PercentPower(owner, source, amount);
    }
}
