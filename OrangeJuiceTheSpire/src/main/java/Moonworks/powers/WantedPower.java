package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WantedPower extends AbstractTrapPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("WantedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public WantedPower(final AbstractCreature owner, final int amount) {


        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("confusion");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    /* Lets just wrap this into a patch...
    @Override //Should run when any power is applied? Nope...
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        logger.info("Hijacking onApplyPower. Power: "+power+". Target: " +target+". Source: "+source+".");
        AbstractCreature newTarget = target;
        for(AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {//look through monster list
            logger.info("Checking target: "+aM+".");
            if(aM != source && aM.hasPower(WantedPower.POWER_ID) && !aM.isDeadOrEscaped()) {//does one have this power?
                logger.info("Target acquired. "+aM+".");
                newTarget = aM; //new target is that monster
                break;
            }
        }
        if(newTarget != null) { //null check
            logger.info("Setting new target.");
            power.owner = newTarget; //set owner
            AbstractDungeon.actionManager.currentAction.target = newTarget; //set action target
            if (power instanceof HexPower) { //Check for Hex
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(newTarget, newTarget, power.ID)); //Remove it, Hex has no use on a monster
            }
        }
        /* Original code, Darkglade1 - Gensokyo - LunacyPower
        if (source == owner && power.type == PowerType.DEBUFF) { //If it is a debuff and the caster has this power
            AbstractMonster newTarget = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng); //Get a new target
            power.owner = newTarget; //Set the owner of the power to the new target?
            AbstractDungeon.actionManager.currentAction.target = newTarget; //Set the target of this power to the new target
            if (power instanceof HexPower) { //Check for Hex
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(newTarget, newTarget, power.ID)); //Remove it, Hex has no use on a monster
            }
        }//*
    }*/

    @Override
    public void atEndOfRound() {
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
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
        return new WantedPower(owner, amount);
    }
}
