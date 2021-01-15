package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.patches.PiercingPatches;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlushieMasterPower extends AbstractTrapPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("PlushieMasterPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final int heal;
    private final int damage;
    private boolean increasedDamage;
    private boolean indirectDamage;
    private int previousBlock;
    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public PlushieMasterPower(final AbstractCreature owner, final AbstractCreature source, final int amount, final int heal, final int damage) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;
        this.heal = heal;
        this.damage = damage;
        this.type = PowerType.BUFF;
        this.priority = Integer.MAX_VALUE;
        this.isTurnBased = false;
        this.canGoNegative = false;

        // We load those txtures here.
        //this.loadRegion("retain");
        this.loadRegion("regrow");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if(!this.owner.isDead && this.amount > 0) {
            if (damage < this.damage) {
                damage = this.damage;
                increasedDamage = true;
                previousBlock = owner.currentBlock;
            }
        }
        indirectDamage = false;
        return super.atDamageFinalReceive(damage, type);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(!this.owner.isDead && this.amount > 0 && !PiercingPatches.PiercingField.piercing.get(info)) {

            //Needed for piercing and indirect calculations to know how much to modify by
            int blockDelta = previousBlock - owner.currentBlock;

            //If we did indirect damage (an attack that did not actually calculate damage on this target, we can do stuff
            if (indirectDamage) {
                //logger.info("Indirect Damage dealt.");
            }

            //If a damage boost as applied, we want to also pierce
            if (increasedDamage) {
                //The amount of damage that was actually blocked is our pierce amount
                if (blockDelta > 0) {
                    //Set up a damage info with the piecing flag.
                    DamageInfo pierceDamage = new DamageInfo(source, blockDelta, DamageInfo.DamageType.HP_LOSS);
                    PiercingPatches.PiercingField.piercing.set(pierceDamage, true);
                    this.addToTop(new DamageAction(owner, pierceDamage, AbstractGameAction.AttackEffect.NONE, true));
                }
            } else {
                //If the damage was not increased, we want Temp HP
                this.addToBot(new AddTemporaryHPAction(source, source, this.heal));
            }

            //Decrement and remove our power. Decrement action is not used as even with ToTop it caused issue with multi hit attacks
            this.amount--;
            if (this.amount <= 0) {
                this.amount = 0;
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
            updateDescription();
        }
        //Package this in an action so multi hit attacks don't get messed up
        this.addToBot(new AbstractGameAction() {
            public void update() {
                increasedDamage = false;
                indirectDamage = true;
                this.isDone = true;
            }});
        return damageAmount;
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + heal + DESCRIPTIONS[1] + damage + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + heal + DESCRIPTIONS[1] + damage + DESCRIPTIONS[3] + amount + DESCRIPTIONS[4];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new PlushieMasterPower(owner, source, amount, heal, damage);
    }
}
